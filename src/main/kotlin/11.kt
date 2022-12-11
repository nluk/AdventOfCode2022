import java.util.ArrayList
import org.huldra.math.BigInt

class Day11 {

    enum class Operator{
        ADD,
        MULTIPLY;

        companion object {
            val lookup = mapOf(
                "+" to ADD,
                "*" to MULTIPLY
            )
        }
    }

    sealed class OwnerAction{
        abstract fun handle(item : Item)
    }

    object Worries : OwnerAction(){
        val WORRY_DIVISOR = BigInt(3)
        override fun handle(item: Item) = item.worryLevel.div(WORRY_DIVISOR)
    }

    data class TooManyMonke(val magicMonkeNumber : BigInt) : OwnerAction(){
        override fun handle(item: Item) = item.worryLevel.mod(magicMonkeNumber)
    }

    class Operation(val operator: Operator, val operand: Operand){

        fun apply(item : Item){
            val a = item.worryLevel
            val b = when(operand){
                is Constant -> operand.value
                is Old -> item.worryLevel.copy()
            }
            when(operator){
                Operator.ADD -> a.add(b)
                Operator.MULTIPLY -> a.mul(b)
            }
        }

        override fun toString(): String {
            return "${operator.name} $operand"
        }
    }

    sealed class Operand

    data class Constant(val value : BigInt) : Operand()

    object Old : Operand()

    data class Item(var worryLevel : BigInt)

    data class Test(val divisor : BigInt, val trueTarget : Int, val falseTarget : Int)

    class Monke(
        val no : String,
        val test : Test,
        val operation : Operation,
        var items : MutableList<Item>,
        var otherMonkeys : List<Monke>,
        var inspectedItems : Long = 0
    ){
        fun act(ownerAction: OwnerAction){
            for(i in 0 until items.size){
                val item = items[i]
                operation.apply(item)
                ownerAction.handle(item)
                val itemCheck = item.worryLevel.copy()
                itemCheck.mod(test.divisor)
                if(itemCheck.isZero){
                    otherMonkeys[test.trueTarget].catchItem(item)
                }
                else{
                    otherMonkeys[test.falseTarget].catchItem(item)
                }
            }
            inspectedItems += items.size
            items.clear()
        }

        fun catchItem(item: Item){
            items.add(item)
        }

        override fun toString(): String {
            return no
        }
    }

    fun parseInput(input : Sequence<String>) : List<Monke>{
        val monkeys = mutableListOf<Monke>()
        for (monkeyLines in input.chunked(7)){
                val items = monkeyLines[1].trim()
                    .substringAfter("Starting items: ")
                    .split(", ")
                    .map { it.toInt() }
                    .map { Item(BigInt(it)) }
                    .toMutableList()

                val resizedItems = ArrayList<Item>(10_000)
                resizedItems.addAll(items)

                val operationDetails = monkeyLines[2].trim().substringAfter("Operation: new = old ")
                val (operatorString, operandString) = operationDetails.split(" ")
                val operator = Operator.lookup[operatorString]!!
                val operand = when(operandString){
                    "old" -> Old
                    else -> Constant(BigInt(operandString.toInt()))
                }
                val operation = Operation(operator, operand)
                val divisor = monkeyLines[3].trim().substringAfter("Test: divisible by ").toInt()
                val trueTarget = monkeyLines[4].trim().substringAfter("If true: throw to monkey ").toInt()
                val falseTarget = monkeyLines[5].trim().substringAfter("If false: throw to monkey ").toInt()
                val test = Test(BigInt(divisor), trueTarget, falseTarget)
            monkeys.add(Monke(monkeyLines[0], test, operation, resizedItems, monkeys))
        }
        return monkeys
    }

    fun solve(monkeys : List<Monke>, rounds : Int, ownerAction: OwnerAction){
        repeat(rounds){
            for (monke in monkeys){
                monke.act(ownerAction)
            }
        }
        monkeys.sortedByDescending { it.inspectedItems }
            .take(2)
            .map { it.inspectedItems }
            .reduce{ acc, item -> acc * item }
            .also(::println)
    }

    fun part1() = solve(day = 11){ input ->
        solve(parseInput(input), 20, Worries)
    }

    fun part2() = solve(day = 11){ input ->
        val monkeys = parseInput(input)
        val magicMonkeNumber = monkeys.asSequence()
            .map { it.test.divisor.copy() }
            .reduce { acc, divisor -> acc.mul(divisor); acc }
        solve(monkeys, 10_000, TooManyMonke(magicMonkeNumber))
    }

}

fun main() = with(Day11()){
    part1()
    part2()
}
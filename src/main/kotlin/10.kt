class Day10 {

    sealed class Instruction{
        abstract val cycleLength : Int
    }
    object Noop : Instruction() {
        override val cycleLength: Int = 1
    }

    data class Addx(val x : Int, override val cycleLength: Int = 2) : Instruction()

    fun parseLine(line : String) : Instruction{
        val instructionData = line.trim().split(" ")
        return when(instructionData.first()){
            "noop" -> Noop
            else -> Addx(instructionData.last().toInt())
        }
    }

    fun part1() = solve(day = 10){ input ->
        val inspectedCycles = setOf(20, 60, 100, 140, 180, 220)
        var register = 1
        var cycle = 0
        val signal = mutableListOf<Int>()
        for(line in input){
            val instruction = parseLine(line)
            repeat(instruction.cycleLength){
                cycle++
                if(cycle in inspectedCycles){
                    signal.add(register * cycle)
                }
            }
            if(instruction is Addx){
                register += instruction.x
            }
        }
        signal.sum().also(::println)
    }

    fun part2() = solve(day = 10){ input ->
        var register = 1
        val crt = (1..6).map {
            (1..40).map { '.' }.toMutableList()
        }
        var crtLine = 0
        var crtPos = 0
        for(line in input){
            val instruction = parseLine(line)
            repeat(instruction.cycleLength){
                if (crtPos >= register - 1 && crtPos <= register + 1){
                    crt[crtLine][crtPos] = '#'
                }
                crtPos++
                if(crtPos == 40){
                    crtPos = 0
                    crtLine++
                }
            }
            if(instruction is Addx){
                register += instruction.x
            }
        }
        crt.asSequence()
            .map { it.joinToString("") }
            .forEach(::println)
    }

}

fun main() = with(Day10()){
    part1()
    part2()
}
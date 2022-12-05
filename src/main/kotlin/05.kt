import java.util.Stack

class Day5 {

    sealed class Crate
    object EmptyCrate : Crate()
    data class LetterCrate(val letter : Char) : Crate()

    data class MoveInstruction(val count : Int, val source : Int, val target : Int)

    fun parseStackLine(stackLine : String) = stackLine
        .chunked(4) //[X]_
        .map { it.replace("[","").replace("]","") }
        .map { if(it.isBlank()) EmptyCrate else LetterCrate(it.trim().first()) }

    fun parseInstructionLine(instructionLine : String) : MoveInstruction {
        val instruction = instructionLine.trim().split(" ")
        return MoveInstruction(instruction[1].toInt(), instruction[3].toInt() - 1, instruction[5].toInt() - 1)
    }

    fun commonParse(input : Sequence<String>) : Pair<List<MoveInstruction>, List<Stack<Char>>> {
        val lines = input.toList()
        var initialStacks = lines.takeWhile { it.isNotBlank() }
        val stacksCount = initialStacks.last().trim().split(" ").last().toInt()
        initialStacks = initialStacks.dropLast(1)
        val instructions = lines.subList(initialStacks.size + 2, lines.size).map { parseInstructionLine(it) }
        val stacks = (1..stacksCount).map { Stack<Char>() }
        initialStacks.asReversed()
            .asSequence()
            .map { parseStackLine(it) }
            .forEach { initialStackLevel ->
                initialStackLevel.forEachIndexed { index, crate ->
                    if(crate is LetterCrate){
                        stacks[index].push(crate.letter)
                    }
                }
            }
        return instructions to stacks
    }

    fun part1() = solve(day = 5){ input ->
        val (instructions, stacks) = commonParse(input)
        for (instruction in instructions){
            val source = stacks[instruction.source]
            val target = stacks[instruction.target]
            var count = instruction.count
            while (count > 0 && source.isNotEmpty()){
                target.add(source.pop())
                count--
            }
        }
        stacks.map { if(it.isNotEmpty()) it.peek() else " " }
            .forEach(::print)
    }

    fun part2() = solve(day = 5){ input ->
        val (instructions, stacks) = commonParse(input)
        for (instruction in instructions){
            val source = stacks[instruction.source]
            val target = stacks[instruction.target]
            var count = instruction.count
            val chunk = mutableListOf<Char>()
            while (count > 0 && source.isNotEmpty()){
                chunk.add(source.pop())
                count--
            }
            chunk.reversed().forEach(target::push)
        }
        stacks.map { if(it.isNotEmpty()) it.peek() else " " }
            .forEach(::print)
    }
}

fun main() = with(Day5()){
    part1()
    println()
    part2()
}
class Day6 {

    fun findMarkerPosition(distinctCount : Int, input : Sequence<String>) : Int{
        var checked = 0
        input.first()
            .asSequence()
            .windowed(distinctCount)
            .first {
                checked++
                it.toHashSet().size == distinctCount
            }
        return checked + distinctCount - 1
    }

    fun part1() = solve(day = 6){ input ->
        println(findMarkerPosition(4, input))
    }

    fun part2() = solve(day = 6){ input ->
        println(findMarkerPosition(14, input))
    }

}

fun main() = with(Day6()){
    part1()
    part2()
}
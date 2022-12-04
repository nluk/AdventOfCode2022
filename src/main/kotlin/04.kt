class Day4 {

    fun IntRange.contains(other : IntRange) = this.start <= other.start && this.endInclusive >= other.endInclusive

    fun IntRange.overlaps(other : IntRange) = maxOf(this.start, other.start) <= minOf(this.endInclusive, other.endInclusive)

    fun parseRange(rangeString : String) : IntRange {
        val (a, b) = rangeString.split("-")
        return a.toInt()..b.toInt()
    }

    fun parseRangePairs(line : String) : Pair<IntRange, IntRange> {
        val (a, b) = line.trim().split(",")
        return parseRange(a) to parseRange(b)
    }

    fun part1() = solve(day = 4){ input ->
        input.map { parseRangePairs(it)}
            .filter { (rangeA, rangeB) -> rangeA.contains(rangeB) or rangeB.contains(rangeA) }
            .count()
            .also(::println)
    }

    fun part2() = solve(day = 4){ input ->
        input.map { parseRangePairs(it)}
            .filter { (rangeA, rangeB) -> rangeA.overlaps(rangeB) }
            .count()
            .also(::println)
    }

}

fun main() = with(Day4()){
    part1()
    part2()
}
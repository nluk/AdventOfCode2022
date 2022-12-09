import kotlin.math.abs

class Day9 {

    val directions = mapOf(
        "L" to Point(-1, 0),
        "R" to Point(1,0),
        "U" to Point(0, 1),
        "D" to Point(0, -1)
    )

    data class Point(val x : Int, val y : Int){
        fun isApartFrom(other : Point) : Boolean {
            return maxOf(abs(this.x - other.x),  abs(this.y - other.y)) > 1
        }

        fun isApartHorizontally(other: Point) : Boolean{
            return abs(this.x - other.x) > 0
        }

        fun isApartVertically(other: Point) : Boolean{
            return abs(this.y - other.y) > 0
        }

        fun isToTheRightOf(other: Point) : Boolean{
            return this.x > other.x
        }

        fun isAbove(other: Point) : Boolean{
            return this.y > other.y
        }
    }

    fun parseLine(line : String) : Pair<Point, Int>{
        val (direction, amount) = line.trim().split(" ")
        return directions[direction]!! to amount.toInt()
    }

    fun countTailVisitedPoints(rope : MutableList<Point>, input : Sequence<String>) : Int {
        val tailVisitedPoints = mutableSetOf(rope.first())
        input.map(this::parseLine)
            .forEach { (direction, amount) ->
                repeat(amount) {
                    val head = rope.first()
                    rope[0] = head.copy(x = head.x + direction.x, y = head.y + direction.y)
                    (1 until rope.size).forEach { i ->
                        val previous = rope[i - 1]
                        var next = rope[i]
                        while (next.isApartFrom(previous)){
                            if(next.isApartHorizontally(previous)){
                                next = when (previous.isToTheRightOf(next)){
                                    true -> next.copy(x = next.x + 1)
                                    false -> next.copy(x = next.x + -1)
                                }
                            }
                            if(next.isApartVertically(previous)){
                                next = when (previous.isAbove(next)){
                                    true -> next.copy(y = next.y + 1)
                                    false -> next.copy(y = next.y + -1)
                                }
                            }
                            rope[i] = next
                        }
                    }
                    tailVisitedPoints.add(rope.last())
                }
            }
        return tailVisitedPoints.size
    }

    fun part1() = solve(day = 9){ input ->
        val rope = mutableListOf(Point(0, 0), Point(0, 0))
        countTailVisitedPoints(rope, input)
            .also(::println)
    }

    fun part2() = solve(day = 9){ input ->
        val rope = (1..10)
            .map { Point(0, 0) }
            .toMutableList()
        countTailVisitedPoints(rope, input)
            .also(::println)
    }

}

fun main() = with(Day9()){
    part1()
    part2()
}
class Day8(input : Sequence<String>) {

    val directions = listOf(
        0 to -1,
        0 to 1,
        -1 to 0,
        1 to 0
    )

    val forest : List<List<Int>> = input.map {
        it.asSequence().map { it.digitToInt() }.toList()
    }.toList()

    val forestLength = forest.size
    val forestWidth = forest.first().size

    fun isVisible(i : Int, j : Int) : Boolean {
        val tree = forest[i][j]
        val visible = mutableMapOf<Pair<Int, Int>, Boolean>()
        for (direction in directions){
            visible[direction] = true
            var x = i
            var y = j
            while (true){
                x += direction.first
                y += direction.second
                if(x >= 0 && x < forest.size && y >= 0 && y < forest[i].size){
                    val checkedTree = forest[x][y]
                    if(checkedTree >= tree){
                        visible[direction] = false
                        break
                    }
                }
                else break
            }
        }
        return visible.any { it.value }
    }

    fun scenicScore(i : Int, j : Int) : Int {
        val tree = forest[i][j]
        var scenicScore = 1
        for (direction in directions){
            var viewingDistance = 0
            var x = i
            var y = j
            while (true){
                x += direction.first
                y += direction.second
                if(x >= 0 && x < forest.size && y >= 0 && y < forest[i].size){
                    val checkedTree = forest[x][y]
                    viewingDistance++
                    if(checkedTree >= tree){
                        break
                    }
                }
                else break
            }
            scenicScore *= viewingDistance
        }
        return scenicScore
    }

    fun part1() = solve(day = 8){ input ->
        var visible = 2 * forestLength
        visible += 2 * forestWidth - 4 // exclude overlapping corners
        for( i in 1 until  forestLength - 1){
            for(j in 1 until  forestWidth - 1){
                if(isVisible(i, j)){
                    visible++
                }
            }
        }
        println(visible)
    }

    fun part2() = solve(day = 8){ input ->
        sequence{
            for( i in 1 until  forestLength - 1){
                for(j in 1 until  forestWidth - 1){
                    yield(scenicScore(i, j))
                }
            }
        }.max()
         .also(::println)
    }

}

fun main() = solve(day = 8){ input ->
    with(Day8(input)){
        part1()
        part2()
    }
}
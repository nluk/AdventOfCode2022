
class Day3 {

    val priorities = sequence{
        yieldAll('a'..'z')
        yieldAll('A'..'Z')
    }.mapIndexed{ index, char ->
        char to (index + 1)
    }.toMap()

    fun part1() = solve(day = 3){ input ->
        input.map { rucksack ->
            val compartmentSize = rucksack.length / 2
            val firstCompartment = rucksack.take(compartmentSize)
                .asSequence()
                .toSet()
            val secondCompartment = rucksack.takeLast(compartmentSize)
                .asSequence()
                .toSet()
            firstCompartment.intersect(secondCompartment)
        }
            .map { it.first() }
            .map { priorities[it]!! }
            .sum()
            .also(::println)
    }

    fun part2() = solve(day = 3){ input ->
        input.chunked(3)
            .map { group ->
                group.map { it.asSequence().toSet() }
                    .reduce{ prev, next -> prev.intersect(next) }
                    .first()
            }
            .map { priorities[it]!! }
            .sum()
            .also(::println)
    }
}

fun main(){
    with(Day3()){
        part1()
        part2()
    }
}
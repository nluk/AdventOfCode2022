class Day2{
    enum class Move(val score : Int){
        Rock(1),
        Paper(2),
        Scissors(3)
    }

    enum class Result(val score : Int){
        Lost(0),
        Draw(3),
        Win(6)
    }

    val moves = mapOf(
        "A" to Move.Rock,
        "B" to Move.Paper,
        "C" to Move.Scissors,
        "X" to Move.Rock,
        "Y" to Move.Paper,
        "Z" to Move.Scissors
    )

    val winners = mapOf(
        Move.Scissors to Move.Rock,
        Move.Rock to Move.Paper,
        Move.Paper to Move.Scissors
    )

    val losers = winners.entries.asSequence()
        .map { (a, b) -> b to a }
        .toMap()

    val expectedResults = mapOf(
        "Y" to Result.Draw,
        "X" to Result.Lost,
        "Z" to Result.Win
    )

    fun playerResult(opponentMove: Move, playerMove: Move) = when(playerMove){
        opponentMove -> Result.Draw
        winners[opponentMove] -> Result.Win
        else -> Result.Lost
    }


    fun part1() = solve(day = 2) { input ->
        input.map { line -> line.trim().split(" ") }
            .map { round -> moves[round.first()]!! to moves[round.last()]!! }
            .map { (opponentMove, myMove) -> playerResult(opponentMove, myMove).score + myMove.score }
            .sum()
            .also(::println)
    }

    fun selectMove(opponentMove: Move, expectedResult: Result) = when(expectedResult){
        Result.Draw -> opponentMove
        Result.Win -> winners[opponentMove]
        else -> losers[opponentMove]
    }

    fun part2() = solve(day = 2) { input ->
        input.map { line -> line.trim().split(" ") }
            .map { round -> moves[round.first()]!! to expectedResults[round.last()]!! }
            .map { (opponentMove, expectedResult) -> expectedResult.score + selectMove(opponentMove, expectedResult)!!.score }
            .sum()
            .also(::println)
    }
}


fun main(){
    val day2 = Day2()
    day2.part1()
    day2.part2()
}
fun main() = solve(day = 1) { input ->
    var calories = 0
    val carriedCalories = mutableListOf<Int>()
    for(line in input){
        if(line.isBlank()){
            carriedCalories += calories
            calories = 0
        }
        else{
            calories += line.toInt()
        }
    }
    carriedCalories += calories
    carriedCalories.sortDescending()
    println("Max: ${carriedCalories.first()}")
    println("Top three: ${carriedCalories.take(3).sum()}")
}
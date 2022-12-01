
fun solve(day : Int, solution : (Sequence<String>) -> Unit){
    {}::class.java
        .getResourceAsStream("$day.txt")!!
        .bufferedReader()
        .useLines {
           solution(it)
        }
}
import java.util.TreeSet

class Day12 {

    data class Edge(val v1: String, val v2: String, val dist: Int)

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    class Vertex(val name: String) : Comparable<Vertex> {

        var dist = Int.MAX_VALUE  // MAX_VALUE assumed to be infinity
        var previous: Vertex? = null
        val neighbours = HashMap<Vertex, Int>()


        fun pathSize() : List<Vertex>{
            var v : Vertex? = this
            val path = mutableListOf<Vertex>()
            while (v != null && v.previous != v ){
                path.add(v)
                v = v.previous
            }
            return path
        }

        fun printPath() {
            if (this == previous) {
                print(name)
            }
            else if (previous == null) {
                print("$name(unreached)")
            }
            else {
                previous!!.printPath()
                print(" -> $name($dist)")
            }
        }

        override fun compareTo(other: Vertex): Int {
            if (dist == other.dist) return name.compareTo(other.name)
            return dist.compareTo(other.dist)
        }

        override fun toString() = "($name, $dist)"
    }

    class Graph(
        val edges: List<Edge>,
        val directed: Boolean,
        val showAllPaths: Boolean = false
    ) {
        // mapping of vertex names to Vertex objects, built from a set of Edges
        private val graph = HashMap<String, Vertex>(edges.size)

        init {
            // one pass to find all vertices
            for (e in edges) {
                if (!graph.containsKey(e.v1)) graph.put(e.v1, Vertex(e.v1))
                if (!graph.containsKey(e.v2)) graph.put(e.v2, Vertex(e.v2))
            }

            // another pass to set neighbouring vertices
            for (e in edges) {
                graph[e.v1]!!.neighbours.put(graph[e.v2]!!, e.dist)
                // also do this for an undirected graph if applicable
                if (!directed) graph[e.v2]!!.neighbours.put(graph[e.v1]!!, e.dist)
            }
        }

        /** Runs dijkstra using a specified source vertex */
        fun dijkstra(startName: String) {
            if (!graph.containsKey(startName)) {
                println("Graph doesn't contain start vertex '$startName'")
                return
            }
            val source = graph[startName]
            val q = TreeSet<Vertex>()

            // set-up vertices
            for (v in graph.values) {
                v.previous = if (v == source) source else null
                v.dist = if (v == source)  0 else Int.MAX_VALUE
                q.add(v)
            }

            dijkstra(q)
        }

        /** Implementation of dijkstra's algorithm using a binary heap */
        private fun dijkstra(q: TreeSet<Vertex>) {
            while (!q.isEmpty()) {
                // vertex with shortest distance (first iteration will return source)
                val u = q.pollFirst()
                // if distance is infinite we can ignore 'u' (and any other remaining vertices)
                // since they are unreachable
                if (u.dist == Int.MAX_VALUE) break

                //look at distances to each neighbour
                for (a in u.neighbours) {
                    val v = a.key // the neighbour in this iteration

                    val alternateDist = u.dist + a.value
                    if (alternateDist < v.dist) { // shorter path to neighbour found
                        q.remove(v)
                        v.dist = alternateDist
                        v.previous = u
                        q.add(v)
                    }
                }
            }
        }

        /** Prints a path from the source to the specified vertex */
        fun printPath(endName: String) {
            if (!graph.containsKey(endName)) {
                println("Graph doesn't contain end vertex '$endName'")
                return
            }
            print(if (directed) "Directed   : " else "Undirected : ")
            graph[endName]!!.printPath()
            println()
            if (showAllPaths) printAllPaths() else println()
        }

        /** Prints the path from the source to every vertex (output order is not guaranteed) */
        private fun printAllPaths() {
            for (v in graph.values) {
                v.printPath()
                println()
            }
            println()
        }

        fun vertex(name : String) : Vertex? {
            return graph[name]
        }
    }


    val directions = listOf(
        0 to -1,
        0 to 1,
        -1 to 0,
        1 to 0
    )

    fun Char.elevationValue() : Int = when(this){
        'S' -> 0
        'E' -> 'z'.code - 'a'.code
        else -> this.code - 'a'.code
    }

    fun part1() = solve(day = 12){ input ->
        val dataGrid = input.map { line ->
            line.trim()
                .asSequence()
                .toList()
        }.toList()

        var start : String = ""
        var end : String = ""

        val edges = mutableListOf<Edge>()

        for(i in 0 until dataGrid.size){
            for (j in 0 until dataGrid[i].size){
                val currentElevation = dataGrid[i][j]
                val currentElevationValue = currentElevation.elevationValue()
                if(currentElevation == 'S'){
                    start = "[$i][$j]($currentElevation)"
                }
                else if(currentElevation == 'E'){
                    end = "[$i][$j]($currentElevation)"
                }
                for ((dx, dy) in directions){
                    val x = i + dx
                    val y = j + dy
                    if(x >= 0 && x < dataGrid.size && y >= 0 && y < dataGrid[x].size){
                        val possibleElevation = dataGrid[x][y]
                        val possibleElevationValue = possibleElevation.elevationValue()
                        if(currentElevationValue >= possibleElevationValue - 1){
                            edges.add(Edge("[$i][$j]($currentElevation)", "[$x][$y]($possibleElevation)", 1))
                        }
                        if(possibleElevationValue >= currentElevationValue - 1){
                            edges.add(Edge("[$x][$y]($possibleElevation)", "[$i][$j]($currentElevation)", 1))
                        }
                    }
                }
            }
        }


        val graph = Graph(edges, true)
        graph.dijkstra(start)
        val p = graph.vertex(end)
        p?.pathSize()?.size.also(::println)
    }

    fun part2() = solve(day = 12){ input ->
        val dataGrid = input.map { line ->
            line.trim()
                .asSequence()
                .toList()
        }.toList()
        var end = ""
        val possibleStarts = mutableListOf<String>()
        val edges = mutableListOf<Edge>()

        for(i in 0 until dataGrid.size){
            for (j in 0 until dataGrid[i].size){
                val currentElevation = dataGrid[i][j]
                val currentElevationValue = currentElevation.elevationValue()
                if(currentElevation == 'S' || currentElevation == 'a'){
                    possibleStarts.add("[$i][$j]($currentElevation)")
                }
                else if(currentElevation == 'E'){
                    end = "[$i][$j]($currentElevation)"
                }
                for ((dx, dy) in directions){
                    val x = i + dx
                    val y = j + dy
                    if(x >= 0 && x < dataGrid.size && y >= 0 && y < dataGrid[x].size){
                        val possibleElevation = dataGrid[x][y]
                        val possibleElevationValue = possibleElevation.elevationValue()
                        if(currentElevationValue >= possibleElevationValue - 1){
                            edges.add(Edge("[$i][$j]($currentElevation)", "[$x][$y]($possibleElevation)", 1))
                        }
                        if(possibleElevationValue >= currentElevationValue - 1){
                            edges.add(Edge("[$x][$y]($possibleElevation)", "[$i][$j]($currentElevation)", 1))
                        }
                    }
                }
            }
        }

        val graph = Graph(edges, true)
        possibleStarts.map {
            graph.dijkstra(it)
            val p = graph.vertex(end)
            p!!.pathSize()
        }.filter { path ->
            path.any { pathSegment -> !pathSegment.name.contains('E') }
        }.minBy {
            it.size
        }.size.also(::println)
    }

}

fun main() = with(Day12()){
    part1()
    part2()
}
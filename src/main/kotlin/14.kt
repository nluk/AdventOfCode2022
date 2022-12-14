class Day14 {

    data class Point(val x : Int, val y : Int){
        fun down() : Point = this.copy(y = this.y + 1)
        fun leftDiagonally() : Point = this.copy(x = this.x - 1, y = this.y + 1)
        fun rightDiagonally() : Point = this.copy(x = this.x + 1, y = this.y + 1)

        override fun toString(): String {
            return "($x, $y)"
        }
    }

    data class Line(val start : Point, val end : Point)

    enum class Particle(val c: Char) {
        EMPTY('.'),
        SAND('o'),
        ROCK('#');
    }

    data class Cave(
        val lastHopeLedgeHeight : Int,
        val particles : MutableMap<Point, Particle>
    )

    val sandEntrance = Point(500, 0)

    fun parseInput(input : Sequence<String>) : Cave{
        val lines = input.map {
            it.trim()
        }.map {
            it.split(" -> ")
        }.flatMap {
            it.windowed(2)
                .map { (aStr, bStr) ->
                    val (ax, ay) = aStr.split(",")
                    val (bx, by) = bStr.split(",")
                    val a = Point(ax.toInt(), ay.toInt())
                    val b = Point(bx.toInt(), by.toInt())
                    Line(a, b)
                }
        }.toList()
        val particles = mutableMapOf<Point, Particle>()
        var maxY = 0
        for(line in lines){
            maxY = maxOf(maxY, maxOf(line.start.y, line.end.y))
            if(line.start.x == line.end.x){
                for(y in minOf(line.start.y, line.end.y)..maxOf(line.start.y, line.end.y)){
                    particles[Point(line.start.x, y)] = Particle.ROCK
                }
            }
            else {
                for(x in minOf(line.start.x, line.end.x)..maxOf(line.start.x, line.end.x)){
                    particles[Point(x, line.start.y)] = Particle.ROCK
                }
            }
        }
        return Cave(maxY, particles)
    }

    fun part1() = solve(day = 14){ input ->
        val cave = parseInput(input)
        var sand = sandEntrance
        var particlesSettled = 0
        while (true){
            if(sand.down().y > cave.lastHopeLedgeHeight){
                break
            }
            if(cave.particles.getOrDefault(sand.down(), Particle.EMPTY) == Particle.EMPTY){
                sand = sand.down()
            }
            else if(cave.particles.getOrDefault(sand.leftDiagonally(), Particle.EMPTY) == Particle.EMPTY){
                sand = sand.leftDiagonally()
            }
            else if(cave.particles.getOrDefault(sand.rightDiagonally(), Particle.EMPTY) == Particle.EMPTY){
                sand = sand.rightDiagonally()
            }
            else {
                cave.particles[sand] = Particle.SAND
                particlesSettled++
                sand = sandEntrance
            }
        }
        println(particlesSettled)
    }

    fun part2() = solve(day = 14){ input ->
        val cave = parseInput(input)
        var sand = sandEntrance
        var particlesSettled = 0

        val getParticle : (Point) -> Particle = { point ->
            var particle = cave.particles.getOrDefault(point, Particle.EMPTY)
            if(point.y == cave.lastHopeLedgeHeight + 2){
                particle = Particle.ROCK
            }
            particle
        }

        while (true){
            if(cave.particles[sandEntrance] == Particle.SAND){
                break
            }
            if(getParticle(sand.down()) == Particle.EMPTY){
                sand = sand.down()
            }
            else if(getParticle(sand.leftDiagonally()) == Particle.EMPTY){
                sand = sand.leftDiagonally()
            }
            else if(getParticle(sand.rightDiagonally()) == Particle.EMPTY){
                sand = sand.rightDiagonally()
            }
            else {
                cave.particles[sand] = Particle.SAND
                particlesSettled++
                sand = sandEntrance
            }
        }
        println(particlesSettled)
    }

}

fun main() = with(Day14()){
    part1()
    part2()
}
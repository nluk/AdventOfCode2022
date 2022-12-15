import kotlin.math.absoluteValue

class Day15 {

    data class Beacon(val x : Int, val y : Int)

    data class Sensor(val x : Int, val y : Int){
        fun distanceToBeacon(beacon: Beacon) : Int{
            return (this.x - beacon.x).absoluteValue + (this.y - beacon.y).absoluteValue
        }
    }

    fun parseInput(input : Sequence<String>) : Sequence<Pair<Sensor, Beacon>> {
        return input.map { line ->
            val (sensorX, sensorY) = line.substringAfter("Sensor at ")
                .substringBefore(":")
                .split(", ")
            val (beaconX, beaconY) = line.substringAfter("beacon is at ")
                .split(", ")
            val sensor = Sensor(sensorX.substringAfter("=").toInt(), sensorY.substringAfter("=").toInt())
            val beacon = Beacon(beaconX.substringAfter("=").toInt(), beaconY.substringAfter("=").toInt())
            sensor to beacon
        }
    }

    fun part1() = solve(day = 15){ input ->
        val devices = parseInput(input).toList()
        val minX = devices.asSequence()
            .map { (sensor, beacon) -> sensor.x - sensor.distanceToBeacon(beacon) }
            .min()
        val maxX = devices.asSequence()
            .map { (sensor, beacon) -> sensor.x + sensor.distanceToBeacon(beacon) }
            .max()

        val cannotBePresent = mutableSetOf<Beacon>()
        for (x in minX..maxX){
            val pseudoBeacon = Beacon(x, 2000000)
            var matchingBeacon : Beacon? = null
            var canBePresent = true
            for((sensor, beacon) in devices){
                if(sensor.distanceToBeacon(pseudoBeacon) <= sensor.distanceToBeacon(beacon)){
                    canBePresent = false
                }
                if(beacon == pseudoBeacon){
                    matchingBeacon = beacon
                }
            }
            if(!canBePresent && matchingBeacon == null){
                cannotBePresent.add(pseudoBeacon)
            }
        }
        println(cannotBePresent.size)
    }

    fun part2() = solve(day = 15){ input ->
        val devices = parseInput(input).toList()
        val maximumLine = 4000000
        val impossibleSegmentsPerLine = Array<MutableList<IntRange>>(maximumLine + 1) { mutableListOf() }
        devices.forEach { (sensor, beacon) ->
            for (lineNo in 0..maximumLine) {
                val distanceToBeacon = sensor.distanceToBeacon(beacon)
                val distanceToLine = (sensor.y - lineNo).absoluteValue
                val lineCoverage = distanceToBeacon - distanceToLine
                if (lineCoverage > 0) {
                    impossibleSegmentsPerLine[lineNo].add(sensor.x - lineCoverage..sensor.x + lineCoverage)
                }
            }
        }
        var distressBeacon : Beacon? = null
        for((lineNo, segments) in impossibleSegmentsPerLine.withIndex()){
            val sortedSegments = segments.sortedBy { it.start } //Segments from left to right
            var leftmostSegmentEnd = sortedSegments.first().endInclusive
            for (segment in sortedSegments.drop(1)){
                if (segment.start > leftmostSegmentEnd) {
                    distressBeacon = Beacon(segment.start - 1, lineNo)
                    break
                }
                if (segment.endInclusive > leftmostSegmentEnd) {
                    leftmostSegmentEnd = segment.endInclusive
                }
            }
            if(distressBeacon != null){
                break
            }
        }
        println((distressBeacon!!.x.toBigInteger() * maximumLine.toBigInteger()) + distressBeacon.y.toBigInteger())
    }
}

fun main() = with(Day15()){
    part1()
    part2()
}
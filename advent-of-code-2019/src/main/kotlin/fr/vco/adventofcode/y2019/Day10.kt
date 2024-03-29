package fr.vco.adventofcode.y2019

import java.io.InputStreamReader
import kotlin.math.*

fun main() {


    val input = getInputReader("Day10")
    println("Part 1 : ${getBestDetection(input)}")

    val inputPart2 = getInputReader("Day10")
    println("Part 2 : ${getDestroyedAsteroidAt(inputPart2, 200)}")
}

fun getBestDetection(input: InputStreamReader): Int {
    val map = createMap(input)
    return map.map { it.getDetectedAsteroids(map).size }.max()!!
}

fun getDestroyedAsteroidAt(input: InputStreamReader, index: Int): Int {
    val map = createMap(input).toMutableList()

    val laser = map.map { it to it.getDetectedAsteroids(map) }.maxBy { it.second.size }!!
    val laserCoord = laser.first
    var destroyeds = laser.second

    var i = index
    while (destroyeds.size <= i) {
        i -= destroyeds.size
        map.removeAll(destroyeds)
        destroyeds = laserCoord.getDetectedAsteroids(map)
    }

    val sorted = destroyeds.sortedBy { laserCoord.getAngle(it) }
    val c = sorted[i - 1]
    return c.x * 100 + c.y
}


fun createMap(input: InputStreamReader): List<Asteroid> {
    val map = mutableListOf<Asteroid>()
    input.readLines().forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') map.add(Asteroid(x, y))
        }
    }
    return map
}


data class Asteroid(val x: Int, val y: Int) {

    fun isAlign(other: Asteroid, other2: Asteroid): Boolean {
        if (this.y == other.y && this.y == other2.y) return true
        val ratio1 = (this.x - other.x) * 1f / (this.y - other.y)
        val ratio2 = (this.x - other2.x) * 1f / (this.y - other2.y)
        return ratio1 == ratio2
    }

    fun isBetween(origin: Asteroid, target: Asteroid): Boolean {
        val xMin = min(origin.x, target.x)
        val xMax = max(origin.x, target.x)
        val yMin = min(origin.y, target.y)
        val yMax = max(origin.y, target.y)
        return (this.x in xMin..xMax && this.y in yMin..yMax)
    }

    fun canDetect(target: Asteroid, map: List<Asteroid>): Boolean {
        if (this == target) return false
        return map.filter { target != it && this != it }
            .filter { this.isAlign(target, it) }
            .filter { it.isBetween(this, target) }.isEmpty()
    }

    fun getDetectedAsteroids(map: List<Asteroid>) =
        map.filter { this.canDetect(it, map) }

    fun getAngle(other: Asteroid): Double {
        val dx = (this.x - other.x).toDouble()
        val dy = (this.y - other.y).toDouble()
        val atan = -atan2(dx, dy)
        return if(atan<0) atan + 2*PI else atan
    }

}

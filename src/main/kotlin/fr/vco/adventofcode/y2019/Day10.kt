package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import java.io.InputStreamReader
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    val inputTest1 = getInputReader("/2019/inputDay10Test1.txt") // Should be 33
    val inputTest2 = getInputReader("/2019/inputDay10Test2.txt") // Should be 35
    val inputTest3 = getInputReader("/2019/inputDay10Test3.txt") // Should be 41
    val inputTest4 = getInputReader("/2019/inputDay10Test4.txt") // Should be 210
    val input = getInputReader("/2019/inputDay10.txt")

    println("Test 1 : ${getBestDetection(inputTest1)}")
    println("Test 2 : ${getBestDetection(inputTest2)}")
    println("Test 3 : ${getBestDetection(inputTest3)}")
    println("Test 4 : ${getBestDetection(inputTest4)}")
    println("Part 1 : ${getBestDetection(input)}")
}


fun getBestDetection(input: InputStreamReader): Int {

    val map = mutableListOf<Asteroide>()
    input.readLines().forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') map.add(Asteroide(x, y))
        }
    }
    return map.map { it.getDetectionCount(map) }.max()!!
}

data class Asteroide(val x: Int, val y: Int) {

    fun isAlign(other: Asteroide, other2: Asteroide): Boolean {
        if (this.y == other.y && this.y == other2.y) return true
        val ratio1 = (this.x - other.x) * 1f / (this.y - other.y)
        val ratio2 = (this.x - other2.x) * 1f / (this.y - other2.y)
        return ratio1 == ratio2
    }

    fun isBetween(origin: Asteroide, target: Asteroide): Boolean {
        val xMin = min(origin.x, target.x)
        val xMax = max(origin.x, target.x)
        val yMin = min(origin.y, target.y)
        val yMax = max(origin.y, target.y)
        return (this.x in xMin..xMax && this.y in yMin..yMax)
    }

    fun canDetect(target: Asteroide, map: List<Asteroide>): Boolean {
        if (this == target) return false
        return map.filter { target != it && this != it }
            .filter { this.isAlign(target, it) }
            .filter { it.isBetween(this, target) }.isEmpty()
    }

    fun getDetectionCount(map: List<Asteroide>) =
        map.filter { this.canDetect(it, map) }.size

}

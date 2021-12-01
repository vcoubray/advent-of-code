package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day01").map { it.toInt() }

    println("Part 1 : ${getIncreased(input, 1)}")
    println("Part 2 : ${getIncreased(input, 3)}")
}

fun getIncreased(depths: List<Int>, range: Int): Int {
    val depthRanges = List(depths.size - range + 1) { depths.subList(it, it + range).sum() }
    var increaseCount = 0
    for (i in 1 until depthRanges.size) {
        if (depthRanges[i] > depthRanges[i - 1]) increaseCount++
    }
    return increaseCount
}

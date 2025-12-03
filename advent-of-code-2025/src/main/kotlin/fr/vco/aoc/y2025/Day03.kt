package fr.vco.aoc.y2025

fun main() {
    val input = readLines("Day03")

    val banks = input.map { bank -> bank.map { it.digitToInt() } }

    println("Part 1 : ${banks.sumOf { it.maxVoltage() }}")
    println("Part 2 : ${banks.sumOf { it.maxVoltage(12) }}")

}

fun List<Int>.maxVoltage(batteryCount: Int = 2): Long {
    var startId = 0
    val batteries = (batteryCount - 1 downTo 0).map { lastId ->
        val currentList = this.drop(startId).dropLast(lastId)
        val max = currentList.max()
        startId += currentList.indexOf(max) + 1
        max
    }
    return batteries.joinToString("").toLong()
}

package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day02")

    println("part 1 : ${input.toReports().count { it.isSafe() }}")
    println("part 2 : ${input.toReports().count { it.isDampenerSafe() }}")
}

fun List<String>.toReports() = map { it.split(" ").map { n -> n.toInt() } }
    .flatMap { listOf(it, it.reversed()) }

fun List<Int>.isSafe() = windowed(2).all { (a, b) -> (b - a) in 1..3 }
fun List<Int>.isDampenerSafe(): Boolean {
    return if (isSafe()) true
    else indices.map { take(it) + drop(it + 1) }.any { it.isSafe() }
}
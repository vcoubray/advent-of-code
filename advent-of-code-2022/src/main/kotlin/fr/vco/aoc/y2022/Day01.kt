package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day01")

    val elvesPackages = input
        .split { it.isEmpty() }
        .map { it.sumBy { calorie -> calorie.toInt() } }

    println("Part 1 : ${elvesPackages.maxOrNull()}")
    println("Part 2 : ${elvesPackages.sorted().takeLast(3).sum()}")
}
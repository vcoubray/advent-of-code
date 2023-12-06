package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day06")

    val (times, distances) = input.map{it.split("""\s+""".toRegex()).drop(1).map{n -> n.toInt()}}

    val races = times.zip(distances).map{(time, distance) -> time to distance}
    println("Part 1: ${races.map {(time, distance) -> (0..time).count{it * (time - it) > distance} }.fold(1){a,b -> a*b}}")

    val (time, distance) = input.map{it.filter{it.isDigit()}.toLong()}
    println("Part 2: ${(0..time).count{it * (time - it) > distance}}")
}
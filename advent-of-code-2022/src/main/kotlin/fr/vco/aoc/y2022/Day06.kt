package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day06").first()
    println("Part 1 : ${input.findStartMarker(4)}")
    println("Part 2 : ${input.findStartMarker(14)}")
}

fun String.findStartMarker(markerSize : Int) = windowed(markerSize).indexOfFirst{it.toSet().size == markerSize} + markerSize
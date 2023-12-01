package fr.vco.aoc.y2023

fun main() {

    val input = readLines("Day01")


    val numbers = List(10) {it.toString() to it.toString()}
    val spelledNumber = listOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    println("Part 1 : ${input.sumOf { it.toCalibration(numbers) }}")
    println("Part 2 : ${input.sumOf { it.toCalibration(numbers + spelledNumber) }}")
}

fun String.toCalibration(numbers : List<Pair<String,String>>): Int {
    val first = numbers.map{it to this.indexOf( it.first )}
        .filter{(_,index) -> index !=-1}
        .minBy{(_,id) -> id}
        .first.second
    val last = numbers.map{it to this.lastIndexOf( it.first )}
        .filter{(_,index) -> index !=-1}
        .maxBy{(_,id) -> id}
        .first.second
    return "${first}$last".toInt()
}

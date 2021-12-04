package fr.vco.aoc.y2021

const val HEIGHT = 5
const val WIDTH = 5

fun main() {
    val input = readLines("Day04")

    val numbers = input.first().split(",").map { it.toInt() }
    val grids = input.asSequence()
        .drop(1)
        .filterNot { it == "" }
        .chunked(5)
        .map(::Grid)
        .map { it.apply { playUntilWin(numbers) } }
        .toList()

    println("Part 1 : ${grids.minByOrNull { it.turn }?.score}")
    println("Part 2 : ${grids.maxByOrNull { it.turn }?.score}")
}

private class Grid(lines: List<String>) {
    val grid: List<Int> = lines.flatMap { line ->
        line.split(" ").filterNot { it == "" }.map { it.toInt() }
    }
    val marked = MutableList(HEIGHT * WIDTH) { false }
    var score: Int = 0
    var turn: Int = 0

    fun getScore(number: Int) = grid.filterIndexed { i, _ -> !marked[i] }.sum() * number

    fun playUntilWin(numbers: List<Int>) {
        for (number in numbers) {
            play(number)
            if (isValid()) {
                score = getScore(number)
                return
            }
            turn++
        }
    }

    fun play(number: Int) {
        val index = grid.indexOf(number)
        if (index != -1) marked[index] = true
    }

    fun isValid(): Boolean {
        for (y in 0 until HEIGHT) {
            if (marked.filterIndexed { i, _ -> i / WIDTH == y }.all { it }) return true
        }
        for (x in 0 until WIDTH) {
            if (marked.filterIndexed { i, _ -> i % WIDTH == x }.all { it }) return true
        }
        return false
    }
}
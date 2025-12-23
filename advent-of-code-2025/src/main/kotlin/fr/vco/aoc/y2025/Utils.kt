package fr.vco.aoc.y2025

import java.io.File


private fun String.getInputFile() = File("advent-of-code-2025/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()


fun Long.squared() = this * this


inline fun List<String>.split(predicate: (String) -> Boolean): List<List<String>> {
    val list = mutableListOf(mutableListOf<String>())
    this.forEach {
        if (predicate(it)) list.add(mutableListOf())
        else list.last().add(it)
    }
    return list
}
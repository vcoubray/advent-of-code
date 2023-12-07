package fr.vco.aoc.y2022

import java.io.File

private fun String.getInputFile() = File("advent-of-code-2022/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()


inline fun List<String>.split(predicate: (String) -> Boolean): List<List<String>> {
    val list = mutableListOf(mutableListOf<String>())
    this.forEach {
        if (predicate(it)) list.add(mutableListOf())
        else list.last().add(it)
    }
    return list
}

fun <T> List<List<T>>.transposed(): List<List<T>> {
    val transposedList = List<MutableList<T>>(this.first().size) { mutableListOf() }
    this.forEach { line ->
        line.forEachIndexed { i, it -> transposedList[i].add(it) }
    }
    return transposedList
}

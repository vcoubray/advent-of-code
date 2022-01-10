package fr.vco.aoc.y2021

import java.io.File

private fun String.getInputFile() = File("src/main/resources", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()
fun readFirstLine(fileName: String) = fileName.getInputFile().readLines().first()

fun List<String>.split(predicate: (String) -> Boolean): List<List<String>> {
    val list = mutableListOf(mutableListOf<String>())
    this.forEach {
        if (predicate(it)) list.add(mutableListOf())
        else list.last().add(it)
    }
    return list
}

fun List<String>.chunked(predicate: (String) -> Boolean): List<List<String>> {
    val list = mutableListOf(mutableListOf<String>())
    this.forEach {
        if (predicate(it)) list.add(mutableListOf(it))
        else list.last().add(it)
    }
    return list
}
package fr.vco.aoc.y2021

import java.io.File

private fun String.getInputFile() = File("src/main/resources", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()
fun readFirstLine(fileName: String) = fileName.getInputFile().readLines().first()

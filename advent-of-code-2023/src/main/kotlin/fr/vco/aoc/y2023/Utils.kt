package fr.vco.aoc.y2023

import java.io.File

private fun String.getInputFile() = File("advent-of-code-2023/src/main/resources", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()

package fr.vco.aoc.y2024

import java.io.File

private fun String.getInputFile() = File("advent-of-code-2024/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()


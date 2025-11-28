package fr.vco.aoc.y2025

import java.io.File


private fun String.getInputFile() = File("advent-of-code-2025/src/main/resources/inputs", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()
fun readText(fileName: String) = fileName.getInputFile().readText()

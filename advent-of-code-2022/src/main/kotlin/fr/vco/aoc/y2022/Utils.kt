package fr.vco.aoc.y2022

import java.io.File

private fun String.getInputFile() = File("advent-of-code-2022/src/main/resources", "$this.txt")
fun readLines(fileName: String) = fileName.getInputFile().readLines()
package fr.vco.adventofcode.y2019

import java.io.File
import java.io.InputStreamReader
import kotlin.math.pow

fun getInputReader(fileName: String): InputStreamReader =  fileName.getInputFile().inputStream().reader()
fun readLines(fileName: String) = fileName.getInputFile().readLines()
fun readText(fileName: String) =  fileName.getInputFile().readText()
private fun String.getInputFile() = File("advent-of-code-2019/src/main/resources/inputs", "$this.txt")




fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var list = this.toMutableList()
    var index = list.indexOfFirst(predicate)
    while (index != -1) {
        result.add(list.subList(0, index))
        list = list.drop(index + 1).toMutableList()
        index = list.indexOfFirst(predicate)
    }
    result.add(list)
    return result
}

data class Position(val x: Int, val y: Int) {
    operator fun plus(p: Position) = Position(x + p.x, y + p.y)
}

fun Int.pow(exp: Int) = this.toDouble().pow(exp.toDouble()).toInt()
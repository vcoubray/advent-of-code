package fr.vco.adventofcode.y2019

import java.io.InputStreamReader


fun readLines(name: String) = getInputReader(name).readLines()
fun readText(name: String) = getInputReader(name).readText()

fun getInputReader(fileName: String): InputStreamReader =
    InputStreamReader(object {}.javaClass.getResource("/inputs/${fileName}.txt").openStream())


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
package fr.vco.adventofcode.y2019

import java.io.InputStreamReader


fun readLines(name: String) = getInputReader("/input${name}.txt").readLines()
fun readText(name: String) = getInputReader("/input${name}.txt").readText()

fun getInputReader(filePath: String): InputStreamReader =
    InputStreamReader(object {}.javaClass.getResource(filePath).openStream())


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
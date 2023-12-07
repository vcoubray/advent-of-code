package fr.vco.adventofcode.y2020

import java.io.InputStreamReader

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

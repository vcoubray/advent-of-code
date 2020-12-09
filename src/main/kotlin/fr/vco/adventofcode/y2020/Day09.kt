package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay09.txt")
    val lines = input.readLines().map { it.toLong() }

    for (i in 25 until lines.size) {
        if (!lines[i].isValid(lines.subList(i - 25, i).sorted())) {
            println("Part 1 :${lines[i]}")
            break
        }
    }
}

fun Long.isValid(preamble: List<Long>): Boolean {
    if (this !in preamble[0] + preamble[1]..preamble.last() + preamble[preamble.size - 2]) return false
    preamble.forEachIndexed { i, it ->
        if (it > this) return false
        if (preamble.drop(i).any { n -> it + n == this }) return true
    }
    return false
}


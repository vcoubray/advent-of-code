package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

const val PREAMBLE_SIZE = 25

fun main() {
    val input = getInputReader("/2020/inputDay09.txt")
    val lines = input.readLines().map { it.toLong() }

    lines.drop(PREAMBLE_SIZE).forEachIndexed{i, it ->
        if (!it.isValid(lines.subList(i, i+PREAMBLE_SIZE))) {
            println("Part 1 : $it")
            val weakness = it.getWeakness(lines).sorted()
            if(weakness.isNotEmpty()) {
                println("Part 2 : ${weakness.first() + weakness.last()}")
            }
            return@forEachIndexed
        }
    }
}

fun Long.isValid(preamble: List<Long>): Boolean {
    preamble.forEachIndexed { i, it ->
        if (preamble.drop(i).any { n -> it + n == this }) return true
    }
    return false
}

fun Long.getWeakness(preamble: List<Long>): List<Long>{
    for(i in 0..preamble.size) {
        for(j in i..preamble.size) {
            val sum = preamble.subList(i,j).sum()
            if(sum == this) return preamble.subList(i,j)
            if(sum > this) break
        }
    }
    return emptyList()
}


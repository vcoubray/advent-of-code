package `2020`

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay06.txt")
    val lines = input.readLines()

    val groups = mutableListOf(mutableSetOf<Char>())

    lines.forEach{
        if(it.isBlank())groups.add(mutableSetOf())
        else groups.last().addAll(it.toCharArray().toList())
    }

    println("Part 1 : ${groups.sumBy{it.size}}")
}
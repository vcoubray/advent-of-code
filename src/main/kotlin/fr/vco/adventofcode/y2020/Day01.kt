package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader


fun main() {
    val input = getInputReader("/2020/inputDay01.txt")
    val expenses = input.readLines().map { it.toInt() }
    expenses.forEachIndexed { i, a ->
        expenses.subList(i, expenses.size).forEach { b ->
            if (a + b == 2020) println("Result : ${a * b}")
        }
    }
}


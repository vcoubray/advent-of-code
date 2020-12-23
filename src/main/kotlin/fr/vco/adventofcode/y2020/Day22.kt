package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader
import fr.vco.adventofcode.split
import java.util.*


fun main() {
    val input = getInputReader("/2020/inputDay22.txt")
    val (deck1, deck2) = input.readLines()
        .split { it.isBlank() }
        .map { it.drop(1).map { card -> card.toInt() } }
        .map { LinkedList(it) }

    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        val c1 = deck1.pop()
        val c2 = deck2.pop()

        if (c1 > c2) {
            deck1.addLast(c1)
            deck1.addLast(c2)
        } else {
            deck2.addLast(c2)
            deck2.addLast(c1)
        }
    }
    val winner = if (deck1.size > deck2.size) deck1 else deck2
    println("Part 1 : ${winner.foldIndexed(0) { i, acc, a -> a * (winner.size - i) + acc }}")
}

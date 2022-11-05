package fr.vco.adventofcode.y2020

import java.util.*

const val PLAYER_1 = 1
const val PLAYER_2 = 2

fun main() {
    val input = getInputReader("/2020/inputDay22.txt")
    val (deck1, deck2) = input.readLines()
        .split { it.isBlank() }
        .map { it.drop(1).map { card -> card.toInt() } }

    println("Part 1 : ${Game(deck1, deck2).apply { play(::getNormalWinner) }.score()}")
    println("Part 2 : ${Game(deck1, deck2).apply { play(::getRecursiveWinner) }.score()}")

}

fun getNormalWinner(c1: Int, c2: Int, deck1: List<Int> = emptyList(), deck2: List<Int> = emptyList()) =
    if (c1 > c2) PLAYER_1 else PLAYER_2

fun getRecursiveWinner(c1: Int, c2: Int, deck1: List<Int>, deck2: List<Int>): Int {
    return if (c1 <= deck1.size && c2 <= deck2.size)
        Game(LinkedList(deck1.take(c1)), LinkedList(deck2.take(c2))).play(::getRecursiveWinner)
    else getNormalWinner(c1, c2)
}


data class GameState(val deck1: List<Int>, val deck2: List<Int>)

class Game(deck1: List<Int>, deck2: List<Int>) {

    private val deck1 = LinkedList(deck1)
    private val deck2 = LinkedList(deck2)
    private val log = mutableListOf<GameState>()

    fun play(getWinner: (Int, Int, List<Int>, List<Int>) -> Int): Int {
        while (deck1.isNotEmpty() && deck2.isNotEmpty()) {

            val state = GameState(deck1.toList(), deck2.toList())
            if (log.contains(state)) return PLAYER_1
            log.add(state)

            val c1 = deck1.pop()
            val c2 = deck2.pop()

            val winner = getWinner(c1, c2, deck1, deck2)

            if (winner == PLAYER_1) {
                deck1.addLast(c1)
                deck1.addLast(c2)
            } else {
                deck2.addLast(c2)
                deck2.addLast(c1)
            }
        }
        return if (deck1.isNotEmpty()) PLAYER_1 else PLAYER_2
    }

    fun score(): Int {
        val winner = if (deck1.isNotEmpty()) deck1 else deck2
        return winner.foldIndexed(0) { i, acc, a -> a * (winner.size - i) + acc }
    }
}
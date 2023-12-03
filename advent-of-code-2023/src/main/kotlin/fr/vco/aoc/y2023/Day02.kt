package fr.vco.aoc.y2023

import kotlin.math.max

fun main() {
    val input = readLines("Day02")

    val games = input.map { it.toCubeGame() }
    val testHand = mapOf("red" to 12, "green" to 13, "blue" to 14)

    println("Part 1: ${games.filter { it.isPossibleWith(testHand)}.sumOf { it.id }}")
    println("Part 2: ${games.sumOf{it.minHand.power()}}")
}

typealias CubeHand = Map<String, Int>

class CubeGame(
    val id: Int,
    hands: List<CubeHand>,
) {
    val minHand = hands.reduce{ acc, hand -> acc.getMinHand(hand)}
    fun isPossibleWith(hand : CubeHand) = hand.all{ (color, count)-> minHand.getColor(color) <= count}
}

fun CubeHand.power() = values.fold(1){acc, a -> acc * a}
fun CubeHand.getColor(color: String) = this[color]?:0
fun CubeHand.getMinHand(hand: CubeHand) = (keys + hand.keys).toSet().associateWith { max(getColor(it), hand.getColor(it) )}

fun String.toCubeGame(): CubeGame {
    val (id, cubes) = """^Game (\d+): (.*)""".toRegex().find(this)!!.destructured
    val hands = cubes.split("; ").map { hand ->
        hand.split(", ").associate {
            val (count, color) = it.split(" ")
            color to count.toInt()
        }
    }
    return CubeGame(id.toInt(), hands)
}
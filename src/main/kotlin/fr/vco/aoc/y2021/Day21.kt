package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day21")

    val players = input.map {
        val (pos) = """.*: (\d+)""".toRegex().find(it)!!.destructured
        Player(pos.toInt())
    }

    println("Part 1 : ${players.game()}")
}

data class Player(var pos: Int, var score: Int = 0) {
    fun play(dice: DeterministicDice) {
        val rolls = dice.roll() + dice.roll() + dice.roll()
        pos = ((pos + rolls - 1) % 10) + 1
        score += pos
    }
}

class DeterministicDice {
    var value: Int = 0
    var rolls: Int = 0
    fun roll(): Int {
        value = if (value == 100) 1 else (value + 1)
        rolls++
        return value
    }
}

private fun List<Player>.game(): Int {
    var currentPlayer = 0
    val dice = DeterministicDice()

    while (this.none { it.score >= 1000 }) {
        this[currentPlayer].play(dice)
        currentPlayer = 1 - currentPlayer
    }
    return this.minOf { it.score } * dice.rolls
}
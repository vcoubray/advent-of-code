package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day21")

    val players = input.map {
        val (pos) = """.*: (\d+)""".toRegex().find(it)!!.destructured
        Player(pos.toInt())
    }

    println("Part 1 : ${players.playDeterministicGame()}")
    println("Part 2 : ${players.playQuantumGame().maxOrNull()}")
}

data class Player(var pos: Int, var score: Int = 0) {
    fun play(roll: Int) {
        pos = ((pos + roll - 1) % 10) + 1
        score += pos
    }
}

class DeterministicDie {
    var value: Int = 0
    var rolls: Int = 0
    private fun roll(): Int {
        value = if (value == 100) 1 else (value + 1)
        rolls++
        return value
    }

    fun roll(times: Int): Int = (0 until times).sumOf { roll() }
}

private fun List<Player>.playDeterministicGame(
    scoreMax: Int = 1000,
    die: DeterministicDie = DeterministicDie()
): Int {
    val players = this.map { it.copy() }
    var currentPlayer = 0

    while (players.none { it.score >= scoreMax }) {
        players[currentPlayer].play(die.roll(3))
        currentPlayer = 1 - currentPlayer
    }
    return players.minOf { it.score } * die.rolls
}

private fun <T> List<T>.combination(list: List<T>, operation: (T, T) -> T) =
    this.flatMap { a -> list.map { b -> operation(a, b) } }

private class QuantumDie(faces: Int = 3, rollsCount: Int = 3) {
    val rolls = List(rollsCount) { List(faces) { it + 1 } }
        .reduce { acc, rolls -> acc.combination(rolls) { a, b -> a + b } }
        .groupingBy { it }.eachCount()
}

private fun List<Player>.playQuantumGame(
    maxScore: Int = 21,
    die: QuantumDie = QuantumDie(),
    currentPlayer: Int = 0,
): List<Long> {
    val wins = mutableListOf(0L, 0L)
    die.rolls.map { (roll, occurrence) ->
        this.map { it.copy() }.also {
            it[currentPlayer].play(roll)
            if (it[currentPlayer].score >= maxScore) {
                wins[currentPlayer] += occurrence.toLong()
            } else {
                it.playQuantumGame(maxScore, die, 1 - currentPlayer).forEachIndexed { i, win ->
                    wins[i] += win * occurrence
                }
            }
        }
    }
    return wins
}
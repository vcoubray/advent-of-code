package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day02")
    println("Part 1 : ${input.sumBy { computeScore(it, ::scorePart1) }}")
    println("Part 2 : ${input.sumBy { computeScore(it, ::scorePart2) }}")
}

fun computeScore(round: String, score: (opp: String, me: String) -> Int): Int {
    val (opp, me) = round.split(" ")
    return score(opp, me)
}

enum class Hand(val point: Int, val win: Int, val lose: Int) {
    ROCK(1, 3, 2),
    PAPER(2, 1, 3),
    SCISSORS(3, 2, 1);
}

const val WIN = 6
const val DRAW = 3
const val LOSE = 0

val handMap = mapOf(
    "A" to Hand.ROCK,
    "B" to Hand.PAPER,
    "C" to Hand.SCISSORS,
    "X" to Hand.ROCK,
    "Y" to Hand.PAPER,
    "Z" to Hand.SCISSORS
)

fun scorePart1(opp: String, me: String): Int {
    val myHand = handMap[me]!!
    val oppHand = handMap[opp]!!
    return myHand.point + when (oppHand) {
        myHand -> DRAW
        Hand.ROCK -> if (myHand == Hand.PAPER) WIN else LOSE
        Hand.PAPER -> if (myHand == Hand.SCISSORS) WIN else LOSE
        Hand.SCISSORS -> if (myHand == Hand.ROCK) WIN else LOSE
    }
}

fun scorePart2(opp: String, me: String) = when (me) {
    "X" -> LOSE + handMap[opp]!!.win
    "Y" -> DRAW + handMap[opp]!!.point
    "Z" -> WIN + handMap[opp]!!.lose
    else -> 0
}




package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day07")

    val hands = input.map { it.split(" ") }
        .map { (hand, bid) -> CamelHand(hand, bid.toInt()) }
    println("Part 1 : ${hands.getTotalWinning( { it.type }, { CamelHand.CARDS_VALUE[it]!! })}")
    println("Part 2 : ${hands.getTotalWinning( { it.typeWithJoker }, { CamelHand.CARDS_VALUE_JOKER[it]!! }) }")
}

data class CamelHand(val hand: String, val bid: Int) {
    companion object {
        val CARDS_VALUE = "23456789TJQKA".mapIndexed { i, it -> it to i }.toMap()
        val CARDS_VALUE_JOKER = "J23456789TQKA".mapIndexed { i, it -> it to i }.toMap()
    }

    val type = hand.groupingBy { it }.eachCount().values.sortedDescending()
    val typeWithJoker: List<Int>

    init {
        val jokers = hand.count { it == 'J' }
        typeWithJoker = hand.filterNot { it == 'J' }
            .groupingBy { it }.eachCount()
            .values.sortedDescending().toMutableList()
            .takeIf { it.isNotEmpty() }?.apply { this[0] += jokers }
            ?: listOf(jokers)
    }
}

fun compareCamelHand(hand1: CamelHand, hand2: CamelHand, handType: (CamelHand) -> List<Int>, cardStrength: (Char) -> Int): Int {
    val typesDiff = handType(hand1).zip(handType(hand2)).map { (a, b) -> a.compareTo(b) }.firstOrNull { it != 0 } ?: 0
    if (typesDiff != 0) return typesDiff
    return hand1.hand.zip(hand2.hand)
        .map { (a, b) -> cardStrength(a).compareTo(cardStrength(b)) }
        .first { it != 0 }
}


fun List<CamelHand>.getTotalWinning(handType: (CamelHand) -> List<Int>, cardStrength: (Char) -> Int): Long {
    return sortedWith { h1, h2 -> compareCamelHand(h1, h2, handType, cardStrength) }
        .foldIndexed(0L) { i, acc, hand -> acc + ((i + 1) * hand.bid) }
}
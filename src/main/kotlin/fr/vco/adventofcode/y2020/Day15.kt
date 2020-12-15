package fr.vco.adventofcode.y2020

import kotlin.system.measureTimeMillis


fun main() {
    val firstNumbers = listOf(0, 20, 7, 16, 1, 18, 15)

    measureTimeMillis {
        println("Part 1 : ${firstNumbers.getSpokenNumber(2020)}")
    }.let { println("in ${it}ms") }
    measureTimeMillis {
        println("Part 2 : ${firstNumbers.getSpokenNumber(30000000)}")
    }.let { println("in ${it}ms") }
}

fun List<Int>.getSpokenNumber(turn: Int): Int {

    val spokenNumbers = this.mapIndexed { i, it -> it to SpokenNumber(i,0) }.toMap().toMutableMap()
    var lastNumber = this.last()

    for (i in spokenNumbers.size until turn) {
        val currentNumber = spokenNumbers[lastNumber]!!.age
        val lastTurn = spokenNumbers.getOrDefault( currentNumber,SpokenNumber(i,0)).lastTurn
        spokenNumbers[currentNumber] = SpokenNumber(i, i-lastTurn )
        lastNumber = currentNumber
    }
    return lastNumber
}

class SpokenNumber(val lastTurn: Int, val age: Int)
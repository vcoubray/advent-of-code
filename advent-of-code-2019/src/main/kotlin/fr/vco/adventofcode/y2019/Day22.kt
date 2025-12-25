package fr.vco.adventofcode.y2019

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO

fun main() {
    val input = readLines("Day22")

    println("Part 1: ${input.toShuffles(10_007).reduced().shuffle(2019)}")
    println("Part 2: ${input.toShuffles(119_315_717_514_047).reduced().repeat(101_741_582_076_661).shuffleInv(2020)}")
}

open class Shuffle(val a: BigInteger = ONE, val b: BigInteger = ZERO, val deckSize: BigInteger) {

    class Stack(deckSize: BigInteger) : Shuffle(a = -ONE, b = -ONE, deckSize)
    class Cut(value: BigInteger, deckSize: BigInteger) : Shuffle(a = ONE, b = -value, deckSize = deckSize)
    class Incremental(value: BigInteger, deckSize: BigInteger) : Shuffle(a = value, b = ZERO, deckSize = deckSize)

    fun shuffle(card: Long): BigInteger = (a * card.toBigInteger() + b).mod(deckSize)
    fun shuffleInv(card: Long): BigInteger = ((card.toBigInteger() - b) * a.modInverse(deckSize)).mod(deckSize)

    operator fun plus(shuffle: Shuffle) = Shuffle(
        a = (a * shuffle.a).mod(deckSize),
        b = (b * shuffle.a + shuffle.b).mod(deckSize),
        deckSize
    )

    fun repeat(repetitions: Long): Shuffle {
        val aPowN = powMod(a, repetitions.toBigInteger(), deckSize)
        return Shuffle(
            a = aPowN,
            b = (b * (aPowN - ONE) * (a - ONE).modInverse(deckSize)).mod(deckSize),
            deckSize
        )
    }
}

val INSTRUCTION_REGEX = """([^-\d+]+)(-?\d+)?$""".toRegex()
fun List<String>.toShuffles(deckSize: Long) = this.map {
    val (instruction, value) = INSTRUCTION_REGEX.find(it)!!.destructured
    when (instruction.trim()) {
        "cut" -> Shuffle.Cut(value.toBigInteger(), deckSize.toBigInteger())
        "deal with increment" -> Shuffle.Incremental(value.toBigInteger(), deckSize.toBigInteger())
        else -> Shuffle.Stack(deckSize.toBigInteger())
    }
}

fun List<Shuffle>.reduced() = this.reduce { a, b -> a + b }

fun powMod(n: BigInteger, exponent: BigInteger, mod: BigInteger): BigInteger {
    return when {
        exponent == ONE -> n
        exponent and ONE == ZERO -> powMod((n * n).mod(mod), exponent / TWO, mod)
        else -> (n * powMod((n * n).mod(mod), (exponent - ONE) / TWO, mod)).mod(mod)
    }
}

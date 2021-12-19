package fr.vco.aoc.y2021

import kotlin.math.ceil

val SPLIT_REGEX = """\d{2,}""".toRegex()

fun main() {
    val input = readLines("Day18")

    val sum = input.reduce{ acc, a -> acc.addNumber(a)}
    println("Part 1 : ${sum.toSnailFishNumber().magnitude()}")

}

fun String.addNumber(string: String) = "[$this,$string]".reduce()
fun String.splitIndex() = SPLIT_REGEX.find(this)
fun String.split(range: IntRange) =
    take(range.first) + substring(range).toInt().let { "[${it / 2},${ceil(it / 2.0).toInt()}]" } + drop(range.last + 1)

fun String.explodeIndex(): IntRange? {
    var counter = 0
    this.forEachIndexed { i, it ->
        when (it) {
            '[' -> counter++
            ']' -> counter--
        }
        if (counter > 4) return """\[\d+,\d+]""".toRegex().find(this, i )?.range
    }
    return null
}

fun String.explode(range: IntRange): String {
    val (left, right) = this.substring(range.first+1, range.last).split(",").map { it.toInt() }
    val leftStr = this.take(range.first).addLeft(left)
    val rightStr = this.drop(range.last +1 ).addRight(right)
    return "${leftStr}0$rightStr"
}

fun String.addRight(value: Int): String {
    return """\d+""".toRegex().find(this)?.range
        ?.let { range -> take(range.first) + substring(range).toInt().let { n -> "${n + value}" } + drop(range.last + 1) }
        ?: this
}

fun String.addLeft(value: Int): String {
    return """\d+""".toRegex().findAll(this).lastOrNull()?.range
        ?.let { range -> take(range.first) + substring(range).toInt().let { n -> "${n + value}" } + drop(range.last + 1) }
        ?: this
}

fun String.reduce() : String {
    explodeIndex()?.let{return explode(it).reduce()}
    splitIndex()?.let{return split(it.range).reduce()}
    return this
}

fun String.toSnailFishNumber(): SnailFishNumber {
    if (!this.startsWith('[')) return SnailFishNumber.Number(this.toInt())
    val s = this.drop(1).dropLast(1)
    val sepId = s.findSeparatorIndex()
    return SnailFishNumber.Pair(s.take(sepId).toSnailFishNumber(), s.drop(sepId + 1).toSnailFishNumber())
}

fun String.findSeparatorIndex(): Int {
    var counter = 0
    this.forEachIndexed { i, it ->
        when {
            it == '[' -> counter++
            it == ']' -> counter--
            it == ',' && counter == 0 -> return i
        }
    }
    return -1
}

abstract class SnailFishNumber {

    abstract fun magnitude(): Int

    class Number(val value: Int) : SnailFishNumber() {
        override fun magnitude() = value
        override fun toString() = "$value"
    }

    class Pair(var left: SnailFishNumber, var right: SnailFishNumber) : SnailFishNumber() {
        override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
        override fun toString() = "[$left,$right]"
    }
}
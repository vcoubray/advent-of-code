package fr.vco.aoc.y2021

import kotlin.math.ceil

val SPLIT_REGEX = """\d{2,}""".toRegex()
val INTEGER_REGEX = """\d+""".toRegex()
val PAIR_REGEX = """\[\d+,\d+]""".toRegex()

fun main() {
    val input = readLines("Day18")

    val sum = input.reduce { acc, a -> acc.addSnailFishNumber(a) }
    println("Part 1 : ${sum.toSnailFishTree().magnitude()}")

    val maxMagnitude = input.maxOf { n ->
        input.filter { n != it }.maxOf { n.addSnailFishNumber(it).toSnailFishTree().magnitude() }
    }
    println("Part 2 : $maxMagnitude")
}

fun String.replace(range: IntRange, transform: (String) -> String) =
    take(range.first) + transform(substring(range)) + drop(range.last + 1)

fun String.addSnailFishNumber(string: String) = "[$this,$string]".reduce()
fun String.getSplitRange() = SPLIT_REGEX.find(this)
fun String.split(range: IntRange) = this.replace(range) { it.toInt().let { n -> "[${n / 2},${ceil(n / 2.0).toInt()}]" } }

fun String.getExplodeRange(): IntRange? {
    var counter = 0
    this.forEachIndexed { i, it ->
        when (it) {
            '[' -> counter++
            ']' -> counter--
        }
        if (counter > 4) return PAIR_REGEX.find(this, i)?.range
    }
    return null
}

fun String.explode(range: IntRange): String {
    val (left, right) = this.substring(range.first + 1, range.last).split(",").map { it.toInt() }
    val leftStr = this.take(range.first).addLeft(left)
    val rightStr = this.drop(range.last + 1).addRight(right)
    return "${leftStr}0$rightStr"
}

fun String.addRight(value: Int) = INTEGER_REGEX.find(this)?.range
    ?.let { range -> this.replace(range) { it.toInt().let { n -> "${n + value}" } } }
    ?: this

fun String.addLeft(value: Int) = INTEGER_REGEX.findAll(this).lastOrNull()?.range
    ?.let { range -> this.replace(range) { it.toInt().let { n -> "${n + value}" } } }
    ?: this


fun String.reduce(): String {
    getExplodeRange()?.let { return explode(it).reduce() }
    getSplitRange()?.let { return split(it.range).reduce() }
    return this
}

fun String.toSnailFishTree(): SnailFishTree {
    if (!this.startsWith('[')) return SnailFishTree.Number(this.toInt())
    val s = this.drop(1).dropLast(1)
    val sepId = s.findSeparatorIndex()
    return SnailFishTree.Pair(s.take(sepId).toSnailFishTree(), s.drop(sepId + 1).toSnailFishTree())
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

abstract class SnailFishTree {
    abstract fun magnitude(): Int

    class Number(val value: Int) : SnailFishTree() {
        override fun magnitude() = value
        override fun toString() = "$value"
    }

    class Pair(var left: SnailFishTree, var right: SnailFishTree) : SnailFishTree() {
        override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
        override fun toString() = "[$left,$right]"
    }
}
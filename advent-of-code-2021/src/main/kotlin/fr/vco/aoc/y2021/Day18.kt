package fr.vco.aoc.y2021

import kotlin.math.ceil

val SPLIT_REGEX = """\d{2,}""".toRegex()
val INTEGER_REGEX = """\d+""".toRegex()
val PAIR_REGEX = """\[\d+,\d+]""".toRegex()

fun main() {
    val input = readLines("Day18")

    println("Part 1 : ${input.reduce { acc, a -> acc.addSnailFishNumber(a) }.magnitude()}")
    println("Part 2 : ${input.maxOf { n -> input.filterNot { it == n }.maxOf { n.addSnailFishNumber(it).magnitude() } }}")
}

private fun String.replace(range: IntRange, transform: (String) -> String) =
    take(range.first) + transform(substring(range)) + drop(range.last + 1)

private fun String.addSnailFishNumber(string: String) = "[$this,$string]".reduce()
private fun String.getSplitRange() = SPLIT_REGEX.find(this)
private fun String.splitNumber(range: IntRange) =
    this.replace(range) { it.toInt().let { n -> "[${n / 2},${ceil(n / 2.0).toInt()}]" } }

private fun String.getExplodeRange(): IntRange? {
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

private fun String.explode(range: IntRange): String {
    val (left, right) = this.substring(range.first + 1, range.last).split(",").map { it.toInt() }
    val leftStr = this.take(range.first).addLeft(left)
    val rightStr = this.drop(range.last + 1).addRight(right)
    return "${leftStr}0$rightStr"
}

private fun String.addRight(value: Int) = INTEGER_REGEX.find(this)?.range
    ?.let { range -> this.replace(range) { it.toInt().let { n -> "${n + value}" } } }
    ?: this

private fun String.addLeft(value: Int) = INTEGER_REGEX.findAll(this).lastOrNull()?.range
    ?.let { range -> this.replace(range) { it.toInt().let { n -> "${n + value}" } } }
    ?: this


private fun String.reduce(): String {
    getExplodeRange()?.let { return explode(it).reduce() }
    getSplitRange()?.let { return splitNumber(it.range).reduce() }
    return this
}

private fun String.magnitude(): Int {
    if (!this.startsWith('[')) return this.toInt()
    val pair = this.drop(1).dropLast(1)
    val sepId = pair.findSeparatorIndex()
    return 3 * pair.take(sepId).magnitude() + 2 * pair.drop(sepId + 1).magnitude()
}

private fun String.findSeparatorIndex(): Int {
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

package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day14")

    val template = Template(input.first())
    val rules = input.drop(2)
        .map { it.split(" -> ") }
        .map { (pair, insert) -> Rule(pair, insert) }
        .associateBy { it.pair }

    println("Part 1 : ${template.applyRules(rules, 10).result()}")
    println("Part 2 : ${template.applyRules(rules, 40).result()}")
}

private data class Rule(val pair: String, val insert: String) {
    val result = listOf(
        pair.first() + insert,
        insert + pair.last()
    )
}

private data class Template(val pairs: Map<String, Long>, val lastChar: Char) {
    constructor(template: String) : this(template.toPairs(), template.last())

    fun applyRules(rules: Map<String, Rule>) = copy(pairs = this.pairs
        .flatMap { (pair, count) -> rules[pair]!!.result.map { it to count } }
        .groupingBy { it.first }.fold(0L) { count, pair -> count + pair.second }
    )

    fun applyRules(rules: Map<String, Rule>, times: Int) =
        (1..times).fold(this) { pairs, _ -> pairs.applyRules(rules) }

    fun eachCharCount(): Map<Char, Long> = this.pairs
        .map { (pair, count) -> pair.first() to count }
        .groupingBy { it.first }.fold(0L) { count, pair -> count + pair.second }
        .toMutableMap().apply { this.merge(lastChar, 1L, Long::plus) }

    fun result() = eachCharCount().values.run { maxOrNull()!! - minOrNull()!! }
}

private fun String.toPairs() = List(this.length - 1) { "${this[it]}${this[it + 1]}" }
    .groupBy { it }
    .map { (c, list) -> c to list.size.toLong() }
    .toMap()

package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay19.txt")
    val lines = input.readLines()

    val (rulesExp, messages) = lines.filterNot { it.isBlank() }.partition { "(\\d)+:.*".toRegex().matches(it) }
    val rules = rulesExp.map { it.split(": ") }.map { (id, rule) -> id.toInt() to Rule(rule) }.toMap()

    println("Part 1 :${messages.count(rules.getRegex(0).toRegex()::matches)}")

    rules[8]?.exp = List(10){List(it+1){42}.joinToString(" ")}.joinToString(" | ")
    rules[11]?.exp = List(10){(List(it+1){42}+List(it+1){31}).joinToString(" ")}.joinToString(" | ")
    println("Part 2 :${messages.count(rules.getRegex(0).toRegex()::matches)}")

}

fun Map<Int, Rule>.getRegex(id: Int): String {
    return this[id]?.let { rule->
        if ("\"\\w\"".toRegex().matches(rule.exp)) rule.exp[1].toString()
        else {
            rule.exp.split(" | ")
                .map { ids -> ids.split(" ").map { it.toInt() } }
                .joinToString(separator = "|", prefix = "(", postfix = ")") { ids ->
                   ids.joinToString("") { id -> this.getRegex(id) }
                }
        }
    } ?: ""
}

data class Rule(var exp: String)

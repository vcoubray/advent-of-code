package fr.vco.aoc.y2023

import java.lang.Exception

fun main() {
    val input = readLines("Day19")
    val (workflowsInput, parts) = input.split { it.isBlank() }
    val workflows = workflowsInput.map { it.toWorkFlow() }.toMap()

    println("Part 1: ${parts.map { it.toPart() }.filter(workflows::process).sumOf{it.score}}")
}


data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    val score = x + m + a + s
}

data class Rule(val dest: String, val value: Int, val partValue: (part: Part) -> Int, val predicate: (Int, Int) -> Boolean) {
    fun match(part: Part) = predicate(partValue(part), value)
}
data class Workflow(val rules: List<Rule>, val default: String) {
    fun process (part: Part) =rules.firstOrNull { it.match(part) }?.dest ?: default
}

fun Map<String, Workflow>.process(part: Part): Boolean {
    var workflow = "in"
    while (workflow != "A" && workflow != "R") {
        workflow = this[workflow]!!.process(part)
    }
    return workflow == "A"
}

/** Parser **/
fun String.toPart(): Part {
    val (x, m, a, s) = """^\{x=(\d*),m=(\d*),a=(\d*),s=(\d*)}$""".toRegex().find(this)!!.destructured.toList().map { it.toInt() }
    return Part(x, m, a, s)
}

fun String.toWorkFlow(): Pair<String, Workflow> {
    val (name, rules, default) = """(^.*)\{(.*),([^,]*)}$""".toRegex().find(this)!!.destructured
    return name to Workflow(rules.split(",").map { it.toRule() }, default)
}

fun isSup(a: Int, b: Int) = a > b
fun isInf(a: Int, b: Int) = a < b
fun String.toRule(): Rule {
    val (field, op, value, dest) = """(^[xmas])([<>])(\d*):(.*$)""".toRegex().find(this)!!.destructured
    val operation = if (op == ">") ::isSup else ::isInf
    val getPartValue = when (field) {
        "x" -> { p: Part -> p.x }
        "m" -> { p: Part -> p.m }
        "a" -> { p: Part -> p.a }
        "s" -> { p: Part -> p.s }
        else -> throw Exception("Should not happen")
    }
    return Rule(dest, value.toInt(), getPartValue, operation)
}






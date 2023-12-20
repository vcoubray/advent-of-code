package fr.vco.aoc.y2023

import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readLines("Day19")
    val (workflowsInput, parts) = input.split { it.isBlank() }
    val workflows = workflowsInput.associate { it.toWorkFlow() }

    println("Part 1: ${parts.map { it.toPart() }.filter(workflows::process).sumOf { it.score }}")
    println("Part 2: ${workflows.countCombination()}")
}

data class Part(val parts: Map<Char,Int>) {
    val score = parts.values.sum()
}

data class RangePart(val nextWorkflow: String, val ranges: Map<Char, IntRange>) {
    constructor(nextWorkflow: String, range: IntRange) : this(nextWorkflow, "xmas".associateWith { range })

    fun getCombinationsCount() = ranges.values.fold(1L) { acc, a -> acc * a.count().toLong() }
}

data class Rule(
    val dest: String,
    val value: Int,
    val op: Boolean,
    val field: Char,
) {
    val predicate = if (op) ::isSup else ::isInf

    private fun isSup(a: Int, b: Int) = a > b
    private fun isInf(a: Int, b: Int) = a < b

    fun match(part: Part) = predicate(part.parts[field]!!, value)

    fun dispatch(part: RangePart): Pair<RangePart?, RangePart?> {
        val range = part.ranges[field]!!
        val (keep, rem) = range.cut(value, op)
        return keep?.let { RangePart(dest, part.ranges.toMutableMap().apply { this[field] = keep }) } to
                rem?.let {
                    RangePart(
                        part.nextWorkflow,
                        part.ranges.toMutableMap().apply { this[field] = rem })
                }
    }

    private fun IntRange.cut(value: Int, isSup: Boolean): Pair<IntRange?, IntRange?> {
        if (isSup) {
            val keep = max(first, value + 1)..max(last, value + 1)
            val rem = min(first, value)..min(last, value)
            return keep.takeIf { !it.isEmpty() } to rem.takeIf { !it.isEmpty() }
        } else {
            val keep = min(value - 1, first)..min(value - 1, last)
            val rem = max(value, first)..max(value, last)
            return keep.takeIf { !it.isEmpty() } to rem.takeIf { !it.isEmpty() }
        }
    }
}

data class Workflow(val rules: List<Rule>, val default: String) {
    fun process(part: Part) = rules.firstOrNull { it.match(part) }?.dest ?: default

    fun process(part: RangePart): List<RangePart> {
        val result = mutableListOf<RangePart>()

        var current: RangePart? = part
        for (rule in rules) {
            if (current == null) break
            val (dispatched, rem) = rule.dispatch(current)
            dispatched?.let(result::add)
            current = rem
        }
        current?.let { result.add(RangePart(default, current.ranges)) }
        return result
    }
}


fun Map<String, Workflow>.process(part: Part): Boolean {
    var workflow = "in"
    while (workflow != "A" && workflow != "R") {
        workflow = this[workflow]!!.process(part)
    }
    return workflow == "A"
}

fun Map<String, Workflow>.countCombination(range: IntRange = (1..4000)): Long {
    val toVisit = ArrayDeque<RangePart>().apply { add(RangePart("in", range)) }
    var combinations = 0L

    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        if (current.nextWorkflow == "A") {
            combinations += current.getCombinationsCount()
        } else if (current.nextWorkflow != "R") {
            this[current.nextWorkflow]!!.process(current).forEach(toVisit::addLast)

        }
    }
    return combinations
}


/** Parser **/
fun String.toPart(): Part {
    val (x, m, a, s) = """^\{x=(\d*),m=(\d*),a=(\d*),s=(\d*)}$""".toRegex().find(this)!!.destructured.toList()
        .map { it.toInt() }
    return Part(mapOf('x' to x, 'm' to m, 'a' to a, 's' to s))
}

fun String.toWorkFlow(): Pair<String, Workflow> {
    val (name, rules, default) = """(^.*)\{(.*),([^,]*)}$""".toRegex().find(this)!!.destructured
    return name to Workflow(rules.split(",").map { it.toRule() }, default)
}

fun String.toRule(): Rule {
    val (field, op, value, dest) = """(^[xmas])([<>])(\d*):(.*$)""".toRegex().find(this)!!.destructured
    return Rule(dest, value.toInt(), op == ">", field.first())
}

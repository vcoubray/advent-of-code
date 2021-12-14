package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day14")

    val template = input.first()
    val rules = input.drop(2)
        .map { it.split(" -> ") }
        .associate { (pair, insert) -> pair to insert }

    println("Part 1 : ${template.applyRules(rules,10).maxMinusMin()}")

}
private fun String.applyRules(rules: Map<String,String>, times : Int ) = (1..times).fold(this){template, _ -> template.applyRules(rules)}

private fun String.applyRules(rules: Map<String, String>): String {
    val sb = StringBuilder()
    for (i in 0..this.length - 2) {
        rules[this.substring(i, i + 2)]?.let { sb.append(it) }
    }
    return this.zip(sb.toString()).fold(""){acc, (a,b)-> "$acc$a$b" } + this.last()
}

private fun String.maxMinusMin() = this.groupBy{it}.values.let{ g -> g.maxOf { it.size } - g.minOf{it.size}}


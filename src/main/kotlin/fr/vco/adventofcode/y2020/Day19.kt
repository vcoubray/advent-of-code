package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay19.txt")
    val lines = input.readLines()

    val(rules, messages) = lines.filterNot{it.isBlank()}.partition{"(\\d)+:.*".toRegex().matches(it)}
    val rulesMap = rules.map { it.split(": ") }.map { (id,rule) -> id.toInt() to Rule(rule) }.toMap()

    println("Part 1 :${messages.count(rulesMap.getRegex(0).toRegex()::matches)}")

}

fun Map<Int,Rule>.getRegex(id: Int): String{
    return this[id]?.let{
        if(it.regex == null) {
            it.regex = it.tree.joinToString(separator="|", prefix = "(", postfix = ")"){
                    ids-> ids.joinToString (""){ id -> this.getRegex(id) }
            }
        }
        it.regex
    }?:""
}


class Rule(rule: String) {

    lateinit var tree : List<List<Int>>
    var regex : String? = null
    init{
        if ("\"\\w\"".toRegex().matches(rule)) regex = rule[1].toString()
        else tree = rule.split(" | ").map{it.split(" ").map{id  -> id.toInt()}}
    }

}
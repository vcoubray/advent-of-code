package fr.vco.adventofcode.y2020

import java.util.*


fun main() {
    val input = getInputReader("/inputDay07.txt")
    val lines = input.readLines()

    val bags = mutableMapOf<String, Bag>()

    lines.forEach {
        val (parentColor, childrenColors) = it.split(" bags contain ")
        val children = childrenColors.dropLast(1).split(", ").map(::extractColor)

        children.filterNotNull()
            .forEach { (n, c) ->
                val parent = bags.getBag(parentColor)
                val child = bags.getBag(c)
                parent.children[child] = n
                child.parents.add(parent)
            }
    }

    println("Part 1 : ${bags.getParentCount("shiny gold")}")
    println("Part 2 : ${bags["shiny gold"]!!.getChildrenCount()}")

}


fun MutableMap<String, Bag>.getBag(color: String): Bag {
    if (!this.containsKey(color)) {
        this[color] = Bag(color)
    }
    return this[color]!!
}


fun MutableMap<String, Bag>.getParentCount(color: String): Int {
    val visited = mutableSetOf<String>()
    val toVisit = LinkedList<String>()
    toVisit.add(color)
    while (toVisit.isNotEmpty()) {
        val current = toVisit.pop()
        if (!visited.contains(current)) {
            visited.add(current)
            this[current]?.parents?.forEach {
                toVisit.add(it.color)
            }
        }
    }
    return visited.size - 1
}

fun extractColor(s: String) = "(\\d+) (.*) bags?".toRegex().find(s)?.groupValues?.let { (_, n, s) -> n.toInt() to s }

class Bag(
    val color: String,
    val parents: MutableList<Bag> = mutableListOf(),
    val children: MutableMap<Bag, Int> = mutableMapOf()
) {
    fun getChildrenCount(): Int = children.toList().sumBy { (b, n) -> n * (1 + b.getChildrenCount()) }
}
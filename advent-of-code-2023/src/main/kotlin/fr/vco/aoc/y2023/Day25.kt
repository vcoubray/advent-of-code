package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day25")
    val nodes = input.toNodes()

    println("Part 1: ${nodes.findMinCut(3)}")
}

data class ComponentNode(val name: String, val size: Int = 1, val children: MutableList<String> = mutableListOf()) {
    fun add(child: String) = children.add(child)

    fun contractWith(node: ComponentNode) = ComponentNode(
        name,
        size + node.size,
        (children + node.children).filterNot { it == node.name || it == name }.toMutableList()
    )

    fun replaceChild(old: String, new: String) = ComponentNode(
        name,
        size,
        children.toMutableList().also { it.replaceAll { s -> if (s == old) new else s } }.toMutableList()
    )
}


fun Map<String, ComponentNode>.findMinCut(cutSize: Int): Int {

    do {
        var nodes = this
        while (nodes.size > 2) {
            nodes = nodes.contractRandom()
        }
        val (a, b) = nodes.values.toList()
        if (a.children.size == cutSize) return a.size * b.size
    } while (true)
}

fun Map<String, ComponentNode>.contractRandom(): Map<String, ComponentNode> {
    val parent = this.keys.random()
    val child = this[parent]!!.children.random()

    val result = this.toMutableMap()
    result.remove(child)
    result[parent] = this[parent]!!.contractWith(this[child]!!)
    this[child]!!.children.toSet().filterNot { it == parent }.forEach { node ->
        result[node] = result[node]!!.replaceChild(child, parent)
    }

    return result
}

fun List<String>.toNodes(): Map<String, ComponentNode> {
    val nodes = mutableMapOf<String, ComponentNode>()
    forEach {
        val (name, children) = """^(.+): (.+)$""".toRegex().find(it)!!.destructured
        children.split(" ").forEach { child ->
            nodes.computeIfAbsent(name) { ComponentNode(name) }.also { it.add(child) }
            nodes.computeIfAbsent(child) { ComponentNode(child) }.also { it.add(name) }
        }
    }
    return nodes
}


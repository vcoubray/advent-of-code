package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day22")
    val sandBricks = input.map { it.toSandBrick() }.toFinalPosition().toTree()

    println("Part 1: ${sandBricks.countCanBeDisintegrated()}")
    println("Part 2: ${sandBricks.countFallenBricks()}")
}

data class SandBrick(val x: IntRange, val y: IntRange, val z: IntRange)

fun String.toSandBrick(): SandBrick {
    val (start, end) = this.split("~").map { it.split(",").map { n -> n.toInt() } }
    val (x, y, z) = start.zip(end).map { (s, e) -> s..e }
    return SandBrick(x, y, z)
}

fun List<SandBrick>.toFinalPosition(): List<SandBrick> {
    val finalBricks = mutableListOf<SandBrick>()
    this.sortedBy { it.z.first }
        .forEach { brick ->
            val finalBrick = finalBricks.lastOrNull { it.x.intersect(brick.x).isNotEmpty() && it.y.intersect(brick.y).isNotEmpty() }
                ?.let { brick.copy(z = (it.z.last + 1)..(it.z.last + 1 + brick.z.size())) }
                ?: brick.copy(z = 1..1 + brick.z.size())
            finalBricks.add(finalBrick)
            finalBricks.sortBy { it.z.last }
        }
    return finalBricks
}

data class BrickNode(
    val id: Int,
    val brick: SandBrick,
    val children: MutableList<BrickNode> = mutableListOf(),
    val parents: MutableList<BrickNode> = mutableListOf()
) {
    override fun toString() = "[id: $id, brick: $brick, children: ${children.map { it.id }}, parents: ${parents.map { it.id }}"
}

fun List<SandBrick>.toTree(): List<BrickNode> {
    val nodes = this.mapIndexed { i, sandBrick -> BrickNode(i, sandBrick) }
    val groupedBricks = nodes.groupBy { it.brick.z.last }

    for (node in nodes) {
        (groupedBricks[node.brick.z.first - 1] ?: emptyList())
            .filter {
                node.brick.x.intersect(it.brick.x).isNotEmpty() &&
                    node.brick.y.intersect(it.brick.y).isNotEmpty()
            }
            .forEach { parent ->
                parent.children.add(node)
                node.parents.add(parent)
            }
    }
    return nodes
}

fun List<BrickNode>.countCanBeDisintegrated(): Int {
    return this.count { it.children.none { child -> child.parents.size <= 1 } }
}

fun List<BrickNode>.countFallenBricks(): Int {

    var totalFallen = 0
    for (node in this) {
        val toRemove = ArrayDeque<BrickNode>().apply { addLast(node) }
        val removed = MutableList(size) { false }
        removed[node.id] = true

        while (toRemove.isNotEmpty()) {
            val current = toRemove.removeFirst()
            current.children.filter { !removed[it.id] }
                .forEach { child ->
                    if (child.parents.all { p -> removed[p.id] }) {
                        toRemove.add(child)
                        removed[child.id] = true
                        totalFallen++
                    }
                }

        }


    }
    return totalFallen
}
package fr.vco.aoc.y2022

const val TOTAL_SIZE = 70_000_000
const val REQUIRED_SIZE = 30_000_000

fun main() {
    val input = readLines("Day07")

    val root = NodeFile("/")
    val files = mutableListOf(root)

    var currentNode = root
    input.forEach { line ->
        if (line.startsWith("$ cd")) {
            val dest = line.split(" ").last()
            currentNode = when(dest) {
                ".." -> currentNode.parent!!
                "/" -> root
                else -> currentNode.children.first{it.name == dest}
            }
        } else if (!line.startsWith("$")) {
            val (data, name) = line.split(" ")
            val file = if (data == "dir") NodeFile(name, currentNode, true)
            else NodeFile(name, currentNode, false, data.toLong())
            currentNode.children.add(file)
            files.add(file)
        }
    }
    root.computeDirSize()

    val directories = files.filter{it.isDirectory}
    println("Part 1 : ${directories.filter{it.size <= 100_000}.sumOf{it.size}}")

    val spaceToFree = REQUIRED_SIZE - (TOTAL_SIZE - root.size)
    println("Part 2 : ${directories.filter{it.size >= spaceToFree}.minOf{it.size}}")

}

class NodeFile(
    val name: String,
    val parent: NodeFile? = null,
    val isDirectory: Boolean = true,
    var size: Long = 0,
    val children: MutableList<NodeFile> = mutableListOf()
) {
    fun print(indent : Int = 0) {
        val prefix = "".padEnd(indent*4, ' ')
        println("$prefix - $name (${"dir".takeIf{isDirectory}?:"file"}) - size : $size")
        children.forEach{it.print(indent+1)}
    }

    fun computeDirSize() : Long {
        if (isDirectory) {
            size = children.sumOf { it.computeDirSize() }
        }
        return size
    }
}

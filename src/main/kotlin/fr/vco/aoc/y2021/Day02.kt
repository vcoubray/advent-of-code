package fr.vco.aoc.y2021

fun main() {
    val lines = readLines("Day02")

    println("Part 1 : ${lines.fold(Submarine()) { submarine, line -> submarine.move(line) }.result()}")
    println("Part 2 : ${lines.fold(Submarine()) { submarine, line -> submarine.moveWithAim(line) }.result()}")
}

private data class Submarine(
    val depth: Int = 0,
    val position: Int = 0,
    val aim: Int = 0
) {

    fun move(instruction: String): Submarine {
        val (direction, distance) = instruction.split(" ")
        return when (direction) {
            "forward" -> copy(position = position + distance.toInt())
            "up" -> copy(depth = depth - distance.toInt())
            "down" -> copy(depth = depth + distance.toInt())
            else -> this
        }
    }

    fun moveWithAim(instruction: String): Submarine {
        val (direction, distance) = instruction.split(" ")
        return when (direction) {
            "forward" -> copy(
                position = position + distance.toInt(),
                depth = depth + distance.toInt() * aim
            )
            "up" -> copy(aim = aim - distance.toInt())
            "down" -> copy(aim = aim + distance.toInt())
            else -> this
        }
    }

    fun result() = depth * position
}
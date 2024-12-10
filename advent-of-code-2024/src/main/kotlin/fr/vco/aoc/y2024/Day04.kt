package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day04")

    println("Part 1 : ${input.countWord("XMAS")}")
    println("Part 2 : ${input.countXWord("MAS")}")
}

val DIAGONALS = listOf(
    Position(0, 0) to Position(1, 1),
    Position(0, 1) to Position(1, -1),
    Position(1, 0) to Position(-1, 1),
    Position(1, 1) to Position(-1, -1)
)

fun List<String>.countWord(word: String): Int {
    return indices.sumOf { y ->
        (0..this[y].lastIndex).sumOf { x ->
            this.countWord(word, Position(x, y))
        }
    }
}

fun List<String>.countWord(word: String, pos: Position): Int {
    return DIRECTIONS.count { dir -> this.hasWord(word, pos, dir) }
}

fun List<String>.countXWord(word: String): Int {
    return indices.sumOf { y ->
        (0..this[y].lastIndex).count { x ->
            this.hasXWord(word, Position(x, y))
        }
    }
}

fun List<String>.hasXWord(word: String, pos: Position): Boolean {
    return DIAGONALS.map { (start, dir) -> (pos + start * (word.length - 1)) to dir }
        .count { (start, dir) -> hasWord(word, start, dir) } == 2
}

fun List<String>.hasWord(word: String, pos: Position, direction: Position): Boolean {
    return (0..word.lastIndex)
        .map { i -> i to pos + direction * i }
        .all { (i, pos) -> word[i] == this.getOrNull(pos.y)?.getOrNull(pos.x) }
}


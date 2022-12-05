package fr.vco.aoc.y2022

typealias CratesStacks = List<ArrayDeque<Char>>

fun main() {
    val (inputStacks, inputMoves) = readLines("Day05").split { it.isBlank() }
    val moves = inputMoves.map { it.toCraneMove() }

    println("Part 1 : ${inputStacks.toCrateStacks().applyCraneMoves9000(moves).getTopCrates()}")
    println("Part 2 : ${inputStacks.toCrateStacks().applyCraneMoves9001(moves).getTopCrates()}")
}

data class CraneMove(val qty: Int, val from: Int, val to: Int)

fun String.toCraneMove(): CraneMove {
    val (qty, from, to) = """^move (\d+) from (\d+) to (\d+)$""".toRegex().find(this)!!.destructured
    return CraneMove(qty.toInt(), from.toInt() - 1, to.toInt() - 1)
}

fun CratesStacks.getTopCrates() = joinToString("") { it.last().toString() }

fun CratesStacks.applyCraneMoves9000(moves: List<CraneMove>) = apply {
    moves.forEach { move ->
        repeat(move.qty) {
            this[move.to].addLast(this[move.from].removeLast())
        }
    }
}

fun CratesStacks.applyCraneMoves9001(moves: List<CraneMove>) = apply {
    moves.forEach { move ->
        val temp = mutableListOf<Char>()
        repeat(move.qty) {
            temp.add(this[move.from].removeLast())
        }
        this[move.to].addAll(temp.reversed())
    }
}

fun List<String>.transposed(): List<String> {
    val transposedList = List<MutableList<Char>>(this.first().length) { mutableListOf() }
    this.reversed().forEach { line ->
        line.forEachIndexed { i, char -> transposedList[i].add(char) }
    }
    return transposedList.map { it.joinToString("") }
}

fun List<String>.toCrateStacks(): CratesStacks {
    return transposed()
        .filterNot { it.startsWith(" ") }
        .map { ArrayDeque(it.drop(1).toList().filterNot { it == ' ' }) }
}

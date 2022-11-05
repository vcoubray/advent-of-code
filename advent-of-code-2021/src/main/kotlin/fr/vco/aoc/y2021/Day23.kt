package fr.vco.aoc.y2021

import java.util.*
import kotlin.math.absoluteValue

fun main() {
    val input = readLines("Day23")

    val (statePart1, statePart2) = input.split { it == "" }.map {
        val chamberSize = it.size - 3
        val board = it.transpose()
            .drop(1)
            .dropLast(1)
            .map { LinkedList<Char>().apply { addAll(it) } }
        State(chamberSize, board)
    }

    println("Part 1 : ${findBestCost(statePart1)?.cost}")
    println("Part 2 : ${findBestCost(statePart2)?.cost}")
}

fun List<String>.transpose(): List<List<Char>> {
    return if (this.isEmpty()) emptyList()
    else this.first().indices.map { y ->
        this.indices.map { x ->
            this.getOrNull(x)?.getOrNull(y) ?: ' '
        }.filter { it in "ABCD" }
    }
}

val chambers = mapOf(2 to 'A', 4 to 'B', 6 to 'C', 8 to 'D')
val costs = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

data class Action(val origin: Int, val target: Int, val cost: Int)

data class State(
    val chamberSize: Int,
    val board: List<MutableList<Char>>,
    var cost: Int = 0
) {

    private fun isValidChamber(x: Int) = board[x].all { chambers[x] == it }

    private fun getTargets(progression: IntProgression, amphipod: Char, start: Int): List<Int> {
        val targets = mutableListOf<Int>()
        for (i in progression) {
            when {
                i in chambers.keys -> if (chambers[i] == amphipod && isValidChamber(i)) targets.add(i)
                board[i].isNotEmpty() -> break
                start in chambers.keys -> targets.add(i)
            }
        }
        return targets
    }

    private fun getTargets(amphipod: Char, start: Int): List<Action> {
        return buildList {
            addAll(getTargets(start - 1 downTo 0, amphipod, start))
            addAll(getTargets(start + 1 until board.size, amphipod, start))
        }.map { target ->
            var cost = (start - target).absoluteValue
            if (start in chambers.keys) cost += chamberSize + 1 - board[start].size
            if (target in chambers.keys) cost += chamberSize - board[target].size
            Action(start, target, cost * costs[amphipod]!!)
        }
    }

    fun getActions(): List<Action> {
        val starts = board.mapIndexedNotNull { x, it ->
            it.firstOrNull()?.let { it to x }
        }.filterNot { (amphipod, x) -> chambers[x] == amphipod && isValidChamber(x) }
        return starts.flatMap { (amphipod, x) -> getTargets(amphipod, x) }
    }

    fun copy(cost: Int = this.cost) = State(
        chamberSize = chamberSize,
        board = board.map { it.toMutableList() },
        cost = cost
    )

    fun play(action: Action) {
        cost += action.cost
        val amphipod = board[action.origin].removeFirst()
        board[action.target].add(0, amphipod)
    }

    fun isValid() = chambers.keys.all { board[it].size == chamberSize && isValidChamber(it) }

    override fun equals(other: Any?): Boolean {
        return if (other is State) this.board == other.board
        else false
    }

    override fun hashCode(): Int {
        return board.hashCode()
    }
}

private fun findBestCost(state: State): State? {
    val costComparator = compareBy<State> { it.cost }
    val toVisit = PriorityQueue(costComparator).apply { add(state) }
    val visited = mutableMapOf<State, Int>()
    while (toVisit.isNotEmpty()) {
        val current = toVisit.poll()
        if (current.isValid()) return current

        if ((visited[current] ?: Int.MAX_VALUE) > current.cost) {
            current.getActions().forEach { action ->
                toVisit.add(current.copy().apply { play(action) })
            }
            visited[current] = current.cost
        }
    }
    return null
}

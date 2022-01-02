package fr.vco.aoc.y2021

import java.util.*

fun main() {
    val input = readLines("Day23")
    val state = State(input.map { it.toMutableList() })

    state.board.forEach { println(it.joinToString("")) }

//    println(state.isValid())
//    state.play(Action(3, 2, 5, 2))
//    state.play(Action(3, 3, 9, 3))
//    state.play(Action(7, 2, 5, 3))
//    state.play(Action(7, 2, 9, 2))
//    state.play(Action(7, 3, 9, 3))
//
//    state.board.forEach { println(it.joinToString("")) }
//    println(state.isValid())

//    state.getTargets(3, 2).forEach(::println)
//    state.getActions().forEach(::println)

//    val array = "abcdef".toCharArray()
//    val array2 = "abcdef".toCharArray()
//    println(array.contentEquals(array2))

     findValidState(state)?.board?.forEach { println(it.joinToString("")) }


}


private fun findValidState(state: State): State? {
    val toVisit = LinkedList<State>().apply { add(state) }
    val visited = mutableSetOf<State>()
    while (toVisit.isNotEmpty()) {
        val current = toVisit.poll()
        visited.add(current)
        current.board.forEach { println(it.joinToString("")) }
        current.getActions().forEach { action ->
            val child = current.copy().apply { play(action) }
            if (child.isValid()) return child
            if (child !in visited && child !in toVisit) {
                toVisit.addLast(child)
            }
        }

    }
    return null

}


data class Action(val xOrigin: Int, val yOrigin: Int, val xTarget: Int, val yTarget: Int)

private fun List<List<Char>>.getNeighbours(x: Int, y: Int): List<Pair<Int, Int>> {
    return listOfNotNull(
        this.getOrNull(y + 1)?.getOrNull(x)?.let { x to y + 1 },
        this.getOrNull(y - 1)?.getOrNull(x)?.let { x to y - 1 },
        this.getOrNull(y)?.getOrNull(x + 1)?.let { x + 1 to y },
        this.getOrNull(y)?.getOrNull(x - 1)?.let { x - 1 to y }
    )
}

private fun isValidTarget(amphipod: Char, x: Int, y: Int): Boolean {
    return (y == 1 && x !in listOf(3, 5, 7, 9)) ||
        (amphipod == 'A' && y != 1 && x == 3) ||
        (amphipod == 'B' && y != 1 && x == 5) ||
        (amphipod == 'C' && y != 1 && x == 7) ||
        (amphipod == 'D' && y != 1 && x == 9)
}


data class State(val board: List<MutableList<Char>>, var cost: Int = 0) {

    fun getActions(): List<Action> {
        return board.flatMapIndexed { y, line ->
            line.mapIndexed { x, _ -> x to y }.filter { (x, y) -> board[y][x] in "ABCD" }
        }
            .filter{(x,_)-> x != 1}
            .flatMap { (x, y) -> getTargets(x, y) }
    }

    private fun getTargets(xOrigin: Int, yOrigin: Int): List<Action> {
        val amphipod = board[yOrigin][xOrigin]
        val toVisit = LinkedList<Pair<Int, Int>>().apply { add(xOrigin to yOrigin) }
        val visited = mutableSetOf<Pair<Int, Int>>()

        while (toVisit.isNotEmpty()) {
            val (x, y) = toVisit.poll()
            visited.add(x to y)
            board.getNeighbours(x, y).forEach { neighbor ->
                if (board[neighbor.second][neighbor.first] == '.' && neighbor !in visited) {
                    toVisit.addLast(neighbor)
                }
            }
        }

        return visited
            .filterNot { (x, y) -> x == xOrigin && y == yOrigin }
            .filter { (x, y) -> isValidTarget(amphipod, x, y) }
            .map { (x, y) -> Action(xOrigin, yOrigin, x, y) }
    }

    fun copy(cost: Int = this.cost) = State(
        board = board.map { it.toMutableList() },
        cost = cost
    )


    fun play(action: Action) {
        val origin = board[action.yOrigin][action.xOrigin]
        board[action.yOrigin][action.xOrigin] = board[action.yTarget][action.xTarget]
        board[action.yTarget][action.xTarget] = origin
    }

    fun isValid(): Boolean =
        board[2][3] == 'A' &&
            board[3][3] == 'A' &&
            board[2][5] == 'B' &&
            board[3][5] == 'B' &&
            board[2][7] == 'C' &&
            board[3][7] == 'C' &&
            board[2][9] == 'D' &&
            board[3][9] == 'D'

}

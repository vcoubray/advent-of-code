package fr.vco.aoc.y2021

import java.util.*

fun main() {
    val input = readLines("Day23")

    val state = State(input.transpose().drop(1).dropLast(1).map { LinkedList<Char>().apply{addAll(it)} })
    state.board.forEach { println("->" + it.joinToString("")) }

//    println(state.getActions())
//    state.play(Action2(2,0))
//    state.play(Action2(2,1))
//    state.play(Action2(4,2))
//    state.play(Action2(8,10))
//    state.play(Action2(8,2))
//    state.play(Action2(4,8))
//    state.play(Action2(6,4))
//    state.play(Action2(6,8))
//    state.play(Action2(1,6))
//    state.play(Action2(10,6))
//    state.play(Action2(0,4))
//    state.board.forEach { println("->" + it.joinToString("")) }
//    println(state.isValid())
//    println(state.getActions())


//    Board.init(input)
//    Board.board.forEach { println(it.joinToString("")) }
//    val state = State(Board.startPositions)

//    val state = StateOld(input.drop(1).dropLast(1).map { it.toMutableList() })
    findValidState(state)?.board?.forEach { println(it.joinToString("")) }

}

fun List<String>.transpose(): List<List<Char>> {
    return if (this.isEmpty()) emptyList()
    else this.first().indices.map { y ->
        this.indices.map { x ->
            this.getOrNull(x)?.getOrNull(y) ?: ' '
        }.filter { it in "ABCD" }
    }
}

val chamber2 = mapOf('A' to 2, 'B' to 4, 'C' to 6, 'D' to 8)
val revertChamber = chamber2.map{(k,v) -> v to k}.toMap()

data class Action(val origin: Int, val target: Int)

data class State(
    val board: List<MutableList<Char>>,
    val cost: Int = 0,
) {

    fun getTargets(amphipod: Char, x: Int) : List<Action> {
        val targets = mutableListOf<Action>()

        var i = x - 1
        while ( i >= 0 ) {
            if(i in revertChamber.keys) {
                if(revertChamber[i] == amphipod && board[i].all{it == amphipod})
                    targets.add(Action(x,i))
            }
            else if (board[i].isNotEmpty()) break
            else if (x in revertChamber.keys ) targets.add(Action(x,i))
            i--
        }

        i = x + 1
        while ( i < board.size ) {
            if(i in revertChamber.keys) {
                if(revertChamber[i] == amphipod && board[i].all{it == amphipod})
                    targets.add(Action(x,i))
            }
            else if (board[i].isNotEmpty()) break
            else if (x in revertChamber.keys ) targets.add(Action(x,i))
            i++
        }
        return targets
    }

    fun getActions(): List<Action> {
        val starts = board.mapIndexedNotNull { x, it ->
            it.firstOrNull()?.let{ it to x }
        }.filterNot{(it,x) -> x == chamber2[it]!!}
        return starts.flatMap{(amphipod,x ) -> getTargets(amphipod,x)}

    }

    fun copy(cost: Int = this.cost) = State(
        board = board.map { it.toMutableList() },
        cost = cost
    )

    fun play(action: Action) {
        val amphipod = board[action.origin].removeFirst()
        board[action.target].add(0,amphipod)
    }

    fun isValid() = chamber2.all { (type, x) -> board[x].size == 2 && board[x].all { it == type } }

}


private fun findValidState(state: State): State? {
    val toVisit = LinkedList<State>().apply { add(state) }
    val visited = mutableSetOf<State>()
    var count = 0
    var start = System.currentTimeMillis()
    while (toVisit.isNotEmpty()) {
        val current = toVisit.poll()
        count++
        visited.add(current)
//        current.board.forEach { println(it.joinToString("")) }
        if (count % 1000 == 0) println("$count in ${System.currentTimeMillis() - start}ms")
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

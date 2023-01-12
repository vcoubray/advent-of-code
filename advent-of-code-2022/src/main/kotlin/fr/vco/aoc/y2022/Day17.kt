package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day17").first()
    val winds = input.map { if (it == '>') RIGHT else LEFT }

    println("Part 1 : ${RockStack(7, winds).play(2022)}")
    println("Part 2 : ${RockStack(7, winds).play(1000000000000)}")
}

data class Pos(val x: Int, val y: Long) {
    operator fun plus(pos: Pos) = Pos(x + pos.x, y + pos.y)
    operator fun minus(pos: Pos) = Pos(x - pos.x, y - pos.y)
}

val LEFT = Pos(-1, 0)
val RIGHT = Pos(1, 0)
val DOWN = Pos(0, -1)


data class Rock(val pos: Pos, val type: Int) {
    val rock = ROCK_TYPE[type].map { it + pos }

    companion object {
        val ROCK_TYPE = listOf(
            List(4) { Pos(it, 0) },
            listOf(Pos(0, 1), Pos(1, 1), Pos(2, 1), Pos(1, 0), Pos(1, 2)),
            listOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(2, 1), Pos(2, 2)),
            List(4) { Pos(0, it.toLong()) },
            listOf(Pos(0, 0), Pos(1, 0), Pos(0, 1), Pos(1, 1)),
        )
    }
    fun move(direction: Pos) = copy(pos = pos + direction)
}


class RockStack(private val width: Int, private val winds: List<Pos>) {
    private val stack = mutableMapOf<Long, MutableList<Int>>()
    private var windId = 0

    private fun getHeight() = stack.maxOfOrNull { it.key + 1 } ?: 0

    private fun isValidPos(pos: Pos) = when {
        pos.y < 0 -> false
        pos.x !in 0 until width -> false
        pos.y !in stack.keys -> true
        else -> pos.x !in stack[pos.y]!!
    }

    private fun isValidPos(rock: Rock) = rock.rock.all { isValidPos(it) }

    private fun addRock(rock: Rock) = rock.rock.sortedBy { it.y }
        .forEach {
            if (it.y !in stack) stack[it.y] = mutableListOf(it.x)
            else stack[it.y]!!.add(it.x)
        }

    private fun playRock(rockType: Int) {
        var rock = Rock(Pos(2, getHeight() + 3), rockType)
        do {
            val lastY = rock.pos.y
            rock = nextPos(rock, winds[windId])
            windId++
            if (windId >= winds.size) windId = 0
        } while (rock.pos.y != lastY)
        addRock(rock)
    }

    fun nextPos(rock: Rock, wind: Pos): Rock {
        val newRock = rock.move(wind).takeIf(::isValidPos) ?: rock
        return newRock.move(DOWN).takeIf(::isValidPos) ?: newRock
    }

    data class Log(var turn: Long, var rockType: Int, var height: Long, var topStack: List<List<Int>>) {
        override fun equals(other: Any?): Boolean {
            return if(other !is Log) false
            else rockType == other.rockType && topStack == other.topStack
        }

        override fun hashCode(): Int {
            var result = rockType
            result = 31 * result + topStack.hashCode()
            return result
        }

        fun update(log: Log) {
            turn = log.turn
            rockType = log.rockType
            height = log.height
            topStack = log.topStack
        }
    }

    fun play(turn: Long): Long {

        val windsLogs = List(winds.size){Log(0,0,0, emptyList())}
        val heightLogs = mutableListOf<Long>()

        var rockType = 0
        for (i in 0 until turn) {
            playRock(rockType)

            val topStack = stack.keys.toList().sorted().takeLast(5).mapNotNull { stack[it]?.toList() }
            val log = Log(i, rockType, getHeight(), topStack)

            if (windsLogs[windId] == log) {

                val cycleSize = log.turn - windsLogs[windId].turn
                val startCycle = i - cycleSize
                val cycleHeight = getHeight() - heightLogs[startCycle.toInt()]
                val remainTurns = turn - i

                val neededCycles = remainTurns / cycleSize
                val turnNeeded = remainTurns % cycleSize - 1
                return getHeight() + cycleHeight * neededCycles + heightLogs[(startCycle + turnNeeded).toInt()] - heightLogs[startCycle.toInt()]

            }
            windsLogs[windId].update(log)
            heightLogs.add(getHeight())
            rockType++
            if (rockType >= Rock.ROCK_TYPE.size) rockType = 0
        }
        return getHeight()
    }
}

package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import java.util.*
import kotlin.math.max
import kotlin.math.min

const val NORTH = 1
const val SOUTH = 2
const val WEST = 3
const val EAST = 4

const val OX_WALL = 0
const val OX_NOTHING = 1
const val OX_SYSTEM = 2
const val OX_UNKNOWN = 3
fun main() {

    val input = getInputReader("/2019/inputDay15.txt")
    val intCode = input.readText().trim().split(",").map { it.toLong() }

    val droid = RepairDroid(intCode)

    val map = OxMap()
    val origin = map.get(0, 0)
    lateinit var system : OxTile
    origin.type = OX_NOTHING


    var currentTile = origin
    var path = map.shortestPath(currentTile, OX_UNKNOWN)
    while (path.isNotEmpty()) {
        val target = path.last()
        val type = droid.move(currentTile.getDirection(target))
        target.type = type
        if( type == OX_SYSTEM) system = target
        if (type != OX_WALL) currentTile = target
        path = map.shortestPath(currentTile, OX_UNKNOWN)
    }
    println(map)
    println("Part 1 : ${map.shortestPath(origin, OX_SYSTEM,false).size} ")
    println("Part 2 : ${map.longestDistance(system)} ")

}



class OxMap {
    private val tiles = mutableMapOf<Int, MutableMap<Int, OxTile>>()

    init {
        get(0, 0)
    }

    fun get(x: Int, y: Int): OxTile {
        tiles.putIfAbsent(y, mutableMapOf())
        tiles[y]!!.putIfAbsent(x, OxTile(x, y))
        return tiles[y]!![x]!!
    }

    private fun getNeighbors(tile: OxTile): List<OxTile> {
        return listOf(
            get(tile.x - 1, tile.y),
            get(tile.x + 1, tile.y),
            get(tile.x, tile.y - 1),
            get(tile.x, tile.y + 1)
        )
    }

    fun longestDistance(origin: OxTile) :Int{
        val visited = mutableListOf<OxTile>()
        val paths = mutableMapOf<OxTile, OxPath>()
        val toVisit = LinkedList<OxTile>()
        toVisit.add(origin)

        while (!toVisit.isEmpty()) {
            val currentTile = toVisit.poll()!!
            if (visited.contains(currentTile)) continue
            visited.add(currentTile)

            getNeighbors(currentTile).forEach {

                if (!visited.contains(it) && !toVisit.contains(it) && it.type != OX_WALL && it.type != OX_UNKNOWN) {
                    toVisit.addLast(it)
                    paths[it] = OxPath((paths[currentTile]?.dist?:0)+1,currentTile)
                }
            }
        }
        return paths.maxBy{it.value.dist }!!.value.dist
    }

    fun shortestPath(origin: OxTile, target: Int, withUnknown : Boolean = true): List<OxTile> {
        val visited = mutableListOf<OxTile>()
        val paths = mutableMapOf<OxTile, OxPath>()
        val toVisit = LinkedList<OxTile>()
        toVisit.add(origin)

        while (!toVisit.isEmpty()) {

            val currentTile = toVisit.poll()!!
            if (visited.contains(currentTile)) continue
            visited.add(currentTile)

            if (currentTile.type == target) return getPath(currentTile, paths)

            getNeighbors(currentTile).forEach {

                if (!visited.contains(it) && !toVisit.contains(it) && it.type != OX_WALL && (withUnknown || it.type != OX_UNKNOWN)) {
                    toVisit.addLast(it)
                    paths[it] = OxPath(paths[currentTile]?.dist?:0+1,currentTile)
                }
            }

        }
        return emptyList()
    }

    private fun getPath(oxTile: OxTile, paths: Map<OxTile, OxPath>): List<OxTile> {
        var current = oxTile
        var path = paths[current]

        val resultPath = mutableListOf(current)
        while (path != null && paths.contains(path.parent)) {
            current = path.parent
            path = paths[current]
            resultPath.add(current)
        }
        return resultPath
    }

    override fun toString(): String {
        val sb = StringBuilder()

        var minX = 0
        var maxX = 0
        tiles.forEach {
            minX = min(minX, it.value.minBy { x -> x.key }!!.key)
            maxX = max(maxX, it.value.maxBy { x -> x.key }!!.key)
        }
        tiles.toSortedMap().forEach {
            val y = it.key
            for (x in minX..maxX) {
                val type = when (get(x, y).type) {
                    OX_WALL -> "#"
                    OX_NOTHING -> "."
                    OX_UNKNOWN -> "?"
                    OX_SYSTEM -> "X"
                    else -> "?"
                }
                sb.append(type)
            }
            sb.append("\n")
        }
        return sb.toString()
    }

}

data class OxPath( val dist: Int, val parent: OxTile)
data class OxTile(val x: Int, val y: Int, var type: Int = OX_UNKNOWN) {

    fun getDirection(target: OxTile) =
        when {
            this.x < target.x -> EAST
            this.x > target.x -> WEST
            this.y < target.y -> SOUTH
            this.y > target.y -> NORTH
            else -> NORTH
        }

}

class RepairDroid(intCode: List<Long>) {

    private val opCode = OpCode(intCode, OpCodeStream())

    fun move(dir: Int): Int {
        opCode.stream.input.add(dir.toLong())
        opCode.exec()
        return opCode.stream.output.poll().toInt()
    }

}


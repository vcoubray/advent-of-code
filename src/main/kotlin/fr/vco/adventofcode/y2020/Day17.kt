package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {
    val input = getInputReader("/2020/inputDay17.txt")
    val lines = input.readLines()

    val area = Area().apply { init(lines) }
    area.print()



    println("Part 1 :${area.cycleUntil(6).count()}")


}


class Area(val cubes : MutableMap<Int, MutableMap<Int, MutableMap<Int, Boolean>>> = mutableMapOf()) {

    lateinit var height: IntRange // z
    lateinit var depth: IntRange // y
    lateinit var width: IntRange // x



    fun init(area: List<String>) {
        height = 0..0
        depth = area.indices
        width = area.first().indices

        cubes[0] = mutableMapOf()
        area.forEachIndexed { y, line ->
            cubes[0]!![y] = line.mapIndexed { x, it -> x to (it == '#') }.toMap().toMutableMap()
        }
    }

    fun getCube(x: Int, y: Int, z: Int): Boolean {
        if(x !in width || y !in depth || z !in height) return false
        return cubes[z]!![y]!![x]!!
    }

    fun createCubeIfNotExist(x: Int, y: Int, z: Int, active : Boolean = false) {
        if (!cubes.containsKey(z)) cubes[z] = mutableMapOf()
        if (!cubes[z]!!.containsKey(y)) cubes[z]!![y] = mutableMapOf()
        if (!cubes[z]!![y]!!.containsKey(x)) cubes[z]!![y]!![x] = active
    }

    fun getNeighbours(x :Int, y : Int, z: Int) : List<Boolean> {
        val neighbours = mutableListOf<Boolean>()
        for(i in z-1..z+1) for (j in y-1..y+1) for (k in x-1..x+1) {
            if(i != z || j !=y || k != x)
                neighbours.add(getCube(k,j,i))
        }
        return neighbours
    }


    fun expand(): Area {
        val area = Area()
        area.height = height.expand(1)
        area.depth = depth.expand(1)
        area.width = width.expand(1)

        for(z in area.height)
            for (y in area.depth)
                for (x in area.width)
                    area.createCubeIfNotExist(x,y,z,false)
        return area

    }

    fun IntRange.expand(expansion: Int) = this.first-expansion..this.last+expansion

    fun cycle(): Area {
        val area = expand()
        area.cubes.forEach{(z,plan) ->
            plan.forEach{(y,line) ->
                line.forEach{(x, _) ->
                    val cube = getCube(x,y,z)
                    val neighbours = getNeighbours(x,y,z)
                    when {
                        cube && neighbours.count{it} in 2..3 -> area.cubes[z]!![y]!![x] = true
                        cube && neighbours.count{it} !in 2..3 -> area.cubes[z]!![y]!![x] = false
                        !cube && neighbours.count{it} == 3 -> area.cubes[z]!![y]!![x] = true
                        !cube && neighbours.count{it} != 3 -> area.cubes[z]!![y]!![x] = false
                        else -> area.cubes[z]!![y]!![x] = cube
                    }
                }
            }
        }
        return area
    }
    fun cycleUntil(turn :Int) : Area{
        var area = this
        repeat(turn){
            area = area.cycle()
            area.print()
        }
        return area
    }


    fun count(): Int{
        return cubes.map{it.value.map{n-> n.value.map{m -> m.value}}}.flatten().flatten().count{ it }
    }

    fun print() {
        cubes.forEach{ (z,plan) ->
            println("Z = ${z}")
            plan.forEach { (y, line) ->
                line.forEach { (x, cube) ->
                    print(if(cube)"#" else ".")
                }
                println()
            }
            println()
        }
        println()
    }
}



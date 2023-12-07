package fr.vco.adventofcode.y2019

import java.io.InputStreamReader
import java.util.*

fun main () {

    val input = getInputReader("Day06")
    val (origin,you,santa) = createOrbitTree(input)


    println("Part 1 : ${getOrbitCount(origin)}")
    println("Part 2 : ${getShortestPath(you, santa)}")

}

fun createOrbitTree( input: InputStreamReader) : List<Planet> {
    val planetsMap = hashMapOf<String, Planet>()
    lateinit var origin : Planet
    lateinit var santa : Planet
    lateinit var you : Planet
    input.readLines().forEach{
        val orbit = it.split(")")
        val parentName = orbit[0]
        val childName = orbit[1]

        val planetParent = planetsMap[parentName]?: Planet(
            parentName,
            mutableListOf()
        )
        val planetChild = planetsMap[childName]?: Planet(
            childName,
            mutableListOf()
        )

        planetParent.neighbors.add(planetChild)
        planetChild.neighbors.add(planetParent)

        planetsMap[parentName] = planetParent
        planetsMap[childName] = planetChild

        if (parentName == "COM") origin = planetParent
        if (childName == "YOU") you = planetParent
        if (childName == "SAN") santa = planetParent
    }
    return listOf(origin,you,santa)
}

fun getShortestPath(origin : Planet, target : Planet) : Int {

    val planetToVisit = LinkedList<PlanetOrbit>()
    val alreadyVisited = mutableListOf<Planet>()
    planetToVisit.add(PlanetOrbit(origin, 0))
    alreadyVisited.add(origin)

    while (planetToVisit.isNotEmpty()){
        val currentPlanetOrbit = planetToVisit.poll()!!
        val orbitChild = currentPlanetOrbit.orbitCount+1
        currentPlanetOrbit.planet.neighbors
            .filterNot(alreadyVisited::contains)
            .forEach{ child ->
                if(child == target) return orbitChild
                planetToVisit.add(PlanetOrbit(child, orbitChild))
                alreadyVisited.add(child)
            }
    }
    return 0
}



fun getOrbitCount(origin : Planet) : Int {
    var count = 0
    val planetToVisit = LinkedList<PlanetOrbit>()
    val alreadyVisited = mutableListOf<Planet>()
    planetToVisit.add(PlanetOrbit(origin, 0))
    alreadyVisited.add(origin)
    while (planetToVisit.isNotEmpty()){
        val currentPlanetOrbit = planetToVisit.poll()!!
        val orbitChild = currentPlanetOrbit.orbitCount+1
        currentPlanetOrbit.planet.neighbors
            .filterNot(alreadyVisited::contains)
            .forEach{ child ->
                count+= orbitChild
                planetToVisit.add(PlanetOrbit(child, orbitChild))
                alreadyVisited.add(child)
            }
        }

    return count
}


class Planet(
    val name : String,
    val neighbors : MutableList<Planet>
)

class PlanetOrbit(
    val planet : Planet,
    val orbitCount : Int
)
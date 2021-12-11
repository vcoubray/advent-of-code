package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day11")
    val octopuses = input.flatMapIndexed { y, line ->
        line.mapIndexed { x, it -> Octopus(it.digitToInt(), input.getNeighbors(x, y)) }
    }

    println(octopuses.simulate(100))
}


private fun List<String>.get(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x)
private fun List<String>.convertCoord(x: Int, y: Int) = this.get(x, y)?.let { this.first().length * y + x }
private fun List<String>.getNeighbors(x: Int, y: Int) = buildList<Int> {
    for (i in -1..1)
        for (j in -1..1) {
            if (i != 0 || j != 0)
                convertCoord(x + i, y + j)?.let(this::add)
        }
}

private data class Octopus(var energy: Int, val neighbors: List<Int>)

private fun List<Octopus>.simulate(step: Int) :Int{
    var flash = 0
    repeat(step) { i ->
        // Increase energy
        this.forEach { it.energy++ }
        // Flash
        while (this.any { it.energy > 9 }) {
            this.forEach { octopus ->
                if (octopus.energy > 9) {
                    octopus.energy = -1
                    flash++
                    octopus.neighbors.forEach { n ->
                        if (this[n].energy != -1) this[n].energy++
                    }
                }
            }
        }
        this.forEach{ if(it.energy == -1 ) it.energy = 0 }
    }
    return flash
}
private fun List<Octopus>.print() = this.chunked(10).joinToString("\n"){it.joinToString(""){o-> "${o.energy}"} }
package fr.vco.aoc.y2024


const val BATHROOM_WIDE = 101
const val BATHROOM_TALL = 103

fun main() {

    val input = readLines("Day14")

    val robots = input.map { it.toRobot() }
    val posPart1 = robots.map { it.move(100) }

    val quadrants = Zone(0..<BATHROOM_WIDE, 0..<BATHROOM_TALL).getQuadrants()
    println("Part 1: ${quadrants.map { quadrant -> posPart1.count { it in quadrant } }.fold(1L) { acc, a -> acc * a }}")

    val (time, _) = (0..<BATHROOM_TALL * BATHROOM_WIDE)
        .map { robots.map { robot -> robot.move(it) } }
        .mapIndexed { i, positions -> i to quadrants.maxOf { quadrant -> positions.count { it in quadrant } } }
        .maxBy {(_, maxByQuadrants ) ->  maxByQuadrants }

    println("Part 2 : $time")
    robots.map { it.move(time) }.print()

}

data class Robot(val start: Position, val vector: Position) {
    fun move(step: Int): Position {
        return (start + vector * step).mod(Position(BATHROOM_WIDE, BATHROOM_TALL))
    }
}

fun String.toRobot(): Robot {
    val (px, py, vx, vy) = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex().find(this)!!.destructured
    return Robot(
        Position(px.toInt(), py.toInt()),
        Position(vx.toInt(), vy.toInt())
    )
}

data class Zone(val xRange: IntRange, val yRange: IntRange) {
    operator fun contains(p: Position) = p.x in xRange && p.y in yRange
    fun getQuadrants(): List<Zone> {
        val xSize = xRange.last - xRange.first + 1
        val ySize = yRange.last - yRange.first + 1
        return listOf(
            Zone(xRange.first..<xRange.first + xSize / 2, yRange.first..<yRange.first + ySize / 2),
            Zone(xRange.first..<xRange.first + xSize / 2, yRange.last - ySize / 2 + 1..yRange.last),
            Zone(xRange.last - xSize / 2 + 1..xRange.last, yRange.first..<yRange.first + ySize / 2),
            Zone(xRange.last - xSize / 2 + 1..xRange.last, yRange.last - ySize / 2 + 1..yRange.last),
        )

    }
}

fun List<Position>.print() {
    repeat(BATHROOM_TALL) { y ->
        val line = (0..<BATHROOM_WIDE).joinToString("") { x ->
            this.count { it.x == x && it.y == y }.takeIf { it > 0 }?.toString() ?: "."
        }
        println(line)
    }
}

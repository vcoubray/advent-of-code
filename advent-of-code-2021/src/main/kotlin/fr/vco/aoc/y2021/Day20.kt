package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day20")

    val enhancementAlgo = input.first().map { it == '#' }
    val image = input.drop(1).filterNot { it == "" }.map { line -> line.map { it == '#' } }

    println("Part 1 : ${image.enhance(enhancementAlgo,2).countLit()}")
    println("Part 2 : ${image.enhance(enhancementAlgo,50).countLit()}")
}

fun List<List<Boolean>>.countLit() = this.sumOf { line -> line.count { it } }

fun List<List<Boolean>>.enhance(algo: List<Boolean>, times: Int): List<List<Boolean>> {
    return (0 until times).fold(this) { image, turn -> image.enhanceOnce(algo, turn) }
}

fun List<List<Boolean>>.enhanceOnce(algo: List<Boolean>, turn: Int): List<List<Boolean>> {
    val outboundValue = algo.first() && turn % 2 != 0
    return List(this.size + 2) { y ->
        List(this.first().size + 2) { x ->
            algo[getEnhancementValue(x - 1, y - 1, outboundValue )]
        }
    }
}

fun List<List<Boolean>>.getEnhancementValue(x: Int, y: Int, outboundValue: Boolean): Int {
    return buildList {
        for (iy in -1..1) {
            for (ix in -1..1) {
                this.add(this@getEnhancementValue.getValue(x + ix, y + iy, outboundValue))
            }
        }
    }.joinToString("") { if (it) "1" else "0" }.toInt(2)
}

fun List<List<Boolean>>.getValue(x: Int, y: Int, outboundValue: Boolean) =
    this.getOrNull(y)?.getOrNull(x) ?: outboundValue
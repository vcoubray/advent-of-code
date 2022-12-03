package fr.vco.aoc.y2022

fun main() {
    val input = readLines("Day03")

    val part1 = input
        .map{it.chunkHalf()}
        .map(::findCommonItems)
        .sumOf{it.first().priority()}
    println("Part 1 : $part1")

    val part2 = input
        .chunked(3)
        .map(::findCommonItems)
        .sumOf{it.first().priority()}
    println("Part 2 : $part2")
}

fun String.chunkHalf() = listOf (
    take(length / 2),
    takeLast(length / 2)
)

fun findCommonItems(backpacks: List<String>): Set<Char> {
    return backpacks.map{it.toSet()}.reduce{acc, backpack -> acc.intersect(backpack)}
}

fun Char.priority() =
    if (isLowerCase()) code - 'a'.code + 1
    else code - 'A'.code + 27

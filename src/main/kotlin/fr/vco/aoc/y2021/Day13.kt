package fr.vco.aoc.y2021

fun main() {
    val input = readLines("Day13")
    val (inputFolds, inputDots) = input.filterNot { it == "" }.partition { it.startsWith("fold along") }

    val dots = inputDots
        .map { it.split(",") }
        .map { (x, y) -> x.toInt() to y.toInt() }
        .toSet()
    val folds = inputFolds
        .map { it.removePrefix("fold along ").split("=") }
        .map { (axis, value) -> Folder(value.toInt(), axis) }

    println("Part 1 : ${folds.first().fold(dots).count()}")
    println("Part 2 : ")
    folds.fold(dots) { foldedDots, folder -> folder.fold(foldedDots) }
        .groupBy { (_, y) -> y }.toSortedMap()
        .map { (y, dots) -> y to dots.map { (x, _) -> x } }
        .map { (_, line) -> List(line.maxOrNull()!! + 1) { if (it in line) "#" else " " }.joinToString("") }
        .forEach(::println)
}

private class Folder(val line: Int, axis: String) {
    val foldAxis = if (axis == "x") ::foldX else ::foldY
    fun fold(dot: Pair<Int, Int>) = foldAxis(dot)
    fun fold(dots: Set<Pair<Int, Int>>) = dots.map { fold(it) }.toSet()
    private fun foldX(dot: Pair<Int, Int>) = Pair(foldValue(dot.first), dot.second)
    private fun foldY(dot: Pair<Int, Int>) = Pair(dot.first, foldValue(dot.second))
    private fun foldValue(value: Int) = if (value < line) value else 2 * line - value
}

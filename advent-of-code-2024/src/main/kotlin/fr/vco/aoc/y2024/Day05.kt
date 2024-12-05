package fr.vco.aoc.y2024

class Page(
    val number: Int,
    val previous: MutableSet<Int> = mutableSetOf()
)

fun main() {
    val (orderInput, updateInputs) = readLines("Day05").split { it.isEmpty() }

    val pages = mutableMapOf<Int, Page>()
    orderInput.forEach { order ->
        val (first, next) = order.split("|").map { it.toInt() }
        pages.computeIfAbsent(next) { Page(next) }.previous.add(first)
    }

    val (correctUpdates, incorrectUpdates) = updateInputs
        .map { line -> line.split(',').map { it.toInt() } }
        .partition { it.isValidUpdate(pages) }

    println("Part 1 : ${correctUpdates.sumOf { it.getMiddleValue() }}")
    println("Part 2 : ${incorrectUpdates.sumOf { it.reordering(pages).getMiddleValue() }}")
}

fun List<Int>.isValidUpdate(pages: Map<Int, Page>): Boolean {
    val notAllowedPage = mutableSetOf<Int>()

    return this.all { page ->
        val allowed = page !in notAllowedPage
        notAllowedPage.addAll(pages[page]?.previous ?: emptySet())
        allowed
    }
}

fun List<Int>.reordering(pages: Map<Int, Page>): List<Int> {
    var reordered = listOf<Int>()
    for (page in this) {
        val index = reordered.indexOfFirst { page in (pages[it]?.previous ?: emptySet()) }
        reordered = if (index == -1) reordered + page
        else reordered.take(index) + page + reordered.drop(index)
    }
    return reordered
}

fun List<Int>.getMiddleValue() = get(size / 2)
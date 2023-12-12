package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day12")
    val springs = input.map { it.toSpringRow() }

    println("Part 1: ${springs.sumOf{it.countValidCombination()}} ")
    println("Part 2: ${springs.map{it.unfold()}.sumOf{it.countValidCombination()}} ")

}

data class SpringRow(
    val springs: String,
    val groups: List<Int>
) {

    fun countValidCombination() = generateCombinations().count(::isValid)

    fun unfold() = SpringRow(List(5){springs}.joinToString ("?"), List(5){groups}.flatten())

    private fun generateCombinations(): List<String> {
        val count = 1 shl springs.count { it == '?' }
        val indices = springs.mapIndexedNotNull { i, c -> i.takeIf { c == '?' } }
        return List(count) { n ->
            val combination = springs.toMutableList()
            for (i in indices.indices) {
                combination[indices[i]] = if ((n shr i) and 1 == 1) '.' else '#'
            }
            combination.joinToString("")
        }
    }

    private fun isValid(row: String): Boolean {
        val combinationGroup = row.split("""\.+""".toRegex()).filterNot{it.isBlank()}.map { it.length }
        return combinationGroup == groups
    }


}

fun String.toSpringRow(): SpringRow {
    val (springs, groups) = this.split(" ")
    return SpringRow(springs, groups.split(",").map { it.toInt() })
}
package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day12")
    val springs = input.map { it.toSpringRow() }

//    springs[1].unfold().countValidCombination()


    "??#?#????#.... 3,2".toSpringRow().countValidCombination()
    var diff = 0
    springs.forEach {
        val old = it.countValidCombinationOld()
        val new = it.countValidCombination()
        if (old != new) {
            diff++
            println("${it.springs} ${it.groups.joinToString(",")}")
            println("${old} to ${new}")
        }
    }

    println("diff: $diff")
//    println("Part 1: ${springs.sumOf{it.countValidCombination()}} ")
//    println("Part 2: ${springs.map{it.unfold()}.sumOf{it.countValidCombination()}} ")
}

data class SpringRow(
    val springs: String,
    val groups: List<Int>,
) {
    fun unfold() = SpringRow(List(5) { springs }.joinToString("?"), List(5) { groups }.flatten())

    fun countValidCombinationOld() = generateCombinations().count(::isValid)
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
        val combinationGroup = row.split("""\.+""".toRegex()).filterNot { it.isBlank() }.map { it.length }
        return combinationGroup == groups
    }


    fun countValidCombination(): Int {
        val workingSprings = ".${springs}.".replace("""\.+""".toRegex(), ".")
        val groupPatterns = groups.map { ".${"".padStart(it, '#')}." }

        val groupCombinations = List(groupPatterns.size + 1) { MutableList(workingSprings.length) { 0 } }

        val firstSpring = workingSprings.takeWhile { it != '#' }.length
        repeat(firstSpring) {
            groupCombinations.first()[it] = 1
        }
        println( groupCombinations.first())

        val lastSprings = workingSprings.indexOfLast { it == '#' }
        var springSize = 0
        val remainsSpringsSize = workingSprings.reversed().mapIndexed { i, it ->
            if (it == '#') springSize = lastSprings - (workingSprings.length - 2 - i)
            springSize
        }.reversed()
        println(workingSprings)
        println(remainsSpringsSize)
        println(workingSprings)
        println(groupPatterns)
        for (i in groupPatterns.indices) {
            val currentPattern = groupPatterns[i]
            val currentGroupCombination = groupCombinations[i + 1]
            val prevGroupCombination = groupCombinations[i]
            val start = prevGroupCombination.indexOfFirst { it > 0 }
            val end = prevGroupCombination.indexOfLast { it > 0 }
            println("$currentPattern $prevGroupCombination")
            var prevCombination = 0
            var currentCombination = 0
            val leftGroupSize = groups.drop(i + 1).sumOf { it + 1 }
//            val remainingSpringsSize = groups.drop(i + 1).sum()

//            println(remainingSpringsSize)

            for (j in start..(workingSprings.length - currentPattern.length)) {
                val springToCompare = workingSprings.substring(j..<j + currentPattern.length)
                if (prevGroupCombination[j] > prevCombination)
                    prevCombination = prevGroupCombination[j]

                val fit = currentPattern.zip(springToCompare).all { (a, b) -> a == b || b == '?' }

                if (!fit && springToCompare.first() == '#' && j > end) break

                if (springToCompare.first() == '#')
                    currentCombination = 0

//                println( "$j -> ${remainsSprings[j]}")

                if (fit && remainsSpringsSize[j + currentPattern.length - 1] <= leftGroupSize) {
                    currentCombination += prevCombination
                    currentGroupCombination[j + currentPattern.length - 1] = currentCombination
                }
            }
//            println("$currentPattern $currentGroupCombination")

        }
        groupCombinations.forEach(::println)
        return groupCombinations.last().last { it > 0 }
    }
}

fun String.toSpringRow(): SpringRow {
    val (springs, groups) = this.split(" ")
    return SpringRow(springs, groups.split(",").map { it.toInt() })
}
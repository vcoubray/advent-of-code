package fr.vco.adventofcode.y2020


fun main() {
    val input = getInputReader("/inputDay10.txt")
    val adapters = input.readLines().map { it.toInt() }.sorted()

    var last = 0
    val diffs = mutableListOf<Int>()
    adapters.forEach {
        diffs.add(it - last)
        last = it
    }
    diffs.add(3)

//    println("0 ${adapters.joinToString(" ")} 141")
//    println(diffs.joinToString(" "))
    println("Part 1 : ${diffs.filter { it == 1 }.size * diffs.filter { it == 3 }.size}")

    var chain = 0
    var combinaisons = 1L
    diffs.forEach {
        if (it == 1) chain++
        else {
            combinaisons *= when (chain) {
                2 -> 2
                3 -> 4
                4 -> 7
                else -> 1
            }
            chain = 0
        }
    }
    println("Part 2 : $combinaisons")
}
package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day13")
    val patterns = input.split { it.isBlank() }

    println("Part 1: ${patterns.sumOf{it.reflectionScore()}}")

}

fun List<String>.reflectionScore(): Int {
    return findHorizontalReflection() * 100 + findVerticalReflection()
}

fun List<String>.findHorizontalReflection(): Int {
    for (i in 0..<this.size - 1) {
        var reflective = true
        for (j in 0..<Math.min(i+1, size-i-1)) {
            if (this[i+j+1] != this[i-j]) {
                reflective = false
                break
            }
        }
        if(reflective) return i+1
    }
    return 0
}

fun List<String>.findVerticalReflection(): Int {
    return transpose(this).findHorizontalReflection()
}
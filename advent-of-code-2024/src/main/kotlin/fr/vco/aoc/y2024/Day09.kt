package fr.vco.aoc.y2024

fun main() {
    val input = readText("Day09")

    val disk = input.mapNotNull { it.digitToIntOrNull() }
        .flatMapIndexed { i, size -> List(size) { if (i % 2 == 0) i / 2 else -1 } }

    println("Part 1 : ${disk.compacted().checksum()}")
    println("Part 2 : ${disk.compactedWithWholeFile().checksum()}")
}

fun List<Int>.compacted(): List<Int> {
    val disk = mutableListOf<Int>()

    var start = 0
    var end = this.size - 1

    while (start <= end) {
        if (this[start] >= 0) {
            disk.add(this[start])
            start++
        } else if (this[end] >= 0) {
            disk.add(this[end])
            start++
            end--
        } else {
            end--
        }
    }
    return disk
}

fun List<Int>.compactedWithWholeFile(): List<Int> {
    val disk = this.toMutableList()
    var end = disk.size - 1

    while (end >= 0) {
        val fileId = disk[end]
        if(fileId == -1) {
            end--
            continue
        }

        var fileSize = 0
        while (end >= 0 && disk[end] == fileId) {
            fileSize++
            end--
        }

        val freeSpaceIndex = disk.findIndexOfFreeSpace(0, end, fileSize)
        if(freeSpaceIndex != -1) {
            repeat(fileSize) { i ->
                disk[freeSpaceIndex + i] = fileId
                disk[end + 1 + i] = -1
            }
        }

    }
    return disk
}

fun List<Int>.findIndexOfFreeSpace(start: Int, end: Int, size: Int): Int {
    var freeSpaceSize = 0
    for (i in start .. end) {
        if (this[i] == -1) {
            freeSpaceSize++
        } else {
            freeSpaceSize = 0
        }
        if (freeSpaceSize >= size) {
            return i - freeSpaceSize + 1
        }
    }
    return -1

}

fun List<Int>.checksum() = this.foldIndexed(0L) { i, acc, a -> acc + if (a > 0) a * i else 0 }

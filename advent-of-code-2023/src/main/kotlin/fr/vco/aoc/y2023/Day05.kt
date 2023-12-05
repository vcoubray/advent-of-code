package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day05")

    val seeds = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }
    val seedRanges = seeds.chunked(2).map { (a, b) -> a until a + b }
    val mappers = input.drop(2).split { it.isBlank() }
        .map { it.toSeedMapper() }
        .associateBy { it.source }

    println("Part 1: ${seeds.minOf { mappers.chainMap("seed", it) }}")
    println("Part 2: ${seedRanges.minOf { mappers.chainMap("seed", it) }}")
}

fun List<String>.toSeedMapper(): SeedMapper {
    val (source, dest) = """^(.*)-to-(.*) map:$""".toRegex().find(this.first())!!.destructured
    val mappers = this.drop(1).map {
        val (destStart, sourceStart, rangeLength) = it.split(" ").map { n -> n.toLong() }
        sourceStart until sourceStart + rangeLength to destStart - sourceStart
    }
    return SeedMapper(source, dest, mappers.sortedBy { (range, _) -> range.first })
}

data class SeedMapper(
    val source: String,
    val destination: String,
    val mappers: List<Pair<LongRange, Long>>
) {
    fun toDest(value: Long): Long {
        return (mappers.firstOrNull { (range, _) -> value in range }?.second ?: 0) + value
    }

    fun toDest(range: LongRange): List<LongRange> {
        val nextRanges = mutableListOf<LongRange>()
        var remainRange = range
        for ((mapper, diff) in mappers) {
            when {
                remainRange.first in mapper && remainRange.last > mapper.last -> {
                    nextRanges.add((remainRange.first..mapper.last) + diff)
                    remainRange = (mapper.last + 1)..remainRange.last
                }

                remainRange.first < mapper.first && remainRange.last in mapper -> {
                    nextRanges.add((mapper.first..remainRange.last) + diff)
                    remainRange = remainRange.first..<mapper.first
                }

                remainRange.first >= mapper.first && remainRange.last <= mapper.last -> {
                    nextRanges.add(remainRange + diff)
                    return nextRanges
                }
            }
        }
        nextRanges.add(remainRange)
        return nextRanges
    }

    operator fun LongRange.plus(value: Long) = (first + value)..(last + value)
}

fun Map<String, SeedMapper>.chainMap(firstMapper: String, seed: Long): Long {
    var mapper = this[firstMapper]
    var value = seed
    while (mapper != null) {
        value = mapper.toDest(value)
        mapper = this[mapper.destination]
    }
    return value
}

fun Map<String, SeedMapper>.chainMap(firstMapper: String, seeds: LongRange): Long {
    var mapper = this[firstMapper]
    var ranges = listOf(seeds)
    while (mapper != null) {
        val currentMapper = mapper
        ranges = ranges.flatMap { currentMapper.toDest(it) }
        mapper = this[mapper.destination]
    }
    return ranges.minOf { it.first }
}
package fr.vco.aoc.y2023

fun main() {
    val input = readLines("Day05")

    val seeds = input.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

    val mappers = input.drop(2).split { it.isBlank() }
        .map { it.toSeedMapper() }
        .associateBy { it.source }

    println("Part 1: ${seeds.minOf{mappers.chainMap("seed", it)}}")

}

fun List<String>.toSeedMapper(): SeedMapper {
    val (source, dest) = """^(.*)-to-(.*) map:$""".toRegex().find(this.first())!!.destructured
    val mappers = this.drop(1).map {
        val (destStart, sourceStart, rangeLength) = it.split(" ").map { n -> n.toLong() }
        sourceStart until sourceStart + rangeLength to destStart - sourceStart
    }
    return SeedMapper(source, dest, mappers)
}

data class SeedMapper(
    val source: String,
    val destination: String,
    val mappers: List<Pair<LongRange, Long>>
) {
    fun toDest(value: Long): Long {
        return (mappers.firstOrNull { (range, _) -> value in range }?.second ?: 0) + value
    }
}

fun Map<String, SeedMapper>.chainMap(firstMapper: String, seed: Long) : Long {
    var mapper = this[firstMapper]
    var value = seed
    while (mapper != null) {
        value = mapper.toDest(value)
        mapper = this[mapper.destination]
    }
    return value
}
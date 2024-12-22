package fr.vco.aoc.y2024

fun main() {
    val input = readLines("Day22")
    val secrets = input.map { RandomPrice(it.toLong()) }

    println("Part 1: ${secrets.sumOf { it.next(2000) }}")

    val bestPrice = secrets.flatMap { it.getSequenceWithBestPrice() }
        .groupingBy { (sequence, _) -> sequence }.fold(0) { acc, (_, price) -> acc + price }
        .maxOf{ (_, price) -> price }
    println("Part 2: $bestPrice")

}

class RandomPrice(private var secret: Long) {

    private val prices = mutableListOf(secret.toPrice())

    fun next(): Long {
        mixAndPrune { s -> s * 64 }
        mixAndPrune { s -> s / 32 }
        mixAndPrune { s -> s * 2048 }
        return secret
    }

    private fun mixAndPrune(mix: (Long) -> Long) {
        secret = secret.xor(mix(secret)) % 16777216L
    }

    fun next(n: Int): Long {
        repeat(n) {
            next()
            prices.add(secret.toPrice())
        }
        return secret
    }

    private fun Long.toPrice() = this.toString().takeLast(1).toInt()

    fun getSequenceWithBestPrice(): List<Pair<String, Int>> {
        val changes = prices.windowed(2).map { (a, b) -> b - a }
        return changes.dropLast(1).asSequence()
            .windowed(4).map { it.joinToString(",") }
            .mapIndexed { i, it -> it to prices[i + 4] }
            .groupBy { (sequence, _) -> sequence }
            .map{(sequence, prices) -> sequence to prices.first().second}
            .toList()
    }
}
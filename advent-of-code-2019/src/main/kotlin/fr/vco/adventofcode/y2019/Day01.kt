package fr.vco.adventofcode.y2019

fun main() {

    val input = getInputReader("/inputDay01.txt")
    val fuels = input.readLines().map { it.toInt()/3-2}
    val totalFuel = fuels.sum()
    println("Part 1 : $totalFuel")

    val total2 = fuels.map{ totalFuel(it) }.sum()
    println("Part 2 : $total2")

}

fun totalFuel (fuel : Int) : Int {
    val fuelForFuel = fuel/3-2
    return if (fuelForFuel <=0) fuel
    else totalFuel(fuelForFuel) +fuel
}
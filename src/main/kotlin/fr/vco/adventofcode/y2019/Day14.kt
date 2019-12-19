package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader

fun main() {

    val input = getInputReader("/2019/inputDay14.txt")

    val recipes = mutableMapOf<Ingredient, List<Ingredient>>()
    input.readLines().forEach {
        val line = it.split(" => ")
        val result = Ingredient(line[1].split(" "))
        val recipe = line[0].split(", ").map{ing -> Ingredient(ing.split(" "))}

        recipes[result] = recipe
    }
    recipes.forEach{println(it)}





}


data class Ingredient( val quantity : Int,val chemical :String ) {

    constructor(ing : List<String>) : this( ing[0].toInt(), ing[1])

}
package fr.vco.adventofcode.y2019

import fr.vco.adventofcode.getInputReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.ceil

data class Recipe (val result : String, val quantity : Long , val ingredients : List<Ingredient>)

data class Ingredient( val quantity : Long,val chemical :String ) {
    constructor(ing : List<String>) : this( ing[0].toLong(), ing[1])
}

fun main() {

    val input = getInputReader("/2019/inputDay14.txt")
    val recipes = createRecipe(input)
    println("Part 1 : ${getOreQuantity(recipes, Ingredient(1, "FUEL"))}")

    var min =0L
    var max = 3000000L
    while (max > min) {
        val i = (max-min)/2+min
        val ore = getOreQuantity(recipes, Ingredient(i,"FUEL"))
        if (ore < 1_000_000_000_000) min = i
        else max = i-1
    }
    println("Part 2 : $min")
}


fun createRecipe ( input : InputStreamReader ) : List<Recipe> {
    val recipes = mutableListOf<Recipe>()
    input.readLines().forEach {
        val line = it.split(" => ")
        val result = Ingredient(line[1].split(" "))
        val recipe = line[0].split(", ").map{ing -> Ingredient(ing.split(" "))}

        recipes.add(Recipe(result.chemical, result.quantity, recipe))
    }
    return recipes
}

fun getOreQuantity(recipes : List<Recipe>, target : Ingredient) :Long {

    val required = LinkedList<Ingredient>()
    required.add(target)
    val remaining =  mutableMapOf<String, Long>()

    var oreCount = 0L
    while (required.isNotEmpty()) {
        val ing = required.poll()!!
        if(ing.chemical== "ORE") {
            oreCount+= ing.quantity
            continue
        }
        val recipe = recipes.filter { it.result == ing.chemical }.firstOrNull()!!
        val requireQte = ing.quantity - (remaining[ing.chemical]?:0)
        val recipeQte = ceil(requireQte.toFloat()/recipe.quantity).toInt()
        remaining[ing.chemical] = recipeQte*recipe.quantity - requireQte

        recipe.ingredients.forEach{
            required.add(Ingredient(it.quantity*recipeQte,it.chemical))
        }

    }
    return oreCount
}
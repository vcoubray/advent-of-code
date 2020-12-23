package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader


fun main() {
    val input = getInputReader("/2020/inputDay21.txt")
    val lines = input.readLines()

    val foods = lines.map{
        "([^\\(]*) (\\(contains (.*)\\))*".toRegex().find(it)?.groupValues
            ?.let{(_,ingredients,_, allergens) ->
                ingredients.split(" ") to allergens.split(", ")
            }
    }.filterNotNull().toMap()

    val allergens = mutableMapOf<String,Set<String>>()

    foods.forEach{ (key,value) ->
        value.forEach{allergen ->
            if (!allergens.containsKey(allergen)) allergens[allergen] = key.toMutableSet()
            allergens[allergen] = allergens[allergen]!!.intersect(key)
        }
    }

    val allergenIngredients = allergens.values.flatten().toSet()
    foods.keys.flatten().count{!allergenIngredients.contains(it)}
        .let{println("Part 1 : $it")}

    val dangerousIngredients = mutableMapOf<String,String>()
    allergens.forEach(::println)

    while (allergens.any{(key,_)-> !dangerousIngredients.containsKey(key) }) {
        allergens.map{(key,value)-> key to value.minus(dangerousIngredients.values)}
            .filter{(_, value)-> value.size == 1}
            .forEach{ (key, value) -> dangerousIngredients[key] = value.first()}
    }

    dangerousIngredients.keys.sorted()
        .joinToString (","){dangerousIngredients[it]!!}
        .let{println("Part 2 : $it")}
}

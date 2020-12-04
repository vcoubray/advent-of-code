package fr.vco.adventofcode.y2020

import fr.vco.adventofcode.getInputReader

fun main() {

    val input = getInputReader("/2020/inputDay04.txt")
    val passports = mutableListOf(Passport())
    input.readLines().forEach{
        if (it.isEmpty()) passports.add(Passport())
        else it.split(" ")
            .forEach{f -> f
                .split(":")
                .let{(k,v) -> passports.last().fields[k]=v}
            }
    }

    println("Part 1 : ${passports.count { it.isValid() }}")
}

class Passport{
    val fields = mutableMapOf<String,String>()

    fun isValid()= fields.keys.containsAll(listOf("byr","iyr","eyr","hgt","hcl","ecl","pid"))
}
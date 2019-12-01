package fr.vco.adventofcode

import java.io.InputStreamReader


fun getInputReader(filePath : String) : InputStreamReader =
    InputStreamReader(object {}.javaClass.getResource(filePath).openStream())
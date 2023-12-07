# AdventOfCode

My solutions in kotlin for the Advent of code challenges : https://adventofcode.com

* [Advent of Code 2019](./advent-of-code-2019)
* [Advent of Code 2020](./advent-of-code-2020)
* [Advent of Code 2021](./advent-of-code-2021)
* [Advent of Code 2022](./advent-of-code-2022)
* [Advent of Code 2023](./advent-of-code-2023)

## Retrieve personal inputs

The precompiled `aoc-plugin` plugin allow to retrieve automatically the personal inputs of a specified year.

Don't forget to exclude these inputs file from the repository with `.gitignore` – we should not post them publicly, as [Eric Wastl requested for](https://twitter.com/ericwastl/status/1465805354214830081).

### Apply plugin

```kts
// build.gradle.kts
plugins {
    `aoc-inputs`
}

apply<Aoc_inputs_gradle.AocPlugin>()
```

### Configure the plugin

```kts
configure<Aoc_inputs_gradle.AocExtension> {
    year = 2023
    dest = "${project.projectDir}/src/main/resources/inputs"
    session = "<AOC_SESSION_ID>"
}
```
The plugin will automatically add all available inputs files for the specified `year` in the folder `dest`.
The `session` can be retrieve from the cookies of your browser after login in adventofcode.com


### Usage
```shell
./gradlew <module>:loadAocInputs
```
if already present, the inputs files will be overridden 

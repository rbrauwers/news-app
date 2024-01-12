package com.rbrauwers.newsapp

import org.junit.Assert
import org.junit.Test

class MiscUnitTest {

    @Test
    fun addIsCorrect() {
        Assert.assertEquals(2 + 2, 4)
    }

    // Quick trials challenge
    private fun sortPackages(packages: MutableMap<String, List<String>>): List<String> {
        val sortedPackages = mutableListOf<String>()
        val size = packages.size

        for (i in 0..<size) {
            println("Iteraction $i")

            val iterator = packages.iterator()
            iterator.forEach { item ->
                val canAdd = canAdd(
                    pack = item.toPair(),
                    currentDependencies = sortedPackages
                )

                println("Item: $item sortedPackages: $sortedPackages canAdd: $canAdd")

                if (canAdd) {
                    sortedPackages.add(item.key)
                    iterator.remove()
                }
            }

            if (sortedPackages.size == size) {
                break
            }
        }

        return sortedPackages
    }

    private fun canAdd(
        pack: Pair<String, List<String>>,
        currentDependencies: List<String>
    ): Boolean {
        return currentDependencies.containsAll(pack.second)
    }

    @Test
    fun sortPackagesTest() {
        val input = mapOf(
            Pair("A", listOf("C", "D")),
            Pair("B", listOf("E")),
            Pair("C", listOf("D")),
            Pair("D", emptyList()),
            Pair("E", listOf("C")),
        )

        val output = listOf("D", "C", "E", "B", "A")
        val result = sortPackages(input.toMutableMap())

        println("Expected: $output")
        println("Result: $result")

        /*
        output.forEachIndexed { index, s ->
            Assert.assertEquals(s, result[index])
        }

         */
    }

}


/*
You are part of the technical team working on a new language and its ecosystem.
An important part of any ecosystem nowdays is having a dependency management tool.
Your task is to write an algorithm which lets the package manager know in which order
to install the packages.

Your code will receive as an argument the list of packages and theirs dependencies
and must return list of packages in the order they must be installed:

```
input = {
    "A": ["C", "D"],
    "B": ["E"],
    "C": ["D"],
    "D": [],
    "E": ["C"],
}
output = ["D", "C", "E", "B", "A"]

```
 */
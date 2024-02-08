package com.rbrauwers.newsapp

import androidx.compose.ui.util.fastReduce
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*
import kotlin.system.measureTimeMillis

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    suspend fun a(): Long {
        val d = 1000L
        delay(d)
        println("a executed")
        return d
    }

    suspend fun b(): Long {
        val d = 500L
        delay(d)
        println("b executed")
        return d
    }

    /**
     * a executed
     * b executed
     * Total time: 1500
     * Done
     */
    @Test
    fun c() = runTest {
        val t = measureTimeMillis {
            val a = a()
            val b = b()
            println("Total time: ${a + b}")
        }

        println("Done")
    }

    /**
     * b executed
     * a executed
     * Total time: 1500
     * Done
     */
    @Test
    fun d() = runTest {
        val t = measureTimeMillis {
            println("Measure start")
            val a = async { a() }
            val b = async { b() }
            val r1 = a.await()
            val r2 = b.await()
            println("Total time: ${r1 + r2}")
        }

        println("Done")
    }

    fun e() {
        val user = User(name = "John")

        // Results in Person
        val let = user.let { u -> Person(name = u.name) }
        val run = user.run { Person(name = this.name) }

        // Results in User
        val apply = user.apply { Person(name = this.name) }
        val also = user.also { Person(name = it.name) }
    }

    fun uniqueStrings(items: List<*>): Set<String> {
        val strings = mutableSetOf<String>()

        items.forEach {
            when (it) {
                is String -> strings.add(it)
                is List<*> -> strings += uniqueStrings(it)
            }
        }

        return strings
    }

    /*
    fun checkConstrain(parent: Any): List<String> {
        val memberProperties = parent::class.memberProperties
        var result = memberProperties
            .filter { hasAnnotation(it) && propertyIsNull(it, parent) }
            .map { formatResult(parent, it) }
        memberProperties.filter { memberIsDataClass(it) }
            .mapNotNull { getMemberPropertyInstance(parent, it) }
            .forEach { result += checkConstrain(it) }
        return result
    }
     */


    @Test
    fun f() {
        //val items = listOf<Any>(1, "a", listOf("b", "c", 3), "b")
        val items = listOf<Any>(1, "a", listOf("b", "c", 3), "b", listOf("b", "f", 3, listOf(9, "g", "b")))
        val strings = uniqueStrings(items)
    }

    @Test
    fun associatedValuesTest() {
        val result = associatedValues(items = listOf(
            Pair("a", 1),
            Pair("b", 2),
            Pair("a", 100),
            Pair("b", 200),
            Pair("a", 10),
            Pair("b", 20),
        ))
        println("Assciated values: $result")
    }

    fun associatedValues(items: List<Pair<String, Int>>) : Map<String, List<Int>> {
        return items
            .groupBy { it.first }
            .mapValues {
                it.value.map { it.second }
            }
    }

    @Test
    fun g() {
        val list = listOf("a", "b", "c", "a")
        val reduce = list.reduce { acc, s -> "$acc+$s"}
        println("Reduce: $reduce")

        val numbers = listOf(1, 100, 200, 300)
        val index = numbers.binarySearch {
            when {
                it == 200 -> 0
                it < 200 -> -1
                else -> 1
            }
        }

        println("Binary search index: $index")

        val dropLast = numbers.dropLast(2)
        val dropLastWhile = numbers.dropLastWhile { it > 100 }
        println("Drop last: $dropLast $numbers")
        println("Drop last while: $dropLastWhile $numbers")

        val partition = numbers.partition { it <= 100 }
        println("Partition: $partition")

        val windows = numbers.windowed(size = 2)
        println("Windows: $windows")

        val slice = numbers.slice(0..2)
        println("Slice: $slice")
    }

    fun encodeString(input: String): String {
        var output = ""
        var char: Char? = null
        var count: Int? = null

        input.forEachIndexed{ index, c ->
            if (c != char) {
                char?.let {
                    output += char
                }

                count?.let {
                    output += count
                }

                char = c
                count = 1
            } else {
                count = count?.plus(1)
            }

            if (index == input.lastIndex && input.length > 1) {
                char?.let {
                    output += char
                }

                count?.let {
                    output += count
                }
            }
        }

        return output
    }

    @Test
    fun h() {
        val input = "aaabbccccd"
    }

    fun reminders(number: Int) : Int {
        var result = 0

        number.toString().forEach { n ->
            val x = n.digitToInt()
            val r = number.rem(x)

            println("n: $n")
            println("x: $x")
            println("r: $r")

            if (r == 0) {
                result++
            }
        }

        return result
    }

    @Test
    fun l() {
        println(reminders(232))
    }

    data class User(val name: String)
    data class Person(val name: String)

    open class Base

    class Detail : Base() {

        fun x() {
            runBlocking {
                async {  }
            }
        }

    }

    val longFlow = MutableStateFlow("")

    /**
     * Will not block the main thread.
     * Reason: delay is a suspend function (it calls suspendCancellableCoroutine).
     */
    /*
    fun longOperation() {
        viewModelScope.launch {
            println("Long operation started")
            delay(10000)
            println("Long operation finished")
            longFlow.value = "Long operation completed"
        }
    }
     */

    /**
     * Will block the main thread.
     */
    /*
    fun longOperation() {
        viewModelScope.launch {
            println("Long operation started")
            val x = longComputation()
            println("Long operation finished")
            longFlow.value = "Long operation completed"
        }
    }
     */

    /**
     * Will not block the main thread.
     * Regardless of being executed on IO context, the flow will be collected at Main thread.
     */
    /*
    fun longOperation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                println("Long operation started")
                val x = longComputation()
                println("Long operation finished")
                longFlow.value = "Long operation completed"
            }
        }
    }
     */

    private suspend fun longComputation(): Long {
        var x = 0L
        for (i in 0..10000000000L) {
            x = i
        }
        return x
    }

}
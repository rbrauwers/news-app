package com.rbrauwers.newsapp

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
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

    @Test
    fun c() = runTest {
        val t = measureTimeMillis {
            val a = a()
            val b = b()
            println("Total time: ${a + b}")
        }

        println("Done")
    }

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

}
package com.rbrauwers.newsapp

import org.junit.Test

class TaxTest {

    @Test
    fun main() {
        val salaries = listOf(
            1000f,
            5000f,
            10000f,
            15000f,
            50000f,
            80000f,
            200000f
        )

        salaries.forEach { salary ->
            calculateTax(salary)
        }
    }

    val rates = listOf(
        Rate(from = 0f, to = 10000f, rate = 5f),
        Rate(from = 10000f, to = 20000f, rate = 10f),
        Rate(from = 20000f, to = 50000f, rate = 15f),
        Rate(from = 50000f, to = 100000f, rate = 20f),
        Rate(from = 100000f, to = null, rate = 25f)
    )

    fun calculateTax(salary: Float): Float? {
        val rateToApply = rates.firstOrNull { rate ->
            rate.canBeAppliedTo(salary = salary)
        }

        val result = rateToApply?.calcTax(salary = salary)
        println(result?.second)

        return result?.first
    }

    data class Rate(val from: Float, val to: Float?, val rate: Float) {

        fun canBeAppliedTo(salary: Float) : Boolean {
            return salary > from && (to == null || salary <= to )
        }

        fun calcTax(salary: Float) : Pair<Float, String> {
            val tax = salary * (rate / 100.0f)
            val description = "[Salary: $salary] [Tax = $salary * ${(rate / 100.0)} = $tax] [Rate applied: $this]"
            return Pair(tax, description)
        }

    }

}
package com.example

import com.example.utils.BraSizeCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun braSizeCalculation_78cm_88cm_returns_34B() {
        val result = BraSizeCalculator.calculateBraSize(78.0, 88.0, "cm")
        assertEquals(34, result.bandSize)
        assertEquals("B", result.cupSize)
        assertEquals("34B", result.fullSize)
        assertEquals(listOf("32B", "36B"), result.sisterSizes)
        assertEquals(78, result.underbustCm.toInt())
        assertEquals(88, result.bustCm.toInt())
    }
}

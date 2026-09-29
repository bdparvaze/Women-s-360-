package com.example.utils

import kotlin.math.roundToInt

data class BraResult(
    val bandSize: Int,
    val cupSize: String,
    val fullSize: String,
    val sisterSizes: List<String>,
    val underbustCm: Double,
    val bustCm: Double
)

object BraSizeCalculator {
    fun calculateBraSize(underbustCm: Double, bustCm: Double, unit: String): BraResult {
        var under = underbustCm
        var bust = bustCm
        if (unit.equals("inch", ignoreCase = true) || unit.equals("in", ignoreCase = true)) {
            under *= 2.54
            bust *= 2.54
        }

        val rawBandInch = (under / 2.54).roundToInt()
        // Standard even number band calculation (+3 for odd, +2 for even; e.g. 78cm -> 31 -> 34)
        val bandInch = if (rawBandInch % 2 != 0) rawBandInch + 3 else rawBandInch + 2

        val diffCm = (bust - under).coerceAtLeast(0.0)
        val diffInch = ((diffCm / 2.54) - 2.0).coerceAtLeast(0.0)

        val cupSize = when {
            diffInch < 0.5 -> "AA"
            diffInch < 1.5 -> "A"
            diffInch < 2.5 -> "B"
            diffInch < 3.5 -> "C"
            diffInch < 4.5 -> "D"
            diffInch < 5.5 -> "DD"
            diffInch < 6.5 -> "E"
            diffInch < 7.5 -> "F"
            else -> "G"
        }

        val sisterSizes = listOf("${bandInch - 2}$cupSize", "${bandInch + 2}$cupSize")

        return BraResult(
            bandSize = bandInch,
            cupSize = cupSize,
            fullSize = "$bandInch$cupSize",
            sisterSizes = sisterSizes,
            underbustCm = under,
            bustCm = bust
        )
    }
}

package com.rroyops.unitconverter.model

/**
 * Pure conversion logic. No Android imports on purpose — this class can be
 * unit-tested with plain JUnit, no emulator required.
 *
 * Length and weight use a "convert to base unit, then to target unit" factor
 * approach. Temperature can't use a simple multiplier (it has an offset,
 * not just a scale) so it gets its own formula-based path.
 */
object ConverterEngine {

    // Base unit for length = meters. Every factor answers: "how many meters is 1 of this unit?"
    private val lengthToMeters = mapOf(
        "mm" to 0.001,
        "cm" to 0.01,
        "m" to 1.0,
        "km" to 1000.0,
        "in" to 0.0254,
        "ft" to 0.3048,
        "mi" to 1609.344
    )

    // Base unit for weight = grams.
    private val weightToGrams = mapOf(
        "mg" to 0.001,
        "g" to 1.0,
        "kg" to 1000.0,
        "lb" to 453.59237,
        "oz" to 28.349523125
    )

    fun convert(
        category: ConversionCategory,
        fromUnitId: String,
        toUnitId: String,
        value: Double
    ): Double {
        if (fromUnitId == toUnitId) return value

        return when (category) {
            ConversionCategory.LENGTH -> convertViaBase(value, fromUnitId, toUnitId, lengthToMeters)
            ConversionCategory.WEIGHT -> convertViaBase(value, fromUnitId, toUnitId, weightToGrams)
            ConversionCategory.TEMPERATURE -> convertTemperature(value, fromUnitId, toUnitId)
        }
    }

    private fun convertViaBase(
        value: Double,
        fromUnitId: String,
        toUnitId: String,
        toBaseFactor: Map<String, Double>
    ): Double {
        val fromFactor = toBaseFactor[fromUnitId]
            ?: error("Unknown unit id: $fromUnitId")
        val toFactor = toBaseFactor[toUnitId]
            ?: error("Unknown unit id: $toUnitId")

        val valueInBaseUnit = value * fromFactor
        return valueInBaseUnit / toFactor
    }

    private fun convertTemperature(value: Double, fromUnitId: String, toUnitId: String): Double {
        // Normalize to Celsius first, then convert from Celsius to the target.
        val celsius = when (fromUnitId) {
            "c" -> value
            "f" -> (value - 32.0) * 5.0 / 9.0
            "k" -> value - 273.15
            else -> error("Unknown temperature unit id: $fromUnitId")
        }

        return when (toUnitId) {
            "c" -> celsius
            "f" -> celsius * 9.0 / 5.0 + 32.0
            "k" -> celsius + 273.15
            else -> error("Unknown temperature unit id: $toUnitId")
        }
    }
}

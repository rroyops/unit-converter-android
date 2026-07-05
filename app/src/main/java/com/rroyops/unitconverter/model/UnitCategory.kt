package com.rroyops.unitconverter.model

/**
 * All supported conversion categories.
 * Adding a new category (e.g. Volume) means: add a value here,
 * add its units below, and add its conversion case in ConverterEngine.
 * Everything else (UI, ViewModel) adapts automatically.
 */
enum class ConversionCategory(val displayName: String) {
    LENGTH("Length"),
    WEIGHT("Weight"),
    TEMPERATURE("Temperature")
}

/**
 * A single unit within a category, e.g. "Meter" inside LENGTH.
 * `symbol` is what's shown in the compact UI (m, kg, °C).
 */
data class UnitOption(
    val id: String,
    val displayName: String,
    val symbol: String
)

object UnitCatalog {

    val length = listOf(
        UnitOption("mm", "Millimeter", "mm"),
        UnitOption("cm", "Centimeter", "cm"),
        UnitOption("m", "Meter", "m"),
        UnitOption("km", "Kilometer", "km"),
        UnitOption("in", "Inch", "in"),
        UnitOption("ft", "Foot", "ft"),
        UnitOption("mi", "Mile", "mi")
    )

    val weight = listOf(
        UnitOption("mg", "Milligram", "mg"),
        UnitOption("g", "Gram", "g"),
        UnitOption("kg", "Kilogram", "kg"),
        UnitOption("lb", "Pound", "lb"),
        UnitOption("oz", "Ounce", "oz")
    )

    val temperature = listOf(
        UnitOption("c", "Celsius", "°C"),
        UnitOption("f", "Fahrenheit", "°F"),
        UnitOption("k", "Kelvin", "K")
    )

    fun unitsFor(category: ConversionCategory): List<UnitOption> = when (category) {
        ConversionCategory.LENGTH -> length
        ConversionCategory.WEIGHT -> weight
        ConversionCategory.TEMPERATURE -> temperature
    }
}

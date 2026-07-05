package com.rroyops.unitconverter.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ConverterEngineTest {

    private val delta = 0.0001

    @Test
    fun `meters to kilometers converts correctly`() {
        val result = ConverterEngine.convert(ConversionCategory.LENGTH, "m", "km", 1500.0)
        assertEquals(1.5, result, delta)
    }

    @Test
    fun `inches to centimeters converts correctly`() {
        val result = ConverterEngine.convert(ConversionCategory.LENGTH, "in", "cm", 1.0)
        assertEquals(2.54, result, delta)
    }

    @Test
    fun `same unit returns same value`() {
        val result = ConverterEngine.convert(ConversionCategory.LENGTH, "m", "m", 42.0)
        assertEquals(42.0, result, delta)
    }

    @Test
    fun `kilograms to pounds converts correctly`() {
        val result = ConverterEngine.convert(ConversionCategory.WEIGHT, "kg", "lb", 1.0)
        assertEquals(2.20462, result, 0.001)
    }

    @Test
    fun `celsius to fahrenheit converts correctly`() {
        val result = ConverterEngine.convert(ConversionCategory.TEMPERATURE, "c", "f", 100.0)
        assertEquals(212.0, result, delta)
    }

    @Test
    fun `celsius to kelvin converts correctly`() {
        val result = ConverterEngine.convert(ConversionCategory.TEMPERATURE, "c", "k", 0.0)
        assertEquals(273.15, result, delta)
    }

    @Test
    fun `fahrenheit to celsius handles freezing point`() {
        val result = ConverterEngine.convert(ConversionCategory.TEMPERATURE, "f", "c", 32.0)
        assertEquals(0.0, result, delta)
    }

    @Test
    fun `negative temperature converts correctly`() {
        val result = ConverterEngine.convert(ConversionCategory.TEMPERATURE, "c", "f", -40.0)
        assertEquals(-40.0, result, delta)
    }
}

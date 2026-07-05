package com.rroyops.unitconverter.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rroyops.unitconverter.model.ConversionCategory
import com.rroyops.unitconverter.model.ConverterEngine
import com.rroyops.unitconverter.model.UnitCatalog
import com.rroyops.unitconverter.model.UnitOption

/**
 * Single source of truth for the converter screen. The Composable reads this
 * state and calls back into it on user input — it does not do any math itself.
 */
class ConverterViewModel : ViewModel() {

    var category by mutableStateOf(ConversionCategory.LENGTH)
        private set

    var fromUnit by mutableStateOf(UnitCatalog.unitsFor(ConversionCategory.LENGTH).first())
        private set

    var toUnit by mutableStateOf(UnitCatalog.unitsFor(ConversionCategory.LENGTH)[1])
        private set

    var inputText by mutableStateOf("")
        private set

    /** null when input is empty/invalid, so the UI can show an error instead of "0" */
    var errorMessage: String? by mutableStateOf(null)
        private set

    var result: Double? by mutableStateOf(null)
        private set

    fun unitsForCurrentCategory(): List<UnitOption> = UnitCatalog.unitsFor(category)

    fun onCategorySelected(newCategory: ConversionCategory) {
        category = newCategory
        val units = UnitCatalog.unitsFor(newCategory)
        fromUnit = units[0]
        toUnit = units[1]
        recalculate()
    }

    fun onFromUnitSelected(unit: UnitOption) {
        fromUnit = unit
        recalculate()
    }

    fun onToUnitSelected(unit: UnitOption) {
        toUnit = unit
        recalculate()
    }

    fun onInputChanged(newText: String) {
        // Allow only digits, at most one decimal point, and an optional leading minus.
        val isValidPartialNumber = newText.isEmpty() ||
            newText.matches(Regex("^-?\\d*\\.?\\d*$"))

        if (isValidPartialNumber) {
            inputText = newText
            recalculate()
        }
    }

    fun swapUnits() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        recalculate()
    }

    private fun recalculate() {
        val numericValue = inputText.toDoubleOrNull()

        if (inputText.isBlank()) {
            result = null
            errorMessage = null
            return
        }

        if (numericValue == null) {
            result = null
            errorMessage = "Enter a valid number"
            return
        }

        errorMessage = null
        result = ConverterEngine.convert(
            category = category,
            fromUnitId = fromUnit.id,
            toUnitId = toUnit.id,
            value = numericValue
        )
    }
}

package com.rroyops.unitconverter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rroyops.unitconverter.model.ConversionCategory
import com.rroyops.unitconverter.model.UnitOption
import com.rroyops.unitconverter.viewmodel.ConverterViewModel
import java.util.Locale

@Composable
fun ConverterScreen(viewModel: ConverterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Unit Converter") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CategorySelector(
                selected = viewModel.category,
                onSelect = viewModel::onCategorySelected
            )

            UnitPickerRow(
                units = viewModel.unitsForCurrentCategory(),
                fromUnit = viewModel.fromUnit,
                toUnit = viewModel.toUnit,
                onFromSelected = viewModel::onFromUnitSelected,
                onToSelected = viewModel::onToUnitSelected,
                onSwap = viewModel::swapUnits
            )

            OutlinedTextField(
                value = viewModel.inputText,
                onValueChange = viewModel::onInputChanged,
                label = { Text("Value in ${viewModel.fromUnit.displayName}") },
                isError = viewModel.errorMessage != null,
                supportingText = {
                    viewModel.errorMessage?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            ResultCard(
                resultValue = viewModel.result,
                unit = viewModel.toUnit
            )
        }
    }
}

@Composable
private fun CategorySelector(
    selected: ConversionCategory,
    onSelect: (ConversionCategory) -> Unit
) {
    Column {
        Text("Category", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ConversionCategory.values().forEach { category ->
                FilterChip(
                    selected = category == selected,
                    onClick = { onSelect(category) },
                    label = { Text(category.displayName) }
                )
            }
        }
    }
}

@Composable
private fun UnitPickerRow(
    units: List<UnitOption>,
    fromUnit: UnitOption,
    toUnit: UnitOption,
    onFromSelected: (UnitOption) -> Unit,
    onToSelected: (UnitOption) -> Unit,
    onSwap: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UnitDropdown(
            modifier = Modifier.weight(1f),
            label = "From",
            units = units,
            selected = fromUnit,
            onSelected = onFromSelected
        )

        IconButton(onClick = onSwap) {
            Icon(Icons.Filled.SwapVert, contentDescription = "Swap units")
        }

        UnitDropdown(
            modifier = Modifier.weight(1f),
            label = "To",
            units = units,
            selected = toUnit,
            onSelected = onToSelected
        )
    }
}

@Composable
private fun UnitDropdown(
    modifier: Modifier = Modifier,
    label: String,
    units: List<UnitOption>,
    selected: UnitOption,
    onSelected: (UnitOption) -> Unit
) {
    var expanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("$label: ${selected.symbol}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text("${unit.displayName} (${unit.symbol})") },
                    onClick = {
                        onSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ResultCard(resultValue: Double?, unit: UnitOption) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Result", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            val displayText = if (resultValue != null) {
                "${formatResult(resultValue)} ${unit.symbol}"
            } else {
                "—"
            }
            Text(
                displayText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatResult(value: Double): String {
    // Trim trailing zeros but keep up to 6 decimal places of precision.
    return String.format(Locale.US, "%.6f", value)
        .trimEnd('0')
        .trimEnd('.')
        .ifEmpty { "0" }
}

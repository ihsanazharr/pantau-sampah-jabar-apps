package com.example.opendatajabar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuFilter(
    selectedFilter: String,
    options: List<String>,
    onFilterSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedFilter,
            onValueChange = {},
            label = {
                Text(
                    "Pilih Kabupaten/Kota",
                    color = MaterialTheme.colorScheme.onError
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onError,
                unfocusedTextColor = MaterialTheme.colorScheme.onError,
                focusedBorderColor = MaterialTheme.colorScheme.onError,
                unfocusedBorderColor = MaterialTheme.colorScheme.onError,
                cursorColor = MaterialTheme.colorScheme.onError
            ),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onError),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = MaterialTheme.colorScheme.onError) },
                    onClick = {
                        onFilterSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.onTertiary)
                )
            }
        }
    }
}

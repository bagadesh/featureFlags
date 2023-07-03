package com.bagadesh.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagadesh.ui.entity.UiFeature
import com.bagadesh.ui.entity.UiVariable

/**
 * Created by bagadesh on 28/06/23.
 */

@Composable
internal fun VariableScreen(
    modifier: Modifier = Modifier,
    variables: List<UiVariable>,
    feature: UiFeature,
    errorMap: Map<String, UiVariable>,
    onVariableValueChange: (String, UiVariable, String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = "Feature Name = ${feature.key}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        items(variables, { it.key }) { variable ->
            val isError = errorMap.containsKey(variable.key)
            VariableUi(
                modifier = Modifier
                    .fillMaxWidth(),
                name = variable.key,
                value = variable.value,
                valueType = variable.valueType,
                onValueChange = {
                    onVariableValueChange(feature.key, variable, it)
                },
                isError = isError
            )
        }
    }
}

@Composable
internal fun VariableUi(
    modifier: Modifier = Modifier, name: String, value: String, valueType: String, onValueChange: (String) -> Unit,
    isError: Boolean
) {
    Column(modifier = modifier) {
        Text(
            text = "Type: $valueType",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(10.dp)
        )
        var maintainedValue by remember { mutableStateOf(value) }
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = maintainedValue,
            onValueChange = {
                maintainedValue = it
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            label = {
                Text(text = name)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            isError = isError,
            keyboardActions = KeyboardActions(
                onDone = {
                    onValueChange(maintainedValue)
                    focusManager.clearFocus()
                }
            )
        )
        AnimatedVisibility(visible = isError) {
            Text(
                text = "Entered value is incorrect",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Divider(modifier = Modifier.padding(20.dp))
    }
}

@Preview
@Composable
fun VariableUiPreview() {
    VariableUi(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        name = "search_configuration",
        value = "2",
        valueType = "integer",
        onValueChange = {},
        isError = true
    )
}
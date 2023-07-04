@file:OptIn(ExperimentalCoroutinesApi::class)

package com.featureflags.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.featureflags.core.contract.Reader
import com.featureflags.core.contract.Writer
import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.request.ChangeVariableRequest
import com.featureflags.ui.entity.UiErrorVariable
import com.featureflags.ui.entity.UiVariable
import com.featureflags.ui.mapper.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Created by bagadesh on 26/06/23.
 */
class FeatureFlagViewModel constructor(
    reader: com.featureflags.core.contract.Reader,
    private val writer: com.featureflags.core.contract.Writer
) : ViewModel() {

    val features = reader
        .getFeatures()
        .mapLatest { features -> features.map { it.toUi() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var errorState = MutableStateFlow<Map<String, UiErrorVariable>>(emptyMap())
        private set

    fun onChange(key: String, isEnabled: Boolean) {
        viewModelScope.launch { writer.enableFeature(key = key, enabled = isEnabled, source = com.featureflags.core.entity.Source.USER) }
    }

    fun onVariableValueChange(featureKey: String, variable: UiVariable, value: String) {
        viewModelScope.launch {
            val result = writer.changeVariableValue(
                request = ChangeVariableRequest(
                    featureKey = featureKey,
                    variableKey = variable.key,
                    currentValue = variable.value,
                    changedValue = value,
                    valueType = variable.valueType,
                ),
                source = com.featureflags.core.entity.Source.USER
            )
            when (result) {
                is com.featureflags.core.entity.VariableSaveState.Error -> {
                    addErrorState(
                        featureKey,
                        variable
                    )
                }

                com.featureflags.core.entity.VariableSaveState.Saved -> {
                    removeErrorStateIfAvailable(featureKey, variable.key)
                }
            }
        }
    }

    private fun removeErrorStateIfAvailable(featureKey: String, key: String) {
        if (!errorState.value.containsKey(featureKey)) return
        val variableList = errorState.value[featureKey]!!.variables
        if (variableList.isEmpty()) return
        if (!variableList.containsKey(key)) return


        val modifiedVariables = variableList.toMutableMap().apply { remove(key) }
        errorState.value = errorState.value
            .toMutableMap().apply {
                put(featureKey, UiErrorVariable(variables = modifiedVariables))
            }
    }

    private fun addErrorState(featureKey: String, variable: UiVariable) {
        val variableList = (errorState.value[featureKey]?.variables?.toMutableMap() ?: mutableMapOf()).apply {
            put(variable.key, variable)
        }
        errorState.value = errorState.value
            .toMutableMap().apply {
                put(
                    featureKey,
                    UiErrorVariable(variableList)
                )
            }
    }
}
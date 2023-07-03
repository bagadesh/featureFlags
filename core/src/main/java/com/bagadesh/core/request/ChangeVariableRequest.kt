package com.bagadesh.core.request

/**
 * Created by bagadesh on 27/06/23.
 */
data class ChangeVariableRequest(
    val featureKey: String,
    val variableKey: String,
    val currentValue: String,
    val changedValue: String,
    val valueType: String
)
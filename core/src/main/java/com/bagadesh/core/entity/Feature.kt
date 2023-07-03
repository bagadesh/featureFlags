package com.bagadesh.core.entity

/**
 * Created by bagadesh on 26/06/23.
 */
data class Feature(
    val key: String,
    val isEnabled: Boolean,
    val description: String = "",
    val variableList: List<Variable>
)

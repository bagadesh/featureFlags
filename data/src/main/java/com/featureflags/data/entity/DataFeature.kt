package com.featureflags.data.entity

/**
 * Created by bagadesh on 26/06/23.
 */
data class DataFeature (
    val key: String,
    val isEnabled: Boolean,
    val description: String = "",
    val variableList: List<DataVariable>
)
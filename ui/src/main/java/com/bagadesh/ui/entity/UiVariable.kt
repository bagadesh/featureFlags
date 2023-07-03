package com.bagadesh.ui.entity

import androidx.compose.runtime.Stable

/**
 * Created by bagadesh on 27/06/23.
 */
@Stable
data class UiVariable(
    val key: String,
    val value: String,
    val valueType: String
)

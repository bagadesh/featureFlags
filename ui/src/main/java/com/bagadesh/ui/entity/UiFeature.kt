package com.bagadesh.ui.entity

import androidx.compose.runtime.Stable

/**
 * Created by bagadesh on 26/06/23.
 */
@Stable
data class UiFeature(
    val key: String,
    val isEnabled: Boolean,
    val description: String,
    val variables: List<UiVariable>
)

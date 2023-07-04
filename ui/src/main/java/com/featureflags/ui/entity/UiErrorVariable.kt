package com.featureflags.ui.entity

import androidx.compose.runtime.Stable

/**
 * Created by bagadesh on 28/06/23.
 */

@Stable
data class UiErrorVariable(
    val variables: Map<String, UiVariable>
)

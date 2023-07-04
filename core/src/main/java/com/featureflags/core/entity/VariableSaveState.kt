package com.featureflags.core.entity

/**
 * Created by bagadesh on 28/06/23.
 */
sealed interface VariableSaveState {

    object Saved : com.featureflags.core.entity.VariableSaveState

    data class Error(val message: String) : com.featureflags.core.entity.VariableSaveState

}
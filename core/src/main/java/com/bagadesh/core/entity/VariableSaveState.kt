package com.bagadesh.core.entity

/**
 * Created by bagadesh on 28/06/23.
 */
sealed interface VariableSaveState {

    object Saved : VariableSaveState

    data class Error(val message: String) : VariableSaveState

}
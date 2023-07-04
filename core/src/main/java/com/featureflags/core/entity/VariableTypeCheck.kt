package com.featureflags.core.entity

/**
 * Created by bagadesh on 01/07/23.
 */
sealed interface VariableTypeCheck {

    data class Success(val modifiedValue: String) : VariableTypeCheck

    data class Failed(val message: String) : VariableTypeCheck

}
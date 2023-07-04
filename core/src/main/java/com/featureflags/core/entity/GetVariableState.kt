package com.featureflags.core.entity

/**
 * Created by bagadesh on 02/07/23.
 */
sealed interface GetVariableState {

    data class Success(val variable: com.featureflags.core.entity.Variable): com.featureflags.core.entity.GetVariableState

    object Failed: com.featureflags.core.entity.GetVariableState

}
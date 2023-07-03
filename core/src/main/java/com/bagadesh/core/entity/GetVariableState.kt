package com.bagadesh.core.entity

/**
 * Created by bagadesh on 02/07/23.
 */
sealed interface GetVariableState {

    data class Success(val variable: Variable): GetVariableState

    object Failed: GetVariableState

}
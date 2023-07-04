package com.featureflags.core.repository

import com.featureflags.core.entity.VariableTypeCheck

/**
 * Created by bagadesh on 01/07/23.
 */
interface VariableCheckerRepository {


    fun verifyType(value: String, valueType: String): VariableTypeCheck

}
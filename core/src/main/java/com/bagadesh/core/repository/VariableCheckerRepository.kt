package com.bagadesh.core.repository

import com.bagadesh.core.entity.VariableTypeCheck

/**
 * Created by bagadesh on 01/07/23.
 */
interface VariableCheckerRepository {


    fun verifyType(value: String, valueType: String): VariableTypeCheck

}
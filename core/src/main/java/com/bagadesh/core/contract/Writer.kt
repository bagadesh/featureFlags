package com.bagadesh.core.contract

import com.bagadesh.core.entity.Source
import com.bagadesh.core.entity.VariableSaveState
import com.bagadesh.core.request.ChangeVariableRequest

/**
 * Created by bagadesh on 26/06/23.
 */
interface Writer {

    suspend fun enableFeature(key: String, enabled: Boolean, source: Source)

    suspend fun changeVariableValue(request: ChangeVariableRequest, source: Source): VariableSaveState
}
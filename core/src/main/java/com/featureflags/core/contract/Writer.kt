package com.featureflags.core.contract

import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.request.ChangeVariableRequest

/**
 * Created by bagadesh on 26/06/23.
 */
interface Writer {

    suspend fun enableFeature(key: String, enabled: Boolean, source: Source)

    suspend fun changeVariableValue(request: ChangeVariableRequest, source: Source): VariableSaveState
}
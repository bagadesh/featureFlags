package com.bagadesh.core.contract.impl

import com.bagadesh.core.contract.Writer
import com.bagadesh.core.entity.Source
import com.bagadesh.core.entity.VariableSaveState
import com.bagadesh.core.request.ChangeVariableRequest
import com.bagadesh.core.usecases.ChangeVariableUseCase
import com.bagadesh.core.usecases.EnableFeatureUseCase

/**
 * Created by bagadesh on 26/06/23.
 */
class WriterImpl constructor(
    private val enableFeatureUseCase: EnableFeatureUseCase,
    private val changeVariableUseCase: ChangeVariableUseCase,
) : Writer {

    override suspend fun enableFeature(key: String, enabled: Boolean, source: Source) =
        enableFeatureUseCase.enable(key = key, enabled = enabled, source = source)

    override suspend fun changeVariableValue(request: ChangeVariableRequest, source: Source): VariableSaveState {
        return changeVariableUseCase.changeVariable(request = request, source = source)
    }
}
package com.featureflags.core.contract.impl

import com.featureflags.core.contract.Writer
import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.request.ChangeVariableRequest
import com.featureflags.core.usecases.ChangeVariableUseCase
import com.featureflags.core.usecases.EnableFeatureUseCase

/**
 * Created by bagadesh on 26/06/23.
 */
class WriterImpl constructor(
    private val enableFeatureUseCase: EnableFeatureUseCase,
    private val changeVariableUseCase: ChangeVariableUseCase,
) : com.featureflags.core.contract.Writer {

    override suspend fun enableFeature(key: String, enabled: Boolean, source: com.featureflags.core.entity.Source) =
        enableFeatureUseCase.enable(key = key, enabled = enabled, source = source)

    override suspend fun changeVariableValue(request: ChangeVariableRequest, source: com.featureflags.core.entity.Source): com.featureflags.core.entity.VariableSaveState {
        return changeVariableUseCase.changeVariable(request = request, source = source)
    }
}
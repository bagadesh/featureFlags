package com.featureflags.core.usecases

import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.repository.FeatureRepository
import com.featureflags.core.request.ChangeVariableRequest

/**
 * Created by bagadesh on 27/06/23.
 */
class ChangeVariableUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun changeVariable(request: ChangeVariableRequest, source: com.featureflags.core.entity.Source): com.featureflags.core.entity.VariableSaveState = repository.changeVariable(request = request, source = source)

}

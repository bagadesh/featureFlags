package com.bagadesh.core.usecases

import com.bagadesh.core.entity.Source
import com.bagadesh.core.entity.VariableSaveState
import com.bagadesh.core.repository.FeatureRepository
import com.bagadesh.core.request.ChangeVariableRequest

/**
 * Created by bagadesh on 27/06/23.
 */
class ChangeVariableUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun changeVariable(request: ChangeVariableRequest, source: Source): VariableSaveState = repository.changeVariable(request = request, source = source)

}

package com.bagadesh.core.usecases

import com.bagadesh.core.entity.GetVariableState
import com.bagadesh.core.repository.FeatureRepository

/**
 * Created by bagadesh on 02/07/23.
 */
class GetVariableValueUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun getVariableValue(featureKey: String, key: String): GetVariableState {
        return repository.getVariableValue(featureKey = featureKey, key = key)
    }

}
package com.featureflags.core.usecases

import com.featureflags.core.entity.GetVariableState
import com.featureflags.core.repository.FeatureRepository

/**
 * Created by bagadesh on 02/07/23.
 */
class GetVariableValueUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun getVariableValue(featureKey: String, key: String): com.featureflags.core.entity.GetVariableState {
        return repository.getVariableValue(featureKey = featureKey, key = key)
    }

}
package com.featureflags.core.usecases

import com.featureflags.core.entity.Feature
import com.featureflags.core.repository.FeatureRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 26/06/23.
 */
class GetFeatureFlagsUseCase constructor(
    private val repository: FeatureRepository
) {

    fun getFeatureFlags(): Flow<List<com.featureflags.core.entity.Feature>> = repository.getFeatureFlags()

}
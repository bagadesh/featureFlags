package com.bagadesh.core.usecases

import com.bagadesh.core.entity.Feature
import com.bagadesh.core.repository.FeatureRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 26/06/23.
 */
class GetFeatureFlagsUseCase constructor(
    private val repository: FeatureRepository
) {

    fun getFeatureFlags(): Flow<List<Feature>> = repository.getFeatureFlags()

}
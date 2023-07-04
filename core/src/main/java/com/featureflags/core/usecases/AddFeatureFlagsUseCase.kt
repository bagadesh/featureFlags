package com.featureflags.core.usecases

import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.Source
import com.featureflags.core.repository.FeatureRepository

/**
 * Created by bagadesh on 28/06/23.
 */
class AddFeatureFlagsUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun insert(data: List<com.featureflags.core.entity.Feature>, source: com.featureflags.core.entity.Source) {
        repository.insert(data, source = source)
    }

}
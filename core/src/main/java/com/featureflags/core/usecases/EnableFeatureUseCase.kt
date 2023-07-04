package com.featureflags.core.usecases

import com.featureflags.core.entity.Source
import com.featureflags.core.repository.FeatureRepository

/**
 * Created by bagadesh on 26/06/23.
 */
class EnableFeatureUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun enable(key: String, enabled: Boolean, source: com.featureflags.core.entity.Source) {
        repository.enableFeature(key = key, enabled = enabled, source = source)
    }

}
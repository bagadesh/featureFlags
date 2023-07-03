package com.bagadesh.core.usecases

import com.bagadesh.core.entity.Source
import com.bagadesh.core.repository.FeatureRepository

/**
 * Created by bagadesh on 26/06/23.
 */
class EnableFeatureUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun enable(key: String, enabled: Boolean, source: Source) {
        repository.enableFeature(key = key, enabled = enabled, source = source)
    }

}
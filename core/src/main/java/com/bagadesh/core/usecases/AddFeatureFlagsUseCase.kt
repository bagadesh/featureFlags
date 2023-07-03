package com.bagadesh.core.usecases

import com.bagadesh.core.entity.Feature
import com.bagadesh.core.entity.Source
import com.bagadesh.core.repository.FeatureRepository

/**
 * Created by bagadesh on 28/06/23.
 */
class AddFeatureFlagsUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun insert(data: List<Feature>, source: Source) {
        repository.insert(data, source = source)
    }

}
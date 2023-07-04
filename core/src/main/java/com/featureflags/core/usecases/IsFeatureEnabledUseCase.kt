package com.featureflags.core.usecases

import com.featureflags.core.repository.FeatureRepository

/**
 * Created by bagadesh on 02/07/23.
 */
class IsFeatureEnabledUseCase constructor(
    private val repository: FeatureRepository
) {

    suspend fun isEnabled(key: String, default: Boolean): Boolean {
        return repository.isFeatureEnabled(key = key, default = default)
    }

}
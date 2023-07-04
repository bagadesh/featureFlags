package com.featureflags.core.repository

import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.GetVariableState
import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.request.ChangeVariableRequest
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 26/06/23.
 */
interface FeatureRepository {

    fun getFeatureFlags(): Flow<List<com.featureflags.core.entity.Feature>>

    suspend fun enableFeature(key: String, enabled: Boolean, source: com.featureflags.core.entity.Source)

    suspend fun changeVariable(request: ChangeVariableRequest, source: com.featureflags.core.entity.Source): com.featureflags.core.entity.VariableSaveState

    suspend fun insert(data: List<com.featureflags.core.entity.Feature>, source: com.featureflags.core.entity.Source)

    suspend fun isFeatureEnabled(key: String, default: Boolean): Boolean

    suspend fun getVariableValue(featureKey: String, key: String): com.featureflags.core.entity.GetVariableState
}
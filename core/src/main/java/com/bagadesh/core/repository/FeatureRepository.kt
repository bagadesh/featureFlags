package com.bagadesh.core.repository

import com.bagadesh.core.entity.Feature
import com.bagadesh.core.entity.GetVariableState
import com.bagadesh.core.entity.Source
import com.bagadesh.core.entity.VariableSaveState
import com.bagadesh.core.request.ChangeVariableRequest
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 26/06/23.
 */
interface FeatureRepository {

    fun getFeatureFlags(): Flow<List<Feature>>

    suspend fun enableFeature(key: String, enabled: Boolean, source: Source)

    suspend fun changeVariable(request: ChangeVariableRequest, source: Source): VariableSaveState

    suspend fun insert(data: List<Feature>, source: Source)

    suspend fun isFeatureEnabled(key: String, default: Boolean): Boolean

    suspend fun getVariableValue(featureKey: String, key: String): GetVariableState
}
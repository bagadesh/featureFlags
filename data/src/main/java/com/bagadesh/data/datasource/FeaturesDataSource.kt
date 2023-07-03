package com.bagadesh.data.datasource

import com.bagadesh.core.entity.Source
import com.bagadesh.core.entity.VariableSaveState
import com.bagadesh.core.request.ChangeVariableRequest
import com.bagadesh.data.entity.DataFeature
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 26/06/23.
 */
interface FeaturesDataSource {

    val currentState: List<DataFeature>

    suspend fun checkInitialize()

    fun getFeatureFlags(): Flow<List<DataFeature>>

    suspend fun enableFeature(key: String, enabled: Boolean, source: Source)

    suspend fun changeVariable(request: ChangeVariableRequest, source: Source)

    suspend fun insertFeatureFlags(data: List<DataFeature>, source: Source)

}
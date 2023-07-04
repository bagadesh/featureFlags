package com.featureflags.data.datasource

import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.request.ChangeVariableRequest
import com.featureflags.data.entity.DataFeature
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 26/06/23.
 */
interface FeaturesDataSource {

    val currentState: List<DataFeature>

    suspend fun checkInitialize()

    fun getFeatureFlags(): Flow<List<DataFeature>>

    suspend fun enableFeature(key: String, enabled: Boolean, source: com.featureflags.core.entity.Source)

    suspend fun changeVariable(request: ChangeVariableRequest, source: com.featureflags.core.entity.Source)

    suspend fun insertFeatureFlags(data: List<DataFeature>, source: com.featureflags.core.entity.Source)

}
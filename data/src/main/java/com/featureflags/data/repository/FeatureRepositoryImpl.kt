package com.featureflags.data.repository

import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.GetVariableState
import com.featureflags.core.entity.Source
import com.featureflags.core.entity.VariableSaveState
import com.featureflags.core.entity.VariableType
import com.featureflags.core.entity.VariableTypeCheck
import com.featureflags.core.repository.FeatureRepository
import com.featureflags.core.repository.VariableCheckerRepository
import com.featureflags.core.request.ChangeVariableRequest
import com.featureflags.data.datasource.FeaturesDataSource
import com.featureflags.data.mapper.toData
import com.featureflags.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by bagadesh on 26/06/23.
 */
class FeatureRepositoryImpl constructor(
    private val featuresDataSource: FeaturesDataSource,
    private val variableCheckerRepository: VariableCheckerRepository
) : FeatureRepository {

    private val supportedTypes by lazy { com.featureflags.core.entity.VariableType.values().map { it.value } }

    override fun getFeatureFlags(): Flow<List<com.featureflags.core.entity.Feature>> =
        featuresDataSource.getFeatureFlags()
            .map { features ->
                features.map { dataFeature -> dataFeature.toDomain() }
            }

    override suspend fun enableFeature(key: String, enabled: Boolean, source: com.featureflags.core.entity.Source) =
        featuresDataSource.enableFeature(key = key, enabled = enabled, source = source)

    override suspend fun changeVariable(request: ChangeVariableRequest, source: com.featureflags.core.entity.Source): com.featureflags.core.entity.VariableSaveState {
        if (!supportedTypes.contains(request.valueType)) return com.featureflags.core.entity.VariableSaveState.Error(message = "Variable Type not supported")
        if (!request.changedValue.verify(type = request.valueType)) return com.featureflags.core.entity.VariableSaveState.Error(message = "Cannot able to parse given type's data")

        featuresDataSource.changeVariable(request = request, source = source)
        return com.featureflags.core.entity.VariableSaveState.Saved
    }

    private fun String.verify(type: String): Boolean = variableCheckerRepository.verifyType(value = this, valueType = type) is VariableTypeCheck.Success

    override suspend fun insert(data: List<com.featureflags.core.entity.Feature>, source: com.featureflags.core.entity.Source) {
        featuresDataSource.insertFeatureFlags(data.map { it.toData() }, source = source)
    }

    override suspend fun isFeatureEnabled(key: String, default: Boolean): Boolean {
        featuresDataSource.checkInitialize()
        return featuresDataSource.currentState.find { it.key == key }?.isEnabled ?: default
    }

    override suspend fun getVariableValue(featureKey: String, key: String): com.featureflags.core.entity.GetVariableState {
        featuresDataSource.checkInitialize()
        val features = featuresDataSource.currentState
        val variableList = features.find { feature -> feature.key == featureKey }?.variableList ?: return com.featureflags.core.entity.GetVariableState.Failed
        val variable = variableList.find { it.key == key } ?: return com.featureflags.core.entity.GetVariableState.Failed
        return com.featureflags.core.entity.GetVariableState.Success(variable = variable.toDomain())
    }
}
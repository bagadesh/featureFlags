package com.bagadesh.data.repository

import com.bagadesh.core.entity.Feature
import com.bagadesh.core.entity.GetVariableState
import com.bagadesh.core.entity.Source
import com.bagadesh.core.entity.VariableSaveState
import com.bagadesh.core.entity.VariableType
import com.bagadesh.core.entity.VariableTypeCheck
import com.bagadesh.core.repository.FeatureRepository
import com.bagadesh.core.repository.VariableCheckerRepository
import com.bagadesh.core.request.ChangeVariableRequest
import com.bagadesh.data.datasource.FeaturesDataSource
import com.bagadesh.data.mapper.toData
import com.bagadesh.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by bagadesh on 26/06/23.
 */
class FeatureRepositoryImpl constructor(
    private val featuresDataSource: FeaturesDataSource,
    private val variableCheckerRepository: VariableCheckerRepository
) : FeatureRepository {

    private val supportedTypes by lazy { VariableType.values().map { it.value } }

    override fun getFeatureFlags(): Flow<List<Feature>> =
        featuresDataSource.getFeatureFlags()
            .map { features ->
                features.map { dataFeature -> dataFeature.toDomain() }
            }

    override suspend fun enableFeature(key: String, enabled: Boolean, source: Source) =
        featuresDataSource.enableFeature(key = key, enabled = enabled, source = source)

    override suspend fun changeVariable(request: ChangeVariableRequest, source: Source): VariableSaveState {
        if (!supportedTypes.contains(request.valueType)) return VariableSaveState.Error(message = "Variable Type not supported")
        if (!request.changedValue.verify(type = request.valueType)) return VariableSaveState.Error(message = "Cannot able to parse given type's data")

        featuresDataSource.changeVariable(request = request, source = source)
        return VariableSaveState.Saved
    }

    private fun String.verify(type: String): Boolean = variableCheckerRepository.verifyType(value = this, valueType = type) is VariableTypeCheck.Success

    override suspend fun insert(data: List<Feature>, source: Source) {
        featuresDataSource.insertFeatureFlags(data.map { it.toData() }, source = source)
    }

    override suspend fun isFeatureEnabled(key: String, default: Boolean): Boolean {
        featuresDataSource.checkInitialize()
        return featuresDataSource.currentState.find { it.key == key }?.isEnabled ?: default
    }

    override suspend fun getVariableValue(featureKey: String, key: String): GetVariableState {
        featuresDataSource.checkInitialize()
        val features = featuresDataSource.currentState
        val variableList = features.find { feature -> feature.key == featureKey }?.variableList ?: return GetVariableState.Failed
        val variable = variableList.find { it.key == key } ?: return GetVariableState.Failed
        return GetVariableState.Success(variable = variable.toDomain())
    }
}
package com.bagadesh.core.contract.impl

import com.bagadesh.core.contract.Reader
import com.bagadesh.core.entity.Feature
import com.bagadesh.core.entity.GetVariableState
import com.bagadesh.core.usecases.GetFeatureFlagsUseCase
import com.bagadesh.core.usecases.GetVariableValueUseCase
import com.bagadesh.core.usecases.IsFeatureEnabledUseCase
import com.bagadesh.core.usecases.TypeConverterUseCase
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * Created by bagadesh on 26/06/23.
 */
class ReaderImpl(
    private val getFeatureFlagsUseCase: GetFeatureFlagsUseCase,
    private val typeConverterUseCase: TypeConverterUseCase,
    private val isFeatureEnabledUseCase: IsFeatureEnabledUseCase,
    private val getVariableValueUseCase: GetVariableValueUseCase
) : Reader {


    override suspend fun isFeatureEnabled(key: String, default: Boolean): Boolean {
        return isFeatureEnabledUseCase.isEnabled(key = key, default = default)
    }


    override suspend fun <T : Any> getVariableValue(featureKey: String, key: String, default: () -> T, kClass: KClass<T>): T {
        return when (val result = getVariableValueUseCase.getVariableValue(featureKey = featureKey, key = key)) {
            GetVariableState.Failed -> default()
            is GetVariableState.Success -> {
                val variable = result.variable
                typeConverterUseCase.convert(value = variable.value, valueType = variable.valueType, kClass = kClass)
            }
        }
    }


    override fun getFeatures(): Flow<List<Feature>> = getFeatureFlagsUseCase.getFeatureFlags()
}
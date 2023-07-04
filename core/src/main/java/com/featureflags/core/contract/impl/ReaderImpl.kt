package com.featureflags.core.contract.impl

import com.featureflags.core.contract.Reader
import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.GetVariableState
import com.featureflags.core.usecases.GetFeatureFlagsUseCase
import com.featureflags.core.usecases.GetVariableValueUseCase
import com.featureflags.core.usecases.IsFeatureEnabledUseCase
import com.featureflags.core.usecases.TypeConverterUseCase
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
) : com.featureflags.core.contract.Reader {


    override suspend fun isFeatureEnabled(key: String, default: Boolean): Boolean {
        return isFeatureEnabledUseCase.isEnabled(key = key, default = default)
    }


    override suspend fun <T : Any> getVariableValue(featureKey: String, key: String, default: () -> T, kClass: KClass<T>): T {
        return when (val result = getVariableValueUseCase.getVariableValue(featureKey = featureKey, key = key)) {
            com.featureflags.core.entity.GetVariableState.Failed -> default()
            is com.featureflags.core.entity.GetVariableState.Success -> {
                val variable = result.variable
                typeConverterUseCase.convert(value = variable.value, valueType = variable.valueType, kClass = kClass)
            }
        }
    }


    override fun getFeatures(): Flow<List<com.featureflags.core.entity.Feature>> = getFeatureFlagsUseCase.getFeatureFlags()
}
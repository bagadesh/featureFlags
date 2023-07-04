package com.featureflags.core.contract

import com.featureflags.core.entity.Feature
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * Created by bagadesh on 26/06/23.
 */
interface Reader {

    suspend fun isFeatureEnabled(key: String, default: Boolean = false): Boolean

    suspend fun <T : Any> getVariableValue(featureKey: String, key: String, default: () -> T, kClass: KClass<T>): T

    fun getFeatures(): Flow<List<com.featureflags.core.entity.Feature>>

}
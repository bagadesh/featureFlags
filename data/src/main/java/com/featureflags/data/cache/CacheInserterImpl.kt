@file:Suppress("UNCHECKED_CAST")

package com.featureflags.data.cache

import android.content.res.Resources
import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.Source
import com.featureflags.core.entity.Variable
import com.featureflags.core.usecases.AddFeatureFlagsUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by bagadesh on 28/06/23.
 */
class CacheInserterImpl constructor(
    private val gson: Gson,
    private val cachedFileName: String,
    private val resources: Resources,
    private val bundleConfigResource: Int,
    private val addFeatureFlagsUseCase: AddFeatureFlagsUseCase,
    private val scope: CoroutineScope
) : CacheInserter {

    companion object {
        const val FEATURE_FLAG_CONFIG = "featureConfigs"
        const val ENABLED = "enabled"
        const val DESCRIPTION = "description"
        const val VARIABLES = "variables"
        const val VALUE = "value"
        const val VALUE_TYPE = "value_type"
    }

    override fun insertFeatureFlags() {
        val packedConfig = getSavedJson()
        val gsonResult = gson.fromJson<Map<String, Any>>(packedConfig, object : TypeToken<Map<String, Any>>() {}.type)
        val featureResult = (gsonResult[FEATURE_FLAG_CONFIG] as Map<String, Map<String, Any>>).toFeatureList()
        scope.launch {
            addFeatureFlagsUseCase.insert(data = featureResult, source = com.featureflags.core.entity.Source.CONFIG)
        }
    }

    private fun getSavedJson(): String {
        val cachedFile = File(cachedFileName)
        return if (cachedFile.exists()) {
            cachedFile.bufferedReader().use { it.readText() }
        } else {
            resources.openRawResource(bundleConfigResource).bufferedReader().use { it.readText() }
        }
    }

    private fun Map<String, Map<String, Any>>.toFeatureList(): List<com.featureflags.core.entity.Feature> {
        return map {
            val enabled = it.value[ENABLED] as Boolean
            val description = it.value[DESCRIPTION] as? String ?: ""
            val variables = it.value[VARIABLES] as Map<String, Map<String, Any>>
            com.featureflags.core.entity.Feature(
                key = it.key,
                isEnabled = enabled,
                description = description,
                variableList = variables.map { variable ->
                    val valueDetail = variable.value
                    val value = valueDetail[VALUE].toString()
                    val valueType = valueDetail[VALUE_TYPE] as String
                    com.featureflags.core.entity.Variable(
                        key = variable.key,
                        value = value,
                        valueType = valueType
                    )
                }
            )
        }
    }
}
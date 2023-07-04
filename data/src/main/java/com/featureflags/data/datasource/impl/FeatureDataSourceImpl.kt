@file:OptIn(ExperimentalCoroutinesApi::class)

package com.featureflags.data.datasource.impl

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.featureflags.core.entity.Source
import com.featureflags.core.request.ChangeVariableRequest
import com.featureflags.data.datasource.FeaturesDataSource
import com.featureflags.data.entity.DataFeature
import com.featureflags.data.entity.DataVariable
import com.featureflags.data.persistence.dao.FeatureDao
import com.featureflags.data.persistence.dao.VariableDao
import com.featureflags.data.persistence.entity.AppFeature
import com.featureflags.data.persistence.entity.AppVariable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by bagadesh on 26/06/23.
 */
class FeatureDataSourceImpl(
    private val featureDao: FeatureDao,
    private val variableDao: VariableDao,
    private val scope: CoroutineScope
) : FeaturesDataSource {

    private val isInitialized = AtomicBoolean(false)
    private val initializedJob = Job()
    private val _state = MutableStateFlow<List<DataFeature>>(emptyList())
    override val currentState: List<DataFeature>
        get() = _state.value

    init {
        scope.launch(Dispatchers.IO) {
            featureDao.getFeatures().collectLatest { features ->
                _state.value = features.map { feature ->
                    DataFeature(
                        key = feature.key,
                        isEnabled = feature.isEnabled,
                        variableList = variableDao.getVariables(featureKey = feature.key)
                            .map { variable ->
                                DataVariable(
                                    key = variable.key,
                                    value = variable.value,
                                    valueType = variable.valueType
                                )
                            },
                        description = feature.description
                    )
                }
                if (!isInitialized.getAndSet(true) && !initializedJob.isCompleted) {
                    initializedJob.complete()
                }
            }
        }
    }

    override suspend fun checkInitialize() {
        initializedJob.join()
    }

    override fun getFeatureFlags(): Flow<List<DataFeature>> {
        return _state.asStateFlow()
    }

    override suspend fun enableFeature(key: String, enabled: Boolean, source: com.featureflags.core.entity.Source) {
        val result = featureDao.update(isEnabled = enabled, source = source.value, key = key)
        enableLocalFeature(key = key, enabled = enabled, result = result)
        Log.d("FeatureDataSourceImpl", "::enableFeature key=$key, enabled=$enabled, result = $result")
    }

    /**
     * [result] - 1 is Row is affected else Nothing is affected
     */
    private suspend fun enableLocalFeature(key: String, enabled: Boolean, result: Int) {
        if (result != 1) return
        _state.emit(_state.value.map {
            if (it.key == key) {
                it.copy(isEnabled = enabled)
            } else it
        })
    }

    override suspend fun changeVariable(request: ChangeVariableRequest, source: com.featureflags.core.entity.Source) {
        val result = variableDao.update(
            value = request.changedValue,
            source = source.value,
            featureKey = request.featureKey,
            key = request.variableKey
        )
        changeVariable(request = request, result = result)
        Log.d("FeatureDataSourceImpl", "::enableFeature request=$request, source=$source, result=$result")
    }

    /**
     * [result] - 1 is Row is affected else Nothing is affected
     */
    private suspend fun changeVariable(request: ChangeVariableRequest, result: Int) {
        if (result != 1) return
        _state.emit(_state.value.map {
            if (it.key == request.featureKey) {
                it.copy(variableList = it.variableList.map { variable ->
                    if (variable.key == request.variableKey) {
                        variable.copy(value = request.changedValue)
                    } else variable
                })
            } else it
        })
    }

    override suspend fun insertFeatureFlags(data: List<DataFeature>, source: com.featureflags.core.entity.Source) {
        val features = data.map { AppFeature(key = it.key, isEnabled = it.isEnabled, description = it.description, source = source.value) }
        val allVariables = data.flatMap { feature ->
            feature.variableList.map { variable -> AppVariable(featureKey = feature.key, key = variable.key, value = variable.value, valueType = variable.valueType, source = source.value) }
        }

        val featuresInDb = featureDao.getFeaturesSync()
        val allVariablesInDb = variableDao.getAllVariables()

        val featuresMapInDb = featuresInDb.associateBy { it.key }
        val allVariablesMapInDb = allVariablesInDb.associateBy { "${it.featureKey}-${it.key}" }


        /**
         * 1. Filter out things need to be added
         */

        val newFeatures = features.filter { !featuresMapInDb.containsKey(it.key) }
        val updateFeatures = features.filter { featuresMapInDb.containsKey(it.key) && it.source >= featuresMapInDb[it.key]!!.source }

        val newAllVariables = allVariables.filter { !allVariablesMapInDb.containsKey("${it.featureKey}-${it.key}") }
        val updateAllVariables = allVariables.filter {
            allVariablesMapInDb.containsKey("${it.featureKey}-${it.key}") && it.source >= allVariablesMapInDb["${it.featureKey}-${it.key}"]!!.source
        }

        Log.d("FeatureDataSourceImpl", "incoming features size = ${features.size}")
        Log.d("FeatureDataSourceImpl", "incoming allVariables size = ${allVariables.size}")

        Log.d("FeatureDataSourceImpl", "incoming featuresInDb size = ${featuresMapInDb.size}")
        Log.d("FeatureDataSourceImpl", "incoming allVariablesInDb size = ${allVariablesMapInDb.size}")

        Log.d("FeatureDataSourceImpl", "incoming newFeatures size = ${newFeatures.size}")
        Log.d("FeatureDataSourceImpl", "incoming updateFeatures size = ${updateFeatures.size}")

        Log.d("FeatureDataSourceImpl", "incoming newAllVariables size = ${newAllVariables.size}")
        Log.d("FeatureDataSourceImpl", "incoming updateAllVariables size = ${updateAllVariables.size}")

        /**
         * Deleted Needs to be handled
         */

        featureDao.insertAll(newFeatures)
        updateFeatures.forEach {
            val query = SimpleSQLiteQuery(
                query = "UPDATE app_feature SET isEnabled = ?, description = ? WHERE key = ? AND source <= ?",
                bindArgs = arrayOf(it.isEnabled, it.description, it.key, it.source)
            )
            featureDao.updateRawQuery(query = query)
        }

        variableDao.insertAll(newAllVariables)
        updateAllVariables.forEach {
            val query = SimpleSQLiteQuery(
                query = buildString {
                    append("UPDATE app_variable SET value = ?, valueType = ? ")
                    append("WHERE key = ? AND featureKey = ? AND source <= ?")
                },
                bindArgs = arrayOf(it.value, it.valueType, it.key, it.featureKey, it.source)
            )
            variableDao.updateRawQuery(query = query)
        }
    }
}
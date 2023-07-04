package com.bagadesh.featureflags

import android.util.Log
import com.featureflags.core.entity.Source
import com.featureflags.core.request.ChangeVariableRequest
import com.featureflags.data.FeatureFlagSdkImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import java.lang.Exception
import kotlin.random.Random

/**
 * Created by bagadesh on 01/07/23.
 */
class FeatureFlagTester {


    sealed interface TestState {
        data class Loading(val message: String) : TestState
        data class Failed(val message: String) : TestState
        data class Success(val message: String) : TestState
    }

    val state: Flow<TestState> = testFlow()
    private val reader = FeatureFlagSdkImpl.getReader()
    private val writer = FeatureFlagSdkImpl.getWriter()
    private val sampleFeatureName = "lastToFirst"


    private fun testFlow(): Flow<TestState> {
        return flow {
            emit(TestState.Loading("Testing isFeatureEnabled method"))
            delay(500)
            val received1 = reader.isFeatureEnabled(key = sampleFeatureName)
            writer.enableFeature(key = sampleFeatureName, enabled = !received1, source = com.featureflags.core.entity.Source.USER)
            val received2 = reader.isFeatureEnabled(key = sampleFeatureName)

            if (received1 == received2) {
                emit(TestState.Failed("isFeatureEnabled is failed"))
                return@flow
            }
            delay(500)
            if (!testVariable()) return@flow
            delay(500)
            emit(TestState.Success("Feature Flag Testing is successFull"))
        }
    }

    private suspend fun FlowCollector<TestState>.testVariable(): Boolean {
        // Testing getVariableValue
        emit(TestState.Loading("Testing getVariableValue method"))
        val variableKey = "test"
        val changedValue = "changedValue" + Random.nextInt().toString()
        val received1 = reader.getVariableValue(featureKey = sampleFeatureName, key = variableKey, default = { "default" }, String::class)
        writer.changeVariableValue(
            request = ChangeVariableRequest(
                featureKey = sampleFeatureName,
                variableKey = variableKey,
                currentValue = received1,
                changedValue = changedValue,
                valueType = "string",
            ),
            source = com.featureflags.core.entity.Source.USER
        )
        val received2 = reader.getVariableValue(featureKey = sampleFeatureName, key = variableKey, default = { "default" }, String::class)
        Log.d("FeatureFlagTester", "received1 = $received1, received2 = $received2")
        if (received1 == received2) {
            emit(TestState.Failed("getVariableValue is failed"))
            return false
        }

        try {
            val a = reader.getVariableValue(featureKey = "testVariables", key = "int1", default = { 0 }, Int::class)
            val b = reader.getVariableValue(featureKey = "testVariables", key = "double1", default = { 0.0 }, Double::class)
            val c = reader.getVariableValue(featureKey = "testVariables", key = "checked", default = { false }, Boolean::class)
            val d = reader.getVariableValue(featureKey = "testVariables", key = "jsonArray", default = { listOf<Any>() }, List::class)
            val e = reader.getVariableValue(featureKey = "testVariables", key = "jsonObject", default = { mapOf<Any, Any>() }, Map::class)
            listOf(a, b, c, d, e).forEach {
                Log.d("FeatureFlagTester", "Transformed = $it")
            }
        } catch (exception: Exception) {
            Log.d("FeatureFlagTester", exception.stackTraceToString())
            emit(TestState.Failed("getVariableValue is failed inside try/catch"))
            return false
        }

        return true
    }


}


















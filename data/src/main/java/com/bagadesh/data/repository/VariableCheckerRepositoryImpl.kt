package com.bagadesh.data.repository

import com.bagadesh.core.entity.VariableType
import com.bagadesh.core.entity.VariableTypeCheck
import com.bagadesh.core.repository.VariableCheckerRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by bagadesh on 01/07/23.
 */
class VariableCheckerRepositoryImpl constructor(
    private val gson: Gson
) : VariableCheckerRepository {

    override fun verifyType(value: String, valueType: String): VariableTypeCheck {
        when (valueType) {
            VariableType.STRING.value -> {
                return verify("Cannot able to parse the string") { value }
            }

            VariableType.BOOLEAN.value -> {
                return verify("Cannot able to parse the boolean") { value.also { it.toBooleanStrict() } }
            }

            VariableType.INT.value -> {
                return verify("Cannot able to parse the Int") { value.also { it.toInt() } }
            }

            VariableType.DOUBLE.value -> {
                return verify("Cannot able to parse the Double") { value.also { it.toDouble() } }
            }

            VariableType.FLOAT.value -> {
                return verify("Cannot able to parse the Float") { value.also { it.toFloat() } }
            }

            VariableType.JSON_ARRAY.value -> {
                return verify("Cannot able to parse the Json Array") {
                    value.also {
                        gson.verify<List<Any>>(value = it)
                    }
                }
            }

            VariableType.JSON_OBJECT.value -> {
                return verify("Cannot able to parse the Json Object") {
                    value.also {
                        gson.verify<Map<String, Any>>(value = it)
                    }
                }
            }


            else -> return VariableTypeCheck.Success(value)
        }
    }

    private fun <T> Gson.verify(value: String): T {
        return fromJson(value, object : TypeToken<T>() {}.type)
    }

    private inline fun verify(errorMessage: String, action: () -> String): VariableTypeCheck {
        kotlin.runCatching {
            return VariableTypeCheck.Success(action())
        }.getOrNull()
            ?: return VariableTypeCheck.Failed(message = errorMessage)
    }
}
package com.bagadesh.data.repository

import com.bagadesh.core.repository.ConverterRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

/**
 * Created by bagadesh on 27/06/23.
 */
class ConverterRepositoryImpl constructor(
    private val gson: Gson
) : ConverterRepository {

    override fun <T : Any> convert(value: String, valueType: String, kClass: KClass<T>): T {
        return gson.parseData(value = value)
    }

    private fun <T> Gson.parseData(value: String): T {
        return fromJson(value, object : TypeToken<T>() {}.type)
    }
}
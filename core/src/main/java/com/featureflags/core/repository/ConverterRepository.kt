package com.featureflags.core.repository

import kotlin.reflect.KClass

/**
 * Created by bagadesh on 27/06/23.
 */
interface ConverterRepository {

    fun <T : Any> convert(value: String, valueType: String, kClass: KClass<T>): T

}
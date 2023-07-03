package com.bagadesh.core.usecases

import com.bagadesh.core.repository.ConverterRepository
import kotlin.reflect.KClass

/**
 * Created by bagadesh on 27/06/23.
 */
class TypeConverterUseCase constructor(
    private val converterRepository: ConverterRepository
) {

    fun <T : Any> convert(value: String, valueType: String, kClass: KClass<T>): T =
        converterRepository.convert(value = value, valueType = valueType, kClass = kClass)

}
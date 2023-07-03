package com.bagadesh.data.mapper

import com.bagadesh.core.entity.Feature
import com.bagadesh.core.entity.Variable
import com.bagadesh.data.entity.DataFeature
import com.bagadesh.data.entity.DataVariable

/**
 * Created by bagadesh on 26/06/23.
 */

fun DataFeature.toDomain(): Feature {
    return Feature(
        key = key,
        isEnabled = isEnabled,
        variableList = variableList.map { it.toDomain() }
    )
}

fun DataVariable.toDomain(): Variable {
    return Variable(
        key = key,
        value = value,
        valueType = valueType
    )
}

fun Feature.toData(): DataFeature {
    return DataFeature(
        key = key,
        isEnabled = isEnabled,
        variableList = variableList.map { it.toData() }
    )
}

fun Variable.toData(): DataVariable {
    return DataVariable(
        key = key,
        value = value,
        valueType = valueType
    )
}



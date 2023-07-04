package com.featureflags.data.mapper

import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.Variable
import com.featureflags.data.entity.DataFeature
import com.featureflags.data.entity.DataVariable

/**
 * Created by bagadesh on 26/06/23.
 */

fun DataFeature.toDomain(): com.featureflags.core.entity.Feature {
    return com.featureflags.core.entity.Feature(
        key = key,
        isEnabled = isEnabled,
        description = description,
        variableList = variableList.map { it.toDomain() }
    )
}

fun DataVariable.toDomain(): com.featureflags.core.entity.Variable {
    return com.featureflags.core.entity.Variable(
        key = key,
        value = value,
        valueType = valueType
    )
}

fun com.featureflags.core.entity.Feature.toData(): DataFeature {
    return DataFeature(
        key = key,
        isEnabled = isEnabled,
        description = description,
        variableList = variableList.map { it.toData() }
    )
}

fun com.featureflags.core.entity.Variable.toData(): DataVariable {
    return DataVariable(
        key = key,
        value = value,
        valueType = valueType
    )
}



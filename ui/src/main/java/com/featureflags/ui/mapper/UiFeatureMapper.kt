package com.featureflags.ui.mapper

import com.featureflags.core.entity.Feature
import com.featureflags.core.entity.Variable
import com.featureflags.ui.entity.UiFeature
import com.featureflags.ui.entity.UiVariable

/**
 * Created by bagadesh on 26/06/23.
 */

fun com.featureflags.core.entity.Feature.toUi(): UiFeature {
    return UiFeature(key = key, isEnabled = isEnabled, variables = variableList.map { it.toUi() }, description = description)
}


fun com.featureflags.core.entity.Variable.toUi(): UiVariable {
    return UiVariable(key = key, value = value, valueType = valueType)
}

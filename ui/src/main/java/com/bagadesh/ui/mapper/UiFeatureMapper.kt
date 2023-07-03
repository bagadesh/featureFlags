package com.bagadesh.ui.mapper

import com.bagadesh.core.entity.Feature
import com.bagadesh.core.entity.Variable
import com.bagadesh.ui.entity.UiFeature
import com.bagadesh.ui.entity.UiVariable

/**
 * Created by bagadesh on 26/06/23.
 */

fun Feature.toUi(): UiFeature {
    return UiFeature(key = key, isEnabled = isEnabled, variables = variableList.map { it.toUi() }, description = description)
}


fun Variable.toUi(): UiVariable {
    return UiVariable(key = key, value = value, valueType = valueType)
}

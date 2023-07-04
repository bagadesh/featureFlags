package com.featureflags.core.entity

/**
 * Created by bagadesh on 28/06/23.
 */
enum class VariableType(val value: String) {
    BOOLEAN("boolean"),
    INT("integer"),
    STRING("string"),
    DOUBLE("double"),
    FLOAT("float"),
    JSON_ARRAY("json_array"),
    JSON_OBJECT("json_object"),
    ;

    companion object {

        val list = com.featureflags.core.entity.VariableType.values().toList()

        fun from(value: String): com.featureflags.core.entity.VariableType {
            return com.featureflags.core.entity.VariableType.Companion.list.find { it.value == value } ?: com.featureflags.core.entity.VariableType.STRING
        }

    }
}
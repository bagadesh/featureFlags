package com.bagadesh.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by bagadesh on 28/06/23.
 */
@Entity(tableName = "app_variable")
data class AppVariable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val featureKey: String,
    val key: String,
    val value: String,
    val valueType: String,
    val source: Int
)

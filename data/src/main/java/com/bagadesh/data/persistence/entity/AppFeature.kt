package com.bagadesh.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by bagadesh on 28/06/23.
 */

@Entity(tableName = "app_feature")
data class AppFeature(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String,
    val isEnabled: Boolean,
    val description: String = "",
    val source: Int
)

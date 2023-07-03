package com.bagadesh.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bagadesh.data.persistence.dao.FeatureDao
import com.bagadesh.data.persistence.dao.VariableDao
import com.bagadesh.data.persistence.entity.AppFeature
import com.bagadesh.data.persistence.entity.AppVariable

/**
 * Created by bagadesh on 28/06/23.
 */
@Database(entities = [AppFeature::class, AppVariable::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun featureDao(): FeatureDao

    abstract fun variableDao(): VariableDao

}
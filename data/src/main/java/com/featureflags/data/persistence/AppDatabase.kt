package com.featureflags.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.featureflags.data.persistence.dao.FeatureDao
import com.featureflags.data.persistence.dao.VariableDao
import com.featureflags.data.persistence.entity.AppFeature
import com.featureflags.data.persistence.entity.AppVariable

/**
 * Created by bagadesh on 28/06/23.
 */
@Database(entities = [AppFeature::class, AppVariable::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun featureDao(): FeatureDao

    abstract fun variableDao(): VariableDao

}
package com.featureflags.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.featureflags.data.persistence.entity.AppFeature
import com.featureflags.data.persistence.entity.AppVariable
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 28/06/23.
 */
@Dao
interface VariableDao {

    @Query("SELECT * FROM app_variable where featureKey = :featureKey")
    suspend fun getVariables(featureKey: String): List<AppVariable>

    @Query("SELECT * FROM app_variable")
    suspend fun getAllVariables(): List<AppVariable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AppVariable>):  List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: AppVariable): Long

    @Query("UPDATE app_variable SET value = :value, source = :source WHERE featureKey = :featureKey AND `key` = :key AND source <= :source")
    suspend fun update(value: String, source: Int, featureKey: String, key: String): Int

    @RawQuery
    suspend fun updateRawQuery(query: SupportSQLiteQuery): Long

}
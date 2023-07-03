package com.bagadesh.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.bagadesh.data.persistence.entity.AppFeature
import kotlinx.coroutines.flow.Flow

/**
 * Created by bagadesh on 28/06/23.
 */
@Dao
interface FeatureDao {

    @Query("SELECT * FROM app_feature")
    fun getFeatures(): Flow<List<AppFeature>>

    @Query("SELECT * FROM app_feature")
    suspend fun getFeaturesSync(): List<AppFeature>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: AppFeature): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AppFeature>): List<Long>

    @RawQuery
    suspend fun updateRawQuery(query: SupportSQLiteQuery): Long

    @Query("UPDATE app_feature SET isEnabled = :isEnabled, source = :source WHERE `key` = :key AND source <= :source")
    suspend fun update(isEnabled: Boolean, source: Int, key: String): Int

}
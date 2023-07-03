package com.bagadesh.data

import android.content.Context
import androidx.room.Room
import com.bagadesh.core.contract.FeatureFlagSdk
import com.bagadesh.core.contract.Reader
import com.bagadesh.core.contract.Writer
import com.bagadesh.core.contract.impl.ReaderImpl
import com.bagadesh.core.contract.impl.WriterImpl
import com.bagadesh.core.repository.ConverterRepository
import com.bagadesh.core.repository.FeatureRepository
import com.bagadesh.core.repository.VariableCheckerRepository
import com.bagadesh.core.usecases.AddFeatureFlagsUseCase
import com.bagadesh.core.usecases.ChangeVariableUseCase
import com.bagadesh.core.usecases.EnableFeatureUseCase
import com.bagadesh.core.usecases.GetFeatureFlagsUseCase
import com.bagadesh.core.usecases.GetVariableValueUseCase
import com.bagadesh.core.usecases.IsFeatureEnabledUseCase
import com.bagadesh.core.usecases.TypeConverterUseCase
import com.bagadesh.data.cache.CacheInserter
import com.bagadesh.data.cache.CacheInserterImpl
import com.bagadesh.data.datasource.FeaturesDataSource
import com.bagadesh.data.datasource.impl.FeatureDataSourceImpl
import com.bagadesh.data.persistence.AppDatabase
import com.bagadesh.data.repository.ConverterRepositoryImpl
import com.bagadesh.data.repository.FeatureRepositoryImpl
import com.bagadesh.data.repository.VariableCheckerRepositoryImpl
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Created by bagadesh on 26/06/23.
 */
object FeatureFlagSdkImpl : FeatureFlagSdk {


    private val gson by lazy { Gson() }
    private lateinit var db: AppDatabase
    private val scope = CoroutineScope(SupervisorJob())
    private val featuresDao by lazy { db.featureDao() }
    private val variablesDao by lazy { db.variableDao() }
    private val variableCheckerRepository: VariableCheckerRepository by lazy { VariableCheckerRepositoryImpl(gson = gson) }
    private val featuresDataSource: FeaturesDataSource by lazy { FeatureDataSourceImpl(scope = scope, featureDao = featuresDao, variableDao = variablesDao) }
    private val featureRepository: FeatureRepository by lazy { FeatureRepositoryImpl(featuresDataSource = featuresDataSource, variableCheckerRepository = variableCheckerRepository) }
    private val converterRepository: ConverterRepository by lazy { ConverterRepositoryImpl(gson = gson) }
    private val getFeaturesUseCase by lazy { GetFeatureFlagsUseCase(repository = featureRepository) }
    private val typeConverterUseCase by lazy { TypeConverterUseCase(converterRepository = converterRepository) }
    private val enableFeatureUseCase by lazy { EnableFeatureUseCase(repository = featureRepository) }
    private val changeVariableUseCase by lazy { ChangeVariableUseCase(repository = featureRepository) }
    private val addFeatureFlagsUseCase by lazy { AddFeatureFlagsUseCase(repository = featureRepository) }
    private val isFeatureEnabledUseCase by lazy { IsFeatureEnabledUseCase(repository = featureRepository) }
    private val getVariableValueUseCase by lazy { GetVariableValueUseCase(repository = featureRepository) }
    private val _reader: Reader by lazy {
        ReaderImpl(
            getFeatureFlagsUseCase = getFeaturesUseCase,
            typeConverterUseCase = typeConverterUseCase,
            isFeatureEnabledUseCase = isFeatureEnabledUseCase,
            getVariableValueUseCase = getVariableValueUseCase
        )
    }
    private val _writer: Writer by lazy { WriterImpl(enableFeatureUseCase = enableFeatureUseCase, changeVariableUseCase = changeVariableUseCase) }
    private lateinit var cacheInserter: CacheInserter

    fun init(
        context: Context,
        cachedFileName: String = "feature_flags_cache.json",
        defaultConfigResource: Int,
    ) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "feature-flag-db"
        ).build()
        cacheInserter = CacheInserterImpl(
            gson = gson,
            cachedFileName = cachedFileName,
            resources = context.resources,
            bundleConfigResource = defaultConfigResource,
            addFeatureFlagsUseCase = addFeatureFlagsUseCase,
            scope = scope
        )
        cacheInserter.insertFeatureFlags()
    }


    override fun getReader(): Reader = _reader

    override fun getWriter(): Writer = _writer
}
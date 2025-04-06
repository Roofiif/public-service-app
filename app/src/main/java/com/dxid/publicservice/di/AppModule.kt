package com.dxid.publicservice.di

import android.content.Context
import androidx.room.Room
import com.dxid.publicservice.BuildConfig
import com.dxid.publicservice.data.local.AppDatabase
import com.dxid.publicservice.data.local.ServiceDao
import com.dxid.publicservice.data.remote.Api
import com.dxid.publicservice.data.repository.ServiceRepositoryImpl
import com.dxid.publicservice.domain.repository.ServiceRepository
import com.dxid.publicservice.domain.usecase.GetServiceUseCase
import com.dxid.publicservice.utils.Constants.BASE_URL
import com.dxid.publicservice.utils.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApi(): Api {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val token = BuildConfig.API_TOKEN
                        val request = chain.request().newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build()
                        chain.proceed(request)
                    }
                    .addInterceptor(loggingInterceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "service_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideServiceDao(database: AppDatabase): ServiceDao {
        return database.serviceDao()
    }

    @Provides
    @Singleton
    fun provideServiceRepository(
        api: Api,
        dao: ServiceDao,
        preferencesManager: PreferencesManager
    ): ServiceRepository {
        return ServiceRepositoryImpl(api, dao, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideGetServiceUseCase(
        repository: ServiceRepository
    ): GetServiceUseCase {
        return GetServiceUseCase(repository)
    }
}
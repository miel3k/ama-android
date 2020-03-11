package com.ama.di.network

import com.ama.data.configurations.remote.ConfigurationsApi
import com.ama.data.locations.remote.LocationsApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideConfigurationsApi(retrofit: Retrofit): ConfigurationsApi {
        return retrofit.create(ConfigurationsApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideLocationsApi(retrofit: Retrofit): LocationsApi {
        return retrofit.create(LocationsApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pl-ama-android.firebaseio.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}
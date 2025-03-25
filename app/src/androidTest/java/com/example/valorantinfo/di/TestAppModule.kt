package com.example.valorantinfo.di

import com.example.valorantinfo.api.*
import com.example.valorantinfo.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://valorant-api.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAgentApiService(retrofit: Retrofit): AgentApiService {
        return retrofit.create(AgentApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAgentDetailsApiService(retrofit: Retrofit): AgentDetailsApiService {
        return retrofit.create(AgentDetailsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAgentRepository(apiService: AgentApiService): AgentRepository {
        return AgentRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAgentDetailsRepository(apiService: AgentDetailsApiService): AgentDetailsRepository {
        return AgentDetailsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideBuddyApiService(retrofit: Retrofit): BuddyApiService {
        return retrofit.create(BuddyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBundleApiService(retrofit: Retrofit): BundleApiService {
        return retrofit.create(BundleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCeremonyApiService(retrofit: Retrofit): CeremonyApiService {
        return retrofit.create(CeremonyApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideContentTierApiService(retrofit: Retrofit): ContentTierApiService {
        return retrofit.create(ContentTierApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContentTierRepository(api: ContentTierApiService): ContentTierRepository {
        return ContentTierRepositoryImpl(api)
    }
} 
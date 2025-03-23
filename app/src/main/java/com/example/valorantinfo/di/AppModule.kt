package com.example.valorantinfo.di

import com.example.valorantinfo.api.AgentApiService
import com.example.valorantinfo.api.AgentDetailsApiService
import com.example.valorantinfo.repository.AgentDetailsRepository
import com.example.valorantinfo.repository.AgentDetailsRepositoryImpl
import com.example.valorantinfo.repository.AgentRepository
import com.example.valorantinfo.repository.AgentRepositoryImpl
import com.example.valorantinfo.api.BuddyApiService
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.repository.BuddyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.lifecycle.ViewModelProvider

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://valorant-api.com/")
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
    fun provideBuddyRepository(apiService: BuddyApiService): BuddyRepository {
        return BuddyRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelProvider.NewInstanceFactory()
    }
}
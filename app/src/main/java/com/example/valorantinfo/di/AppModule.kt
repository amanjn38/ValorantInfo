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
import com.example.valorantinfo.api.BundleApiService
import com.example.valorantinfo.repository.BundleRepository
import com.example.valorantinfo.repository.BundleRepositoryImpl
import com.example.valorantinfo.api.CeremonyApiService
import com.example.valorantinfo.repository.CeremonyRepository
import com.example.valorantinfo.repository.CeremonyRepositoryImpl
import com.example.valorantinfo.api.CompetitiveTierApiService
import com.example.valorantinfo.repository.CompetitiveTierRepository
import com.example.valorantinfo.repository.CompetitiveTierRepositoryImpl
import com.example.valorantinfo.api.ContentTierApiService
import com.example.valorantinfo.repository.ContentTierRepository
import com.example.valorantinfo.repository.ContentTierRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.lifecycle.ViewModelProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://valorant-api.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
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
    fun provideBundleApiService(retrofit: Retrofit): BundleApiService {
        return retrofit.create(BundleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBundleRepository(apiService: BundleApiService): BundleRepository {
        return BundleRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCeremonyApiService(retrofit: Retrofit): CeremonyApiService {
        return retrofit.create(CeremonyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCeremonyRepository(apiService: CeremonyApiService): CeremonyRepository {
        return CeremonyRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideCompetitiveTierApiService(retrofit: Retrofit): CompetitiveTierApiService {
        return retrofit.create(CompetitiveTierApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCompetitiveTierRepository(apiService: CompetitiveTierApiService): CompetitiveTierRepository {
        return CompetitiveTierRepositoryImpl(apiService)
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

    @Provides
    @Singleton
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelProvider.NewInstanceFactory()
    }
}
package com.example.valorantinfo.di

import androidx.lifecycle.ViewModelProvider
import com.example.valorantinfo.api.AgentApiService
import com.example.valorantinfo.api.AgentDetailsApiService
import com.example.valorantinfo.api.BuddyApiService
import com.example.valorantinfo.api.BundleApiService
import com.example.valorantinfo.api.CeremonyApiService
import com.example.valorantinfo.api.CompetitiveTierApiService
import com.example.valorantinfo.api.ContentTierApiService
import com.example.valorantinfo.api.ContractApiService
import com.example.valorantinfo.api.CurrencyApiService
import com.example.valorantinfo.api.EventsApiService
import com.example.valorantinfo.api.FlexApiService
import com.example.valorantinfo.api.GameModeApiService
import com.example.valorantinfo.api.GearApiService
import com.example.valorantinfo.api.LevelBordersApiService
import com.example.valorantinfo.api.MapsApiService
import com.example.valorantinfo.repository.AgentDetailsRepository
import com.example.valorantinfo.repository.AgentDetailsRepositoryImpl
import com.example.valorantinfo.repository.AgentRepository
import com.example.valorantinfo.repository.AgentRepositoryImpl
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.repository.BuddyRepositoryImpl
import com.example.valorantinfo.repository.BundleRepository
import com.example.valorantinfo.repository.BundleRepositoryImpl
import com.example.valorantinfo.repository.CeremonyRepository
import com.example.valorantinfo.repository.CeremonyRepositoryImpl
import com.example.valorantinfo.repository.CompetitiveTierRepository
import com.example.valorantinfo.repository.CompetitiveTierRepositoryImpl
import com.example.valorantinfo.repository.ContentTierRepository
import com.example.valorantinfo.repository.ContentTierRepositoryImpl
import com.example.valorantinfo.repository.ContractRepository
import com.example.valorantinfo.repository.ContractRepositoryImpl
import com.example.valorantinfo.repository.CurrencyRepository
import com.example.valorantinfo.repository.CurrencyRepositoryImpl
import com.example.valorantinfo.repository.EventRepository
import com.example.valorantinfo.repository.EventRepositoryImpl
import com.example.valorantinfo.repository.FlexRepository
import com.example.valorantinfo.repository.FlexRepositoryImpl
import com.example.valorantinfo.repository.GameModeRepository
import com.example.valorantinfo.repository.GameModeRepositoryImpl
import com.example.valorantinfo.repository.GearRepository
import com.example.valorantinfo.repository.GearRepositoryImpl
import com.example.valorantinfo.repository.LevelBorderRepository
import com.example.valorantinfo.repository.LevelBorderRepositoryImpl
import com.example.valorantinfo.repository.MapRepository
import com.example.valorantinfo.repository.MapRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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
        networkInterceptor: Interceptor,
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
    fun provideContractServiceApi(retrofit: Retrofit): ContractApiService {
        return retrofit.create(ContractApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContractRepository(api: ContractApiService): ContractRepository {
        return ContractRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelProvider.NewInstanceFactory()
    }

    @Provides
    @Singleton
    fun provideCurrencyApiService(retrofit: Retrofit): CurrencyApiService {
        return retrofit.create(CurrencyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(api: CurrencyApiService): CurrencyRepository {
        return CurrencyRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideEventsApiService(retrofit: Retrofit): EventsApiService {
        return retrofit.create(EventsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideEventRepository(api: EventsApiService): EventRepository {
        return EventRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFlexApiService(retrofit: Retrofit): FlexApiService {
        return retrofit.create(FlexApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFlexRepository(api: FlexApiService): FlexRepository {
        return FlexRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGameModeApiService(retrofit: Retrofit): GameModeApiService {
        return retrofit.create(GameModeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGameModeRepository(apiService: GameModeApiService): GameModeRepository {
        return GameModeRepositoryImpl(apiService)
    }


    @Provides
    @Singleton
    fun provideGearApiService(retrofit: Retrofit): GearApiService {
        return retrofit.create(GearApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGearRepository(
        gearModeApiService: GearApiService
    ): GearRepository {
        return GearRepositoryImpl(gearModeApiService)
    }

    @Provides
    @Singleton
    fun provideLevelBorderApiService(retrofit: Retrofit): LevelBordersApiService {
        return retrofit.create(LevelBordersApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLevelBorderRepository(
        levelBordersApiService: LevelBordersApiService
    ): LevelBorderRepository {
        return LevelBorderRepositoryImpl(levelBordersApiService)
    }

    @Provides
    @Singleton
    fun provideMapApiService(retrofit: Retrofit): MapsApiService {
        return retrofit.create(MapsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMapsRepository(
        mapsApiService: MapsApiService
    ): MapRepository {
        return MapRepositoryImpl(mapsApiService)
    }
}

package com.example.valorantinfo.di

import com.example.valorantinfo.data.models.bundle.Bundle
import com.example.valorantinfo.data.models.bundle.BundleDetailResponse
import com.example.valorantinfo.data.models.bundle.BundlesListResponse
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.data.models.buddy.BuddyDetailResponse
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.data.models.buddy.BuddyResponse
import com.example.valorantinfo.data.models.ceremony.Ceremony
import com.example.valorantinfo.data.models.ceremony.CeremoniesListResponse
import com.example.valorantinfo.data.models.ceremony.CeremonyDetailResponse
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTier
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTierDetailResponse
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTiersListResponse
import com.example.valorantinfo.data.models.competitivetier.Tier
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.repository.BundleRepository
import com.example.valorantinfo.repository.CeremonyRepository
import com.example.valorantinfo.repository.CompetitiveTierRepository
import com.example.valorantinfo.utilities.Resource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
class TestRepositoryModule {

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkIntercept: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS) // Short timeout for tests
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkIntercept)
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE) // No logging in tests
        return interceptor
    }

    @Provides
    fun provideNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideBuddyRepository(): BuddyRepository {
        return object : BuddyRepository {
            override fun fetchBuddies(): Flow<Resource<BuddyResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    BuddyResponse(
                        status = 200,
                        data = listOf(
                            Buddy(
                                uuid = "test-uuid",
                                displayName = "Test Buddy",
                                isHiddenIfNotOwned = false,
                                themeUuid = null,
                                displayIcon = "https://example.com/icon.png",
                                assetPath = "path/to/buddy",
                                levels = emptyList()
                            )
                        )
                    )
                ))
            }

            override fun fetchBuddyDetails(buddyUuid: String): Flow<Resource<BuddyDetailResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    BuddyDetailResponse(
                        status = 200,
                        data = Buddy(
                            uuid = buddyUuid,
                            displayName = "Test Buddy Detail",
                            isHiddenIfNotOwned = false,
                            themeUuid = null,
                            displayIcon = "https://example.com/icon.png",
                            assetPath = "path/to/buddy",
                            levels = emptyList()
                        )
                    )
                ))
            }

            override fun fetchBuddyLevel(levelUuid: String): Flow<Resource<BuddyLevelResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    BuddyLevelResponse(
                        status = 200,
                        data = null
                    )
                ))
            }
        }
    }

    @Provides
    @Singleton
    fun provideBundleRepository(): BundleRepository {
        return object : BundleRepository {
            override suspend fun fetchBundles(): Flow<Resource<BundlesListResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    BundlesListResponse(
                        status = 200,
                        data = listOf(
                            Bundle(
                                uuid = "test-uuid",
                                displayName = "Test Bundle",
                                displayNameSubText = null,
                                description = "Test Description",
                                extraDescription = null,
                                promoDescription = null,
                                useAdditionalContext = false,
                                displayIcon = "https://example.com/icon.png",
                                displayIcon2 = "https://example.com/icon2.png",
                                logoIcon = null,
                                verticalPromoImage = "https://example.com/promo.png",
                                assetPath = "path/to/bundle"
                            )
                        )
                    )
                ))
            }

            override suspend fun fetchBundleDetails(bundleUuid: String): Flow<Resource<BundleDetailResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    BundleDetailResponse(
                        status = 200,
                        data = Bundle(
                            uuid = bundleUuid,
                            displayName = "Test Bundle Detail",
                            displayNameSubText = null,
                            description = "Test Description",
                            extraDescription = null,
                            promoDescription = null,
                            useAdditionalContext = false,
                            displayIcon = "https://example.com/icon.png",
                            displayIcon2 = "https://example.com/icon2.png",
                            logoIcon = null,
                            verticalPromoImage = "https://example.com/promo.png",
                            assetPath = "path/to/bundle"
                        )
                    )
                ))
            }
        }
    }
    
    @Provides
    @Singleton
    fun provideCeremonyRepository(): CeremonyRepository {
        return object : CeremonyRepository {
            override suspend fun fetchCeremonies(): Flow<Resource<CeremoniesListResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    CeremoniesListResponse(
                        status = 200,
                        data = listOf(
                            Ceremony(
                                uuid = "test-ceremony-uuid",
                                displayName = "ACE",
                                assetPath = "ShooterGame/Content/Ceremonies/AceCeremony_PrimaryAsset"
                            ),
                            Ceremony(
                                uuid = "test-ceremony-uuid-2",
                                displayName = "FLAWLESS",
                                assetPath = "ShooterGame/Content/Ceremonies/FlawlessCeremony_PrimaryAsset"
                            )
                        )
                    )
                ))
            }

            override suspend fun fetchCeremonyDetail(ceremonyUuid: String): Flow<Resource<CeremonyDetailResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    CeremonyDetailResponse(
                        status = 200,
                        data = Ceremony(
                            uuid = ceremonyUuid,
                            displayName = "Test Ceremony Detail",
                            assetPath = "ShooterGame/Content/Ceremonies/TestCeremony_PrimaryAsset"
                        )
                    )
                ))
            }
        }
    }

    @Provides
    @Singleton
    fun provideCompetitiveTierRepository(): CompetitiveTierRepository {
        return object : CompetitiveTierRepository {
            override suspend fun fetchCompetitiveTiers(): Flow<Resource<CompetitiveTiersListResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    CompetitiveTiersListResponse(
                        status = 200,
                        data = listOf(
                            CompetitiveTier(
                                uuid = "test-competitive-tier-uuid",
                                assetObjectName = "Episode1_CompetitiveTierDataTable",
                                tiers = listOf(
                                    Tier(
                                        tier = 0,
                                        tierName = "UNRANKED",
                                        division = "ECompetitiveDivision::UNRANKED",
                                        divisionName = "UNRANKED",
                                        color = "ffffffff",
                                        backgroundColor = "00000000",
                                        smallIcon = "https://example.com/small-icon.png",
                                        largeIcon = "https://example.com/large-icon.png",
                                        rankTriangleDownIcon = null,
                                        rankTriangleUpIcon = null
                                    )
                                ),
                                assetPath = "ShooterGame/Content/UI/Screens/Shared/Competitive/Episode1_CompetitiveTierDataTable"
                            )
                        )
                    )
                ))
            }

            override suspend fun fetchCompetitiveTierDetail(competitiveTierUuid: String): Flow<Resource<CompetitiveTierDetailResponse>> = flow {
                emit(Resource.Loading())
                emit(Resource.Success(
                    CompetitiveTierDetailResponse(
                        status = 200,
                        data = CompetitiveTier(
                            uuid = competitiveTierUuid,
                            assetObjectName = "Episode1_CompetitiveTierDataTable",
                            tiers = listOf(
                                Tier(
                                    tier = 0,
                                    tierName = "UNRANKED",
                                    division = "ECompetitiveDivision::UNRANKED",
                                    divisionName = "UNRANKED",
                                    color = "ffffffff",
                                    backgroundColor = "00000000",
                                    smallIcon = "https://example.com/small-icon.png",
                                    largeIcon = "https://example.com/large-icon.png",
                                    rankTriangleDownIcon = null,
                                    rankTriangleUpIcon = null
                                )
                            ),
                            assetPath = "ShooterGame/Content/UI/Screens/Shared/Competitive/Episode1_CompetitiveTierDataTable"
                        )
                    )
                ))
            }
        }
    }
} 
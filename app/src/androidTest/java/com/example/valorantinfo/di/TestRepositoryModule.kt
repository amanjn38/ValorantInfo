package com.example.valorantinfo.di

import com.example.valorantinfo.data.models.bundle.Bundle
import com.example.valorantinfo.data.models.bundle.BundleDetailResponse
import com.example.valorantinfo.data.models.bundle.BundlesListResponse
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.data.models.buddy.BuddyDetailResponse
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.data.models.buddy.BuddyResponse
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.repository.BundleRepository
import com.example.valorantinfo.utilities.Resource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
class TestRepositoryModule {

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
} 
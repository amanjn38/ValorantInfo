package com.example.valorantinfo.repository

import com.example.valorantinfo.api.BuddyApiService
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.utilities.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BuddyRepositoryImplSimpleTest {

    private lateinit var repository: BuddyRepositoryImpl
    private lateinit var apiService: BuddyApiService

    private val sampleLevelUuid = "level-uuid"
    private val sampleLevel = BuddyLevel(
        uuid = sampleLevelUuid,
        charmLevel = 1,
        hideIfNotOwned = false,
        displayName = "Test Level",
        displayIcon = "https://example.com/icon.png",
        assetPath = "path/to/asset",
    )
    private val sampleLevelResponse = BuddyLevelResponse(
        status = 200,
        data = sampleLevel,
    )

    @Before
    fun setup() {
        apiService = mockk()
        repository = BuddyRepositoryImpl(apiService)
    }

    @Test
    fun `fetchBuddyLevel returns success when API call succeeds`() = runTest {
        // Given
        coEvery { apiService.getBuddyLevel(sampleLevelUuid) } returns Response.success(sampleLevelResponse)

        // When
        val flow = repository.fetchBuddyLevel(sampleLevelUuid)
        val emissions = flow.toList()

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Success)

        val successEmission = emissions[1] as Resource.Success
        assertEquals(sampleLevelResponse, successEmission.data)
    }

    @Test
    fun `fetchBuddyLevel returns error when API call fails`() = runTest {
        // Given
        val errorBody = mockk<ResponseBody>()
        coEvery { apiService.getBuddyLevel(sampleLevelUuid) } returns Response.error(404, errorBody)

        // When
        val flow = repository.fetchBuddyLevel(sampleLevelUuid)
        val emissions = flow.toList()

        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
    }
}

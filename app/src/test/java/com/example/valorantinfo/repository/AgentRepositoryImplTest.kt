package com.example.valorantinfo.repository

import app.cash.turbine.test
import com.example.valorantinfo.api.AgentApiService
import com.example.valorantinfo.data.models.agent.Agent
import com.example.valorantinfo.data.models.agent.AgentResponse
import com.example.valorantinfo.utilities.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class AgentRepositoryImplTest {

    private lateinit var repository: AgentRepositoryImpl
    private lateinit var apiService: AgentApiService

    @Before
    fun setup() {
        apiService = mockk()
        repository = AgentRepositoryImpl(apiService)
    }

    @Test
    fun `fetchAgents emits Loading and then Success when API call is successful`() = runTest {
        // Given
        val mockResponse = AgentResponse(
            status = 200,
            data = listOf(
                Agent(
                    uuid = "1",
                    displayName = "Jett",
                    description = "Swift assassin",
                    displayIcon = "jett_icon.png",
                    fullPortrait = "jett_portrait.png",
                    background = "jett_background.png",
                    role = null,
                    abilities = emptyList(),
                    isPlayableCharacter = true,
                    developerName = "Jett",
                    releaseDate = null,
                    characterTags = null,
                    displayIconSmall = "jett_icon_small.png",
                    bustPortrait = null,
                    killfeedPortrait = null,
                    backgroundGradientColors = emptyList(),
                    assetPath = "Jett_Path",
                    isFullPortraitRightFacing = false,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null
                )
            )
        )
        
        coEvery { apiService.getAgents() } returns Response.success(mockResponse)

        // When & Then
        repository.fetchAgents().test {
            // First emission should be Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            
            // Second emission should be Success with the mock data
            val successResource = awaitItem()
            assertThat(successResource).isInstanceOf(Resource.Success::class.java)
            assertThat((successResource as Resource.Success).data).isEqualTo(mockResponse)
            
            // No more emissions
            awaitComplete()
        }
    }

    @Test
    fun `fetchAgents emits Loading and then Error when API call returns an error response`() = runTest {
        // Given
        coEvery { apiService.getAgents() } returns Response.error(
            404, 
            "Not found".toResponseBody(null)
        )

        // When & Then
        repository.fetchAgents().test {
            // First emission should be Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            
            // Second emission should be Error
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat((errorResource as Resource.Error).message).contains("Error code: 404")
            
            // No more emissions
            awaitComplete()
        }
    }

    @Test
    fun `fetchAgents emits Loading and then Error when empty response body is returned`() = runTest {
        // Given
        coEvery { apiService.getAgents() } returns Response.success(null)

        // When & Then
        repository.fetchAgents().test {
            // First emission should be Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            
            // Second emission should be Error
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat((errorResource as Resource.Error).message).isEqualTo("Empty response")
            
            // No more emissions
            awaitComplete()
        }
    }

    @Test
    fun `fetchAgents emits Loading and then Error when network exception occurs`() = runTest {
        // Given
        coEvery { apiService.getAgents() } throws IOException("Network error")

        // When & Then
        repository.fetchAgents().test {
            // First emission should be Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            
            // Second emission should be Error
            val errorResource = awaitItem()
            assertThat(errorResource).isInstanceOf(Resource.Error::class.java)
            assertThat((errorResource as Resource.Error).message).contains("Network error")
            
            // No more emissions
            awaitComplete()
        }
    }
} 
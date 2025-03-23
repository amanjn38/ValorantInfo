package com.example.valorantinfo.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.utilities.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)  // Remove this if using JUnit5
class BuddyDetailsViewModelSimpleTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher() // ✅ Updated
    private lateinit var repository: BuddyRepository
    private lateinit var viewModel: BuddyDetailsViewModel

    private val sampleLevelUuid = "level-uuid"
    private val sampleLevel = BuddyLevel(
        uuid = sampleLevelUuid,
        charmLevel = 1,
        hideIfNotOwned = false,
        displayName = "Test Level",
        displayIcon = "https://example.com/icon.png",
        assetPath = "path/to/asset"
    )
    private val sampleLevelResponse = BuddyLevelResponse(
        status = 200,
        data = sampleLevel
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = BuddyDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchBuddyLevel sets selectedLevel to Loading then Success`() = runTest {
        // Given
        coEvery { repository.fetchBuddyLevel(sampleLevelUuid) } returns flowOf(
            Resource.Loading(),
            Resource.Success(sampleLevelResponse)
        )

        // When
        viewModel.fetchBuddyLevel(sampleLevelUuid)

        // Then
        val state = viewModel.selectedLevel.value
        assert(state is Resource.Success)
        assertEquals(sampleLevel, (state as Resource.Success).data)
    }

    @Test
    fun `fetchBuddyLevel sets selectedLevel to Loading then Error`() = runTest {
        // Given
        val errorMessage = "Error fetching buddy level"
        coEvery { repository.fetchBuddyLevel(sampleLevelUuid) } returns flowOf(
            Resource.Loading(),
            Resource.Error(errorMessage)
        )

        // When
        viewModel.fetchBuddyLevel(sampleLevelUuid)

        // Then
        val state = viewModel.selectedLevel.value
        assert(state is Resource.Error)
        assertEquals(errorMessage, (state as Resource.Error).message)
    }
}

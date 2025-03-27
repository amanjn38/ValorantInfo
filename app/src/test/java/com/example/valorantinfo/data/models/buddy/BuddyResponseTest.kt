package com.example.valorantinfo.data.models.buddy

import org.junit.Assert.assertEquals
import org.junit.Test

class BuddyResponseTest {

    @Test
    fun `BuddyResponse has correct properties`() {
        // Given
        val status = 200
        val buddies = listOf(
            Buddy(
                uuid = "buddy-uuid-1",
                displayName = "Buddy 1",
                isHiddenIfNotOwned = false,
                themeUuid = null,
                displayIcon = "https://example.com/buddy1.png",
                assetPath = "path/to/buddy1",
                levels = emptyList(),
            ),
            Buddy(
                uuid = "buddy-uuid-2",
                displayName = "Buddy 2",
                isHiddenIfNotOwned = true,
                themeUuid = "theme-uuid",
                displayIcon = "https://example.com/buddy2.png",
                assetPath = "path/to/buddy2",
                levels = listOf(
                    BuddyLevel(
                        uuid = "level-uuid",
                        charmLevel = 1,
                        hideIfNotOwned = false,
                        displayName = "Level 1",
                        displayIcon = "https://example.com/level.png",
                        assetPath = "path/to/level",
                    ),
                ),
            ),
        )

        // When
        val response = BuddyResponse(
            status = status,
            data = buddies,
        )

        // Then
        assertEquals(status, response.status)
        assertEquals(buddies, response.data)
        assertEquals(2, response.data.size)
        assertEquals("buddy-uuid-1", response.data[0].uuid)
        assertEquals("buddy-uuid-2", response.data[1].uuid)
        assertEquals(0, response.data[0].levels.size)
        assertEquals(1, response.data[1].levels.size)
    }

    @Test
    fun `BuddyResponse can have empty data`() {
        // Given
        val status = 200
        val buddies = emptyList<Buddy>()

        // When
        val response = BuddyResponse(
            status = status,
            data = buddies,
        )

        // Then
        assertEquals(status, response.status)
        assertEquals(buddies, response.data)
        assertEquals(0, response.data.size)
    }
}

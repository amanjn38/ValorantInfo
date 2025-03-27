package com.example.valorantinfo.data.models.buddy

import org.junit.Assert.assertEquals
import org.junit.Test

class BuddyLevelResponseTest {

    @Test
    fun `BuddyLevelResponse has correct properties`() {
        // Given
        val status = 200
        val buddyLevel = BuddyLevel(
            uuid = "test-uuid",
            charmLevel = 1,
            hideIfNotOwned = false,
            displayName = "Test Level",
            displayIcon = "https://example.com/icon.png",
            assetPath = "path/to/asset",
        )

        // When
        val response = BuddyLevelResponse(
            status = status,
            data = buddyLevel,
        )

        // Then
        assertEquals(status, response.status)
        assertEquals(buddyLevel, response.data)
        assertEquals("test-uuid", response.data?.uuid)
        assertEquals(1, response.data?.charmLevel)
        assertEquals("Test Level", response.data?.displayName)
    }
}

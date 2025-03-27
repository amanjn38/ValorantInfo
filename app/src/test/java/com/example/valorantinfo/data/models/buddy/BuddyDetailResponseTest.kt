package com.example.valorantinfo.data.models.buddy

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// class BuddyDetailResponseTest {
//
//    @Test
//    fun `BuddyDetailResponse has correct properties`() {
//        // Given
//        val status = 200
//        val buddyLevel = BuddyLevel(
//            uuid = "level-uuid",
//            charmLevel = 1,
//            hideIfNotOwned = false,
//            displayName = "Level 1",
//            displayIcon = "https://example.com/level.png",
//            assetPath = "path/to/level"
//        )
//
//        val buddy = Buddy(
//            uuid = "buddy-uuid",
//            displayName = "Test Buddy",
//            isHiddenIfNotOwned = false,
//            themeUuid = null,
//            displayIcon = "https://example.com/buddy.png",
//            assetPath = "path/to/buddy",
//            levels = listOf(buddyLevel)
//        )
//
//        // When
//        val response = BuddyDetailResponse(
//            status = status,
//            data = buddy
//        )
//
//        // Then
//        assertEquals(status, response.status)
//        assertEquals(buddy, response.data)
//        assertEquals("buddy-uuid", response.data?.uuid)
//        assertEquals("Test Buddy", response.data?.displayName)
//        assertEquals(1, response.data?.levels?.size ?: "NA")
//        assertEquals("level-uuid", response.data?.levels?.get(0)?.uuid ?: "NA")
//    }
// }

@RunWith(JUnit4::class) // ✅ Add this if missing
class BuddyDetailResponseTest {

    @Test
    fun `BuddyDetailResponse has correct properties`() {
        // Given
        val status = 200
        val buddyLevel = BuddyLevel(
            uuid = "level-uuid",
            charmLevel = 1,
            hideIfNotOwned = false,
            displayName = "Level 1",
            displayIcon = "https://example.com/level.png",
            assetPath = "path/to/level",
        )

        val buddy = Buddy(
            uuid = "buddy-uuid",
            displayName = "Test Buddy",
            isHiddenIfNotOwned = false,
            themeUuid = null,
            displayIcon = "https://example.com/buddy.png",
            assetPath = "path/to/buddy",
            levels = listOf(buddyLevel),
        )

        // When
        val response = BuddyDetailResponse(
            status = status,
            data = buddy,
        )

        // Then
        assertEquals(status, response.status)
        assertEquals(buddy, response.data)
        assertEquals("buddy-uuid", response.data?.uuid)
        assertEquals("Test Buddy", response.data?.displayName)
        assertEquals(1, response.data?.levels?.size ?: "NA")
        assertEquals("level-uuid", response.data?.levels?.get(0)?.uuid ?: "NA")
    }
}

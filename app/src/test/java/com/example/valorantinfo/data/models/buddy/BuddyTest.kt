package com.example.valorantinfo.data.models.buddy

import org.junit.Assert.assertEquals
import org.junit.Test

class BuddyTest {

    @Test
    fun `Buddy has correct properties`() {
        // Given
        val uuid = "test-buddy-uuid"
        val displayName = "Test Buddy"
        val isHiddenIfNotOwned = false
        val themeUuid = "theme-uuid"
        val displayIcon = "https://example.com/buddy.png"
        val assetPath = "path/to/buddy"
        val levels = listOf(
            BuddyLevel(
                uuid = "level-uuid-1",
                charmLevel = 1,
                hideIfNotOwned = false,
                displayName = "Level 1",
                displayIcon = "https://example.com/level1.png",
                assetPath = "path/to/level1",
            ),
            BuddyLevel(
                uuid = "level-uuid-2",
                charmLevel = 2,
                hideIfNotOwned = true,
                displayName = "Level 2",
                displayIcon = "https://example.com/level2.png",
                assetPath = "path/to/level2",
            ),
        )

        // When
        val buddy = Buddy(
            uuid = uuid,
            displayName = displayName,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
            themeUuid = themeUuid,
            displayIcon = displayIcon,
            assetPath = assetPath,
            levels = levels,
        )

        // Then
        assertEquals(uuid, buddy.uuid)
        assertEquals(displayName, buddy.displayName)
        assertEquals(isHiddenIfNotOwned, buddy.isHiddenIfNotOwned)
        assertEquals(themeUuid, buddy.themeUuid)
        assertEquals(displayIcon, buddy.displayIcon)
        assertEquals(assetPath, buddy.assetPath)
        assertEquals(levels, buddy.levels)
        assertEquals(2, buddy.levels.size)
        assertEquals("level-uuid-1", buddy.levels[0].uuid)
        assertEquals("level-uuid-2", buddy.levels[1].uuid)
    }

    @Test
    fun `Buddy can have null themeUuid`() {
        // Given
        val uuid = "test-buddy-uuid"
        val displayName = "Test Buddy"
        val isHiddenIfNotOwned = false
        val themeUuid = null
        val displayIcon = "https://example.com/buddy.png"
        val assetPath = "path/to/buddy"
        val levels = emptyList<BuddyLevel>()

        // When
        val buddy = Buddy(
            uuid = uuid,
            displayName = displayName,
            isHiddenIfNotOwned = isHiddenIfNotOwned,
            themeUuid = themeUuid,
            displayIcon = displayIcon,
            assetPath = assetPath,
            levels = levels,
        )

        // Then
        assertEquals(uuid, buddy.uuid)
        assertEquals(displayName, buddy.displayName)
        assertEquals(isHiddenIfNotOwned, buddy.isHiddenIfNotOwned)
        assertEquals(null, buddy.themeUuid)
        assertEquals(displayIcon, buddy.displayIcon)
        assertEquals(assetPath, buddy.assetPath)
        assertEquals(0, buddy.levels.size)
    }
}

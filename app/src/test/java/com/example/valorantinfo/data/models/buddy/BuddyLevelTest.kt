package com.example.valorantinfo.data.models.buddy

import org.junit.Assert.assertEquals
import org.junit.Test

class BuddyLevelTest {

    @Test
    fun `BuddyLevel has correct properties`() {
        // Given
        val uuid = "test-uuid"
        val charmLevel = 1
        val hideIfNotOwned = false
        val displayName = "Test Level"
        val displayIcon = "https://example.com/icon.png"
        val assetPath = "path/to/asset"
        
        // When
        val buddyLevel = BuddyLevel(
            uuid = uuid,
            charmLevel = charmLevel,
            hideIfNotOwned = hideIfNotOwned,
            displayName = displayName,
            displayIcon = displayIcon,
            assetPath = assetPath
        )
        
        // Then
        assertEquals(uuid, buddyLevel.uuid)
        assertEquals(charmLevel, buddyLevel.charmLevel)
        assertEquals(hideIfNotOwned, buddyLevel.hideIfNotOwned)
        assertEquals(displayName, buddyLevel.displayName)
        assertEquals(displayIcon, buddyLevel.displayIcon)
        assertEquals(assetPath, buddyLevel.assetPath)
    }
    
    @Test
    fun `test BuddyLevel copy function works correctly`() {
        // Given
        val original = BuddyLevel(
            uuid = "original",
            charmLevel = 1,
            hideIfNotOwned = false,
            displayName = "Original",
            displayIcon = "original.png",
            assetPath = "original/path"
        )
        
        // When
        val copy = original.copy(
            charmLevel = 2,
            displayName = "Copy"
        )
        
        // Then
        assertEquals("original", copy.uuid)
        assertEquals(2, copy.charmLevel)
        assertEquals(false, copy.hideIfNotOwned)
        assertEquals("Copy", copy.displayName)
        assertEquals("original.png", copy.displayIcon)
        assertEquals("original/path", copy.assetPath)
    }
} 
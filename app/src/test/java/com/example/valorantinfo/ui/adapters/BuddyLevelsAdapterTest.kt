package com.example.valorantinfo.ui.adapters

import com.example.valorantinfo.data.models.buddy.BuddyLevel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BuddyLevelsAdapterTest {

    private val level1 = BuddyLevel(
        uuid = "level-uuid-1",
        charmLevel = 1,
        hideIfNotOwned = false,
        displayName = "Level 1",
        displayIcon = "https://example.com/level1.png",
        assetPath = "path/to/level1"
    )
    
    private val level2 = BuddyLevel(
        uuid = "level-uuid-2",
        charmLevel = 2,
        hideIfNotOwned = true,
        displayName = "Level 2",
        displayIcon = "https://example.com/level2.png",
        assetPath = "path/to/level2"
    )
    
    @Test
    fun `diffCallback identifies same items by uuid`() {
        // Given
        val callback = BuddyLevelsAdapter.LevelDiffCallback()
        val sameLevelDifferentContent = BuddyLevel(
            uuid = "level-uuid-1",
            charmLevel = 5,
            hideIfNotOwned = true,
            displayName = "Updated Level",
            displayIcon = "https://example.com/new.png",
            assetPath = "new/path"
        )
        
        // When & Then
        assertTrue(callback.areItemsTheSame(level1, sameLevelDifferentContent))
        assertFalse(callback.areContentsTheSame(level1, sameLevelDifferentContent))
    }
    
    @Test
    fun `diffCallback identifies different items by uuid`() {
        // Given
        val callback = BuddyLevelsAdapter.LevelDiffCallback()
        
        // When & Then
        assertFalse(callback.areItemsTheSame(level1, level2))
    }
    
    @Test
    fun `diffCallback identifies same content`() {
        // Given
        val callback = BuddyLevelsAdapter.LevelDiffCallback()
        val exactCopy = BuddyLevel(
            uuid = "level-uuid-1",
            charmLevel = 1,
            hideIfNotOwned = false,
            displayName = "Level 1",
            displayIcon = "https://example.com/level1.png",
            assetPath = "path/to/level1"
        )
        
        // When & Then
        assertTrue(callback.areContentsTheSame(level1, exactCopy))
    }
    
    @Test
    fun `onClickListener is invoked with correct level`() {
        // Given
        var clickedLevel: BuddyLevel? = null
        val adapter = BuddyLevelsAdapter { level -> clickedLevel = level }
        
        // When
        adapter.onLevelClick?.let { it(level1) }
        
        // Then
        assertTrue(clickedLevel == level1)
    }
} 
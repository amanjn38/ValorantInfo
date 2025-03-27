package com.example.valorantinfo.ui.adapters

import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BuddiesAdapterTest {

    private val sampleLevel = BuddyLevel(
        uuid = "level-uuid",
        charmLevel = 1,
        hideIfNotOwned = false,
        displayName = "Test Level",
        displayIcon = "https://example.com/level.png",
        assetPath = "path/to/level",
    )

    private val buddy1 = Buddy(
        uuid = "buddy-uuid-1",
        displayName = "Buddy 1",
        isHiddenIfNotOwned = false,
        themeUuid = null,
        displayIcon = "https://example.com/buddy1.png",
        assetPath = "path/to/buddy1",
        levels = listOf(sampleLevel),
    )

    private val buddy2 = Buddy(
        uuid = "buddy-uuid-2",
        displayName = "Buddy 2",
        isHiddenIfNotOwned = true,
        themeUuid = "theme-uuid",
        displayIcon = "https://example.com/buddy2.png",
        assetPath = "path/to/buddy2",
        levels = listOf(sampleLevel),
    )

    @Test
    fun `diffCallback identifies same items by uuid`() {
        // Given
        val callback = BuddiesAdapter.BuddyDiffCallback()
        val sameBuddyDifferentContent = Buddy(
            uuid = "buddy-uuid-1",
            displayName = "Updated Name",
            isHiddenIfNotOwned = true,
            themeUuid = "new-theme",
            displayIcon = "https://example.com/new.png",
            assetPath = "new/path",
            levels = emptyList(),
        )

        // When & Then
        assertTrue(callback.areItemsTheSame(buddy1, sameBuddyDifferentContent))
        assertFalse(callback.areContentsTheSame(buddy1, sameBuddyDifferentContent))
    }

    @Test
    fun `diffCallback identifies different items by uuid`() {
        // Given
        val callback = BuddiesAdapter.BuddyDiffCallback()

        // When & Then
        assertFalse(callback.areItemsTheSame(buddy1, buddy2))
    }

    @Test
    fun `diffCallback identifies same content`() {
        // Given
        val callback = BuddiesAdapter.BuddyDiffCallback()
        val exactCopy = Buddy(
            uuid = "buddy-uuid-1",
            displayName = "Buddy 1",
            isHiddenIfNotOwned = false,
            themeUuid = null,
            displayIcon = "https://example.com/buddy1.png",
            assetPath = "path/to/buddy1",
            levels = listOf(sampleLevel),
        )

        // When & Then
        assertTrue(callback.areContentsTheSame(buddy1, exactCopy))
    }
}

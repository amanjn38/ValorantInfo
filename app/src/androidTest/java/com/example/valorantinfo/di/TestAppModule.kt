package com.example.valorantinfo.di

import com.example.valorantinfo.data.models.agent.Agent
import com.example.valorantinfo.data.models.agent.AgentResponse
import com.example.valorantinfo.data.models.agent.Role as AgentRole
import com.example.valorantinfo.data.models.agentDetails.Ability
import com.example.valorantinfo.data.models.agentDetails.AgentDetails
import com.example.valorantinfo.data.models.agentDetails.AgentDetailsResponse
import com.example.valorantinfo.data.models.agentDetails.Role as AgentDetailsRole
import com.example.valorantinfo.data.models.agentDetails.VoiceLine
import com.example.valorantinfo.repository.AgentDetailsRepository
import com.example.valorantinfo.repository.AgentRepository
import com.example.valorantinfo.utilities.Resource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideAgentRepository(): AgentRepository {
        return FakeAgentRepository()
    }
    
    @Provides
    @Singleton
    fun provideAgentDetailsRepository(): AgentDetailsRepository {
        return FakeAgentDetailsRepository()
    }
}

class FakeAgentRepository : AgentRepository {
    override fun fetchAgents(): Flow<Resource<AgentResponse>> = flow {
        emit(Resource.Loading())
        // Simulate a delay
        kotlinx.coroutines.delay(500)
        emit(Resource.Success(createFakeAgentResponse()))
    }

    private fun createFakeAgentResponse(): AgentResponse {
        return AgentResponse(
            status = 200,
            data = listOf(
                Agent(
                    uuid = "1",
                    displayName = "Jett",
                    description = "Jett description",
                    displayIcon = "jett_icon.png",
                    fullPortrait = "jett_portrait.png",
                    background = "jett_background.png",
                    role = AgentRole(
                        uuid = "r1",
                        displayName = "Duelist",
                        description = "Duelist description",
                        displayIcon = "duelist_icon.png",
                        assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset"
                    ),
                    abilities = emptyList(),
                    isPlayableCharacter = true,
                    developerName = "Jett",
                    releaseDate = "2020-04-07",
                    characterTags = listOf("Fast", "Mobility"),
                    displayIconSmall = "jett_icon_small.png",
                    bustPortrait = "jett_bust.png",
                    killfeedPortrait = "jett_killfeed.png",
                    backgroundGradientColors = listOf("#12345", "#67890"),
                    assetPath = "Agents/Jett",
                    isFullPortraitRightFacing = false,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null
                ),
                Agent(
                    uuid = "2",
                    displayName = "Phoenix",
                    description = "Phoenix description",
                    displayIcon = "phoenix_icon.png",
                    fullPortrait = "phoenix_portrait.png",
                    background = "phoenix_background.png",
                    role = AgentRole(
                        uuid = "r1",
                        displayName = "Duelist",
                        description = "Duelist description",
                        displayIcon = "duelist_icon.png",
                        assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset"
                    ),
                    abilities = emptyList(),
                    isPlayableCharacter = true,
                    developerName = "Phoenix",
                    releaseDate = "2020-04-07",
                    characterTags = listOf("Fire", "Flash"),
                    displayIconSmall = "phoenix_icon_small.png",
                    bustPortrait = "phoenix_bust.png",
                    killfeedPortrait = "phoenix_killfeed.png",
                    backgroundGradientColors = listOf("#ABCDE", "#FGHIJ"),
                    assetPath = "Agents/Phoenix",
                    isFullPortraitRightFacing = false,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null
                )
            )
        )
    }
}

class FakeAgentDetailsRepository : AgentDetailsRepository {
    override fun fetchAgentDetails(agentUuid: String): Flow<Resource<AgentDetailsResponse>> = flow {
        emit(Resource.Loading())
        // Simulate a delay
        kotlinx.coroutines.delay(500)
        emit(Resource.Success(createFakeAgentDetailsResponse(agentUuid)))
    }
    
    private fun createFakeAgentDetailsResponse(agentUuid: String): AgentDetailsResponse {
        return AgentDetailsResponse(
            status = 200,
            data = when (agentUuid) {
                "1" -> AgentDetails(
                    uuid = "1",
                    displayName = "Jett",
                    description = "Jett description",
                    displayIcon = "jett_icon.png",
                    fullPortrait = "jett_portrait.png",
                    fullPortraitV2 = "jett_portrait_v2.png",
                    background = "jett_background.png",
                    role = AgentDetailsRole(
                        uuid = "r1",
                        displayName = "Duelist",
                        description = "Duelist description",
                        displayIcon = "duelist_icon.png",
                        assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset"
                    ),
                    abilities = listOf(
                        Ability(
                            slot = "Q",
                            displayName = "Updraft",
                            description = "Updraft description",
                            displayIcon = "updraft_icon.png"
                        ),
                        Ability(
                            slot = "E",
                            displayName = "Tailwind",
                            description = "Tailwind description",
                            displayIcon = "tailwind_icon.png"
                        )
                    ),
                    developerName = "Jett",
                    characterTags = listOf("Fast", "Mobility"),
                    displayIconSmall = "jett_icon_small.png",
                    bustPortrait = "jett_bust_portrait.png",
                    releaseDate = "2020-04-07",
                    killfeedPortrait = "jett_killfeed.png",
                    backgroundGradientColors = listOf("#12345", "#67890"),
                    assetPath = "Agents/Jett",
                    isFullPortraitRightFacing = false,
                    isPlayableCharacter = true,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null
                )
                "2" -> AgentDetails(
                    uuid = "2",
                    displayName = "Phoenix",
                    description = "Phoenix description",
                    displayIcon = "phoenix_icon.png",
                    fullPortrait = "phoenix_portrait.png",
                    fullPortraitV2 = "phoenix_portrait_v2.png",
                    background = "phoenix_background.png",
                    role = AgentDetailsRole(
                        uuid = "r1",
                        displayName = "Duelist",
                        description = "Duelist description",
                        displayIcon = "duelist_icon.png",
                        assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset"
                    ),
                    abilities = listOf(
                        Ability(
                            slot = "Q",
                            displayName = "Curveball",
                            description = "Curveball description",
                            displayIcon = "curveball_icon.png"
                        ),
                        Ability(
                            slot = "E",
                            displayName = "Hot Hands",
                            description = "Hot Hands description",
                            displayIcon = "hot_hands_icon.png"
                        )
                    ),
                    developerName = "Phoenix",
                    characterTags = listOf("Fire", "Flash"),
                    displayIconSmall = "phoenix_icon_small.png",
                    bustPortrait = "phoenix_bust_portrait.png",
                    releaseDate = "2020-04-07",
                    killfeedPortrait = "phoenix_killfeed.png",
                    backgroundGradientColors = listOf("#ABCDE", "#FGHIJ"),
                    assetPath = "Agents/Phoenix",
                    isFullPortraitRightFacing = false,
                    isPlayableCharacter = true,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null
                )
                else -> null
            },
            error = if (agentUuid != "1" && agentUuid != "2") "Agent not found" else null
        )
    }
} 
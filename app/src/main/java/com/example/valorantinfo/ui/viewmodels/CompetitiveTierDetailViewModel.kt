package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTierDetailResponse
import com.example.valorantinfo.repository.CompetitiveTierRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitiveTierDetailViewModel @Inject constructor(
    private val repository: CompetitiveTierRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _competitiveTierDetailState = MutableStateFlow<Resource<CompetitiveTierDetailResponse>>(Resource.Loading())
    val competitiveTierDetailState: StateFlow<Resource<CompetitiveTierDetailResponse>> = _competitiveTierDetailState

    private var competitiveTierUuid: String? = null
    private var competitiveTierName: String? = null

    init {
        savedStateHandle.get<String>("competitiveTierUuid")?.let { uuid ->
            competitiveTierUuid = uuid
            getCompetitiveTierDetail(uuid)
        }

        savedStateHandle.get<String>("competitiveTierName")?.let { name ->
            competitiveTierName = name
        }
    }

    fun getCompetitiveTierDetail(uuid: String) {
        viewModelScope.launch {
            repository.fetchCompetitiveTierDetail(uuid).collect { result ->
                _competitiveTierDetailState.value = result
            }
        }
    }

    fun getCompetitiveTierName(): String {
        return competitiveTierName ?: "Competitive Tier"
    }
}

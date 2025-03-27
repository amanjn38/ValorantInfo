package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.ceremony.CeremonyDetailResponse
import com.example.valorantinfo.repository.CeremonyRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CeremonyDetailViewModel @Inject constructor(
    private val repository: CeremonyRepository,
) : ViewModel() {

    private val _ceremonyDetail = MutableStateFlow<Resource<CeremonyDetailResponse>>(Resource.Loading())
    val ceremonyDetail: StateFlow<Resource<CeremonyDetailResponse>> = _ceremonyDetail

    fun fetchCeremonyDetail(ceremonyUuid: String) {
        viewModelScope.launch {
            repository.fetchCeremonyDetail(ceremonyUuid).onEach { result ->
                _ceremonyDetail.value = result
            }.launchIn(viewModelScope)
        }
    }
}

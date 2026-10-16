package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacancyViewModel(
    private val interactor: JobsInteractor
) : ViewModel() {
    private val mutableState = MutableLiveData<VacancyState>()
    val state: LiveData<VacancyState> = mutableState

    fun loadVacancy(id: String) {
        viewModelScope.launch {
            mutableState.value = VacancyState.Loading
            interactor.getVacancyDetails(id).collect { result ->
                mutableState.value = when (result) {
                    is Resource.Success -> result.data
                        ?.let(VacancyState::Content)
                        ?: VacancyState.Error(ErrorType.SERVER_ERROR)

                    is Resource.Error -> VacancyState.Error(
                        result.code ?: ErrorType.SERVER_ERROR
                    )
                }
            }
        }
    }
}

sealed interface VacancyState {
    data object Loading : VacancyState
    data class Content(val vacancy: VacancyDetail) : VacancyState
    data class Error(val error: ErrorType) : VacancyState
}

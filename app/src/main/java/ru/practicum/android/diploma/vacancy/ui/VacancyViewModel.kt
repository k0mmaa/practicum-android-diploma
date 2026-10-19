package ru.practicum.android.diploma.vacancy.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacancyViewModel(
    private val interactor: JobsInteractor
) : ViewModel() {
    private companion object {
        const val TAG = "VacancyViewModel"
    }

    private val mutableState = MutableLiveData<VacancyState>()
    val state: LiveData<VacancyState> = mutableState
    private var loadJob: Job? = null
    private var requestedVacancyId: String? = null

    fun loadVacancy(id: String, forceReload: Boolean = false) {
        val vacancyId = id.trim()
        if (vacancyId.isEmpty()) {
            loadJob?.cancel()
            requestedVacancyId = null
            mutableState.value = VacancyState.Error(ErrorType.BAD_REQUEST)
            return
        }
        if (!forceReload && vacancyId == requestedVacancyId && mutableState.value != null) {
            return
        }

        requestedVacancyId = vacancyId
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            mutableState.value = VacancyState.Loading
            try {
                interactor.getVacancyDetails(vacancyId).collect { result ->
                    mutableState.value = when (result) {
                        is Resource.Success -> result.data
                            ?.let(VacancyState::Content)
                            ?.takeIf { it.vacancy.id == vacancyId }
                            ?: VacancyState.Error(ErrorType.SERVER_ERROR)

                        is Resource.Error -> VacancyState.Error(
                            result.code ?: ErrorType.SERVER_ERROR
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(TAG, "Unexpected failure while loading vacancy $vacancyId", exception)
                mutableState.value = VacancyState.Error(ErrorType.SERVER_ERROR)
            }
        }
    }
}

sealed interface VacancyState {
    data object Loading : VacancyState
    data class Content(val vacancy: VacancyDetail) : VacancyState
    data class Error(val error: ErrorType) : VacancyState
}

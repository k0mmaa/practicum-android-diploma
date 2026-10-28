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
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.models.vacancy.Phone
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyInteractor
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class VacancyViewModel(
    private val jobInteractor: JobsInteractor,
    private val favoriteInteractor: FavoriteVacancyInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private companion object {
        const val TAG = "VacancyViewModel"
    }

    private val mutableState = MutableLiveData<VacancyState>()
    val state: LiveData<VacancyState> = mutableState
    private val favoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavorite(): LiveData<Boolean> = favoriteLiveData
    private var loadJob: Job? = null
    private var requestedVacancyId: String? = null
    private var favoriteJob: Job? = null

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
        favoriteJob?.cancel()
        loadJob?.cancel()

        favoriteJob = viewModelScope.launch {
            favoriteInteractor.isVacancyFavorite(vacancyId)
                .collect { result ->
                    favoriteLiveData.value = result
                }
        }

        loadJob = viewModelScope.launch {
            try {

                mutableState.value = VacancyState.Loading
                val vacancy = favoriteInteractor.getVacancy(id)

                if (vacancy != null) {
                    mutableState.value = VacancyState.Content(vacancy)
                }else{
                    jobInteractor.getVacancyDetails(vacancyId).collect { result ->
                        mutableState.value = when (result) {
                            is Resource.Success -> result.data
                                ?.let(VacancyState::Content)
                                ?.takeIf { it.vacancy.id == vacancyId }
                                ?: VacancyState.Error(ErrorType.SERVER_ERROR)

                            is Resource.Error -> {
                                if (result.code == ErrorType.NOT_FOUND) {
                                    favoriteInteractor.deleteVacancy(vacancyId)
                                }
                                VacancyState.Error(
                                    result.code ?: ErrorType.SERVER_ERROR
                                )
                            }
                        }
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

    fun onLikeButtonClick() {
        val contentNow = mutableState.value as? VacancyState.Content ?: return
        val vacancy = contentNow.vacancy

        viewModelScope.launch {
            if (favoriteLiveData.value == true) {
                favoriteInteractor.deleteVacancy(vacancy.id)
            } else {
                favoriteInteractor.insertVacancy(vacancy)
            }
        }
    }

    fun openPhone(phone: Phone) {
        sharingInteractor.openPhone(phone.formatted)
    }

    fun openEmail(email: String) {
        sharingInteractor.openEmail(email)
    }

    fun shareLink() {
        val contentNow = mutableState.value as? VacancyState.Content ?: return
        val vacancy = contentNow.vacancy

        val formUrl = if (vacancy.url.startsWith("http://") || vacancy.url.startsWith("https://")) {
            vacancy.url
        } else {
            "https://${vacancy.url}"
        }

        sharingInteractor.shareLink(formUrl)
    }

}

sealed interface VacancyState {
    data object Loading : VacancyState
    data class Content(val vacancy: VacancyDetail) : VacancyState
    data class Error(val error: ErrorType) : VacancyState
}

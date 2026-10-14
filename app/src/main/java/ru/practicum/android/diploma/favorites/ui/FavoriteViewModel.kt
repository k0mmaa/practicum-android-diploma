package ru.practicum.android.diploma.favorites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacanciesState
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyInteractor
import ru.practicum.android.diploma.util.debounce

class FavoriteViewModel(private val interactor: FavoriteVacancyInteractor) : ViewModel() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    var vacancyList = mutableListOf<VacancyCard>()
    private val stateLiveData = MutableLiveData<FavoriteVacanciesState>()
    fun observeState(): LiveData<FavoriteVacanciesState> = stateLiveData

    private val intentLiveData = MutableLiveData<VacancyCard?>()
    fun observeIntent(): MutableLiveData<VacancyCard?> = intentLiveData

    init {
        appDataBase()
    }


    private val onClickDebounce = debounce<VacancyCard>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = false
    ) { vacancy ->
        intentLiveData.value = vacancy
    }

    fun appDataBase() {
        viewModelScope.launch {
            interactor.favoriteVacancies().collect { vacancyCards ->
                showResults(vacancyCards)
            }
        }
    }

    fun showResults(vacancyCard: List<VacancyCard>) {
        if (vacancyCard.isNotEmpty()) {
            vacancyList.addAll(vacancyCard)
            renderState(FavoriteVacanciesState.Content(vacancyCard))
        } else if (vacancyCard.isEmpty()) {
            renderState(FavoriteVacanciesState.Empty)
        } else {
            renderState(FavoriteVacanciesState.Error)
        }
    }

    fun openVacancy(vacancy: VacancyCard) {
        onClickDebounce(vacancy)
    }

    fun cleanVacancy() {
        intentLiveData.value = null
    }

    fun renderState(state: FavoriteVacanciesState) {
        stateLiveData.postValue(state)
    }
}

package ru.practicum.android.diploma.vacancy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.favorites.domain.db.FavoriteVacancyInteractor

class VacancyViewModel(private val vacancy: VacancyDetail, private val interactor: FavoriteVacancyInteractor) :
    ViewModel() {

    init {
        viewModelScope.launch {
            interactor.isVacancyFavorite(vacancy.id).collect { result ->
                favoriteLiveData.value = result
            }
        }
    }

    private val favoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavorite(): LiveData<Boolean> = favoriteLiveData

    fun onLikeButtonClick() {
        viewModelScope.launch {
            if (favoriteLiveData.value == true) {
                interactor.deleteVacancy(vacancy)
            } else {
                interactor.insertVacancy(vacancy)
            }
            favoriteLiveData.postValue(!(favoriteLiveData.value!!))
        }
    }


}

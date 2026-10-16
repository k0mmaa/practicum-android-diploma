package ru.practicum.android.diploma.vacancy.ui.view_model

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.SharingInteractor

class VacancyViewModel(
    private val sharingInteractor: SharingInteractor,
)  : ViewModel()  {


    fun shareLink(){
        sharingInteractor.shareLink(getLink())
    }


    // когда появится модель - заменить
    fun getLink() : String{
        return "https://dzen.ru/"
    }
}

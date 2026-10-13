package ru.practicum.android.diploma.search.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.util.Debounce
import ru.practicum.android.diploma.util.Resource

class SearchViewModel(private val context: Context, private val jobsInteractor: JobsInteractor): ViewModel()  {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var latestSearchText: String = ""
    private var page: Int = 0
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    init {
        latestSearchText = ""
        page = 0
        renderState(
            SearchState.StartPage
        )
    }
    private val vacanciesSearchDebounce =
        Debounce<String>(
            delayMillis = SEARCH_DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
            useLastParam = true
        ) { changedText ->
            searchRequest(changedText)
        }
    fun searchDebounce(changedText: String){
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        vacanciesSearchDebounce.cancel()
        if(latestSearchText.isNotEmpty()){
            vacanciesSearchDebounce(changedText)
        }
    }
    fun searchNow(changedText: String){
        this.latestSearchText = changedText
        vacanciesSearchDebounce.cancel()
        searchRequest( changedText)
    }

    fun nextPage(){
        page++
        searchNow(this.latestSearchText)
    }

    fun clearSearch(){
        latestSearchText = ""
        page = 0
        renderState(
            SearchState.StartPage
        )
    }
    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            val txt = newSearchText;
            viewModelScope.launch {
                jobsInteractor.getVacancies(text = newSearchText, page = page).collect {
                        result ->
                    if(txt == latestSearchText){
                        processResult(result)
                    }
                }
            }
        }
    }
    private fun processResult(result: Resource<Vacancies> ){
        when(result){
            is Resource.Success -> {
                result.data?.let { renderState(SearchState.Content(it)) }
            }
            is Resource.Error -> {

            }
        }

    }
    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}

package ru.practicum.android.diploma.search.ui.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.util.Debounce
import ru.practicum.android.diploma.util.ErrorType.*
import ru.practicum.android.diploma.util.Resource

class SearchViewModel(private val context: Context, private val jobsInteractor: JobsInteractor): ViewModel()  {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var vacanciesList: MutableList<VacancyCard> = mutableListOf()
    private var latestSearchText: String = ""
    private var currentPage: Int = DEFAULT_PAGE_VALUE
    private var totalPages: Int = DEFAULT_TOTAL_PAGES_VALUE
    private var totalCount: Int = 0
    private var loadingNextPage = false;
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val DEFAULT_PAGE_VALUE = 1
        private const val DEFAULT_TOTAL_PAGES_VALUE = 1
    }

    init {
        latestSearchText = ""
        currentPage = DEFAULT_PAGE_VALUE
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
            if(changedText == latestSearchText){
                renderState(SearchState.Loading)
                searchRequest(changedText)
            }
        }
    fun searchDebounce(changedText: String){
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        vacanciesSearchDebounce.cancel()
        if(latestSearchText.isNotEmpty()){
            vacanciesSearchDebounce(changedText)
        }else{
            renderState(SearchState.StartPage)
        }
    }
    fun searchNow(changedText: String){
        if (latestSearchText == changedText || this.latestSearchText.isBlank()) {
            return
        }
        this.latestSearchText = changedText

        vacanciesSearchDebounce.cancel()
        renderState(SearchState.Loading)
        searchRequest( changedText)
    }

    fun nextPage(){
        if(loadingNextPage){
            return
        }
        if(totalPages > currentPage){
            currentPage++
            vacanciesSearchDebounce.cancel()
            loadingNextPage = true
            renderState(SearchState.LoadingNextPage)
            searchRequest( this.latestSearchText, false )
        }
    }

    fun clearSearch(){
        latestSearchText = ""
        currentPage = DEFAULT_PAGE_VALUE
        vacanciesList.clear()
        renderState(
            SearchState.StartPage
        )
    }
    private fun searchRequest(newSearchText: String, clearResult: Boolean = true) {
        if (newSearchText.isNotEmpty()) {
            val txt = newSearchText;
            viewModelScope.launch {
                jobsInteractor.getVacancies(text = newSearchText, page = currentPage).collect {
                        result ->
                    if(txt == latestSearchText){
                        processResult(result, clearResult)
                    }
                    loadingNextPage = false
                }
            }
        }
    }
    private fun processResult(result: Resource<Vacancies>, clearResult: Boolean = true ){
        when(result){
            is Resource.Success -> {
                if(clearResult){
                    vacanciesList.clear()
                }
                result.data?.let {
                    vacanciesList.addAll(it.items)
                    currentPage = it.page
                    totalPages = it.pages
                    totalCount = it.found
                    if(totalCount == 0){
                        renderState(SearchState.NotFound)
                    }else{
                        renderState(SearchState.Content(vacanciesList, totalCount))
                    }
                }
            }
            is Resource.Error -> {
                when(result.code){
                    NOT_FOUND -> {
                        renderState(SearchState.NotFound)
                    }
                    NO_INTERNET -> {
                        renderState(SearchState.NoInternet)
                    }
                    else -> {
                        renderState(SearchState.ServerError)
                    }
                }
            }
        }

    }
    private fun renderState(state: SearchState) {
        Log.d("debug_model",  state.javaClass.toString())
        stateLiveData.postValue(state)
    }
}

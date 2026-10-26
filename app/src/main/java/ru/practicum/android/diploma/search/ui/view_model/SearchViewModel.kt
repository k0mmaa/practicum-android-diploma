package ru.practicum.android.diploma.search.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FiltersInteractor
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.filters.VacancyFilter
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty
import ru.practicum.android.diploma.domain.models.vacancy.Vacancies
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.util.Debounce
import ru.practicum.android.diploma.util.ErrorType.AUTHORIZATION_ERROR
import ru.practicum.android.diploma.util.ErrorType.NOT_FOUND
import ru.practicum.android.diploma.util.ErrorType.NO_INTERNET
import ru.practicum.android.diploma.util.Resource

class SearchViewModel(
    private val jobsInteractor: JobsInteractor,
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    private val filter = MutableLiveData<VacancyFilter>()
    fun observeFilter(): LiveData<VacancyFilter> = filter
    private var vacanciesList: MutableList<VacancyCard> = mutableListOf()
    private var latestSearchText: String = ""
    private var currentPage: Int = DEFAULT_PAGE_VALUE
    private var totalPages: Int = DEFAULT_TOTAL_PAGES_VALUE
    private var totalCount: Int = 0
    private var loadingNextPage = false
    private var searchJob: Job? = null

    private var lastFilter: VacancyFilter? = null

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val DEFAULT_PAGE_VALUE = 1
        private const val DEFAULT_TOTAL_PAGES_VALUE = 1
    }

    init {
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
            if (changedText == latestSearchText) {
                renderState(SearchState.Loading)
                searchRequest(changedText)
            }
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        vacanciesSearchDebounce.cancel()
        if (latestSearchText.isNotEmpty()) {
            resetPagination()
            vacanciesSearchDebounce(changedText)
        } else {
            renderState(SearchState.StartPage)
        }
    }

    fun filterSearch() {
        if (this.latestSearchText.isBlank()) {
            return
        }
        searchNow(this.latestSearchText)
    }

    fun searchNow(changedText: String) {
        if (changedText.isBlank()) {
            return
        }
        this.latestSearchText = changedText

        vacanciesSearchDebounce.cancel()

        resetPagination()

        renderState(SearchState.Loading)
        searchRequest(changedText)
    }

    fun nextPage() {
        if (loadingNextPage) {
            return
        }
        if (lastFilter != filter.value) {
            searchNow(this.latestSearchText)
            return
        }
        if (totalPages > currentPage) {
            vacanciesSearchDebounce.cancel()
            loadingNextPage = true
            renderState(SearchState.LoadingNextPage)
            searchRequest(
                this.latestSearchText,
                false, currentPage + 1
            )
        }
    }

    fun clearSearch() {
        latestSearchText = ""
        vacanciesSearchDebounce.cancel()
        resetPagination()
        vacanciesList.clear()
        renderState(
            SearchState.StartPage
        )
    }

    private fun resetPagination() {
        currentPage = DEFAULT_PAGE_VALUE
        totalPages = DEFAULT_TOTAL_PAGES_VALUE
        totalCount = 0
        loadingNextPage = false
    }

    private fun searchRequest(newSearchText: String, clearResult: Boolean = true, page: Int = DEFAULT_PAGE_VALUE) {
        if (newSearchText.isNotEmpty()) {
            val txt = newSearchText
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                val requestFilter = filter.value?.copy()
                jobsInteractor.getVacancies(
                    newSearchText,
                    page,
                    requestFilter
                ).collect { result ->
                    if (txt == latestSearchText) {
                        processResult(result, clearResult)
                        lastFilter = requestFilter
                    }
                    loadingNextPage = false
                }
            }
        }
    }



    private fun processResult(result: Resource<Vacancies>, clearResult: Boolean = true) {
        when (result) {
            is Resource.Success -> {
                if (clearResult) {
                    vacanciesList.clear()
                }
                result.data?.let {
                    vacanciesList.addAll(it.items)
                    currentPage = it.page
                    totalPages = it.pages
                    totalCount = it.found
                    if (totalCount == 0) {
                        renderState(SearchState.NotFound)
                    } else {
                        renderState(SearchState.Content(vacanciesList.toList(), totalCount))
                    }
                }
            }

            is Resource.Error -> {
                when (result.code) {
                    NOT_FOUND -> {
                        renderState(SearchState.NotFound)
                    }

                    NO_INTERNET -> {
                        renderState(SearchState.NoInternet)
                    }

                    AUTHORIZATION_ERROR -> {
                        renderState(SearchState.AuthorizationError)
                    }

                    else -> {
                        renderState(SearchState.ServerError)
                    }
                }
            }
        }

    }

    fun loadFilter() {
        val savedFilter = filtersInteractor.getFilter()
        if (filter.value != savedFilter) {
            filter.value = savedFilter
        }
    }

    private fun renderState(state: SearchState) {

        stateLiveData.postValue(state)
    }
}

package ru.practicum.android.diploma.filter.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.domain.models.filters.FilterOption
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class IndustryViewModel(
    private val jobsInteractor: JobsInteractor
) : ViewModel() {
    private companion object {
        const val TAG = "IndustryViewModel"
    }

    private val mutableState = MutableLiveData<IndustryState>(IndustryState.Loading)
    val state: LiveData<IndustryState> = mutableState

    private var allIndustries: List<FilterIndustry> = emptyList()
    private var query: String = ""
    private var selectedIndustry: FilterOption? = null
    private var selectionInitialized = false
    private var loadJob: Job? = null


    fun initialize(industry: FilterOption?) {
        if (selectionInitialized) return
        selectionInitialized = true
        selectedIndustry = industry
        loadIndustries()
    }

    fun selectIndustry(industry: FilterIndustry) {
        selectedIndustry = FilterOption(industry.id, industry.name)
        renderContent()
    }

    fun getSelectedIndustry(): FilterOption? = selectedIndustry

    fun searchIndustries(value: String) {
        query = value.trim()
        renderContent()
    }

    fun loadIndustries() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            mutableState.value = IndustryState.Loading
            try {
                jobsInteractor.getIndustries().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            allIndustries = result.data.orEmpty()
                            renderContent()
                        }

                        is Resource.Error -> {
                            mutableState.value = IndustryState.Error(
                                result.code ?: ErrorType.SERVER_ERROR
                            )
                        }
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to load industries", exception)
                mutableState.value = IndustryState.Error(ErrorType.SERVER_ERROR)
            }
        }
    }

    private fun renderContent() {
        if (mutableState.value !is IndustryState.Loading &&
            mutableState.value !is IndustryState.Content
        ) {
            return
        }

        val filteredIndustries = if (query.isBlank()) {
            allIndustries
        } else {
            allIndustries.filter { it.name.contains(query, ignoreCase = true) }
        }
        mutableState.value = IndustryState.Content(
            industries = filteredIndustries,
            selectedIndustryId = selectedIndustry?.id
        )
    }
}

sealed interface IndustryState {
    data object Loading : IndustryState
    data class Content(
        val industries: List<FilterIndustry>,
        val selectedIndustryId: String?
    ) : IndustryState

    data class Error(val error: ErrorType) : IndustryState
}

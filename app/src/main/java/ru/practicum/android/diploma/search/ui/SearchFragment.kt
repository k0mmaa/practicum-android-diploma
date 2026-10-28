package ru.practicum.android.diploma.search.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.filter.ui.FilterFragment.Companion.FILTERS_APPLIED_KEY
import ru.practicum.android.diploma.search.ui.activity.VacanciesListAdapter
import ru.practicum.android.diploma.search.ui.view_model.SearchState
import ru.practicum.android.diploma.search.ui.view_model.SearchViewModel
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment


class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VacanciesListAdapter

    private var current_search: String = STRING_DEF
    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var onVacancyClickDebounce: (VacancyCard) -> Unit

    companion object {

        const val STRING_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 2000L
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadFilter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeToast().observe(viewLifecycleOwner){ errorId ->
            if(errorId != null){
                val errorMessage = getString(errorId)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            viewModel.clearErrorToast()
        }

        binding.searchFragmentTitle.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.button_to_filter -> {
                    findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
                    true
                }

                else -> false
            }
        }
        binding.searchInputLayout.setEndIconOnClickListener {
            if (current_search.isNotBlank()) {
                current_search = ""
                hideKeyboard()
                binding.inputStrings.clearFocus()
                viewModel.clearSearch()
            }
        }
        parentFragmentManager.setFragmentResultListener(
            FILTERS_APPLIED_KEY,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.filterSearch()
        }
        onVacancyClickDebounce = debounce<VacancyCard>(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        )
        { vacancy ->
            openVacancy(vacancy)
        }

        binding.inputStrings.setOnEditorActionListener { _, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) {
                viewModel.searchNow(current_search)
            }
            false
        }

        adapter = VacanciesListAdapter({ vacancy ->
            onVacancyClickDebounce(vacancy) // обработка клика
        })
        binding.vacanciesList.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeFilter().observe(viewLifecycleOwner) {
            if (it == null || it.isModelEmpty()) {
                updateFilterIcon(false)
            } else {
                updateFilterIcon(true)
            }
        }

        binding.vacanciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.vacanciesList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.nextPage()
                    }
                }
            }
        })
        binding.inputStrings.doOnTextChanged { s: CharSequence?, start: Int, before: Int, count: Int ->
            if (!s.isNullOrEmpty()) {
                current_search = s.toString().trim()
            } else {
                current_search = STRING_DEF
            }

            if (!s.isNullOrEmpty()) {
                binding.searchInputLayout.setEndIconDrawable(R.drawable.ic_clear)
            } else {
                binding.searchInputLayout.setEndIconDrawable(R.drawable.ic_search_18)
            }

            viewModel.searchDebounce(current_search)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> {
                showVacancies(state.vacancies)
                if (state.totalCount > 0) {
                    hideAll(textInfo = true, vacanciesList = true)
                    val vacanciesCountText = resources.getQuantityString(
                        R.plurals.vacancies_count,
                        state.totalCount,
                        state.totalCount,
                    )
                    binding.textInfo.text = resources.getString(
                        R.string.finding_vacancies,
                        resources.getString(R.string.finding),
                        vacanciesCountText
                    )
                } else {
                    showEmptyVacancies()
                }

            }

            SearchState.Loading -> {
                hideAll(progressBar = true)
            }

            SearchState.NoInternet -> {
                hideAll(errorPlaceholder = true)
                binding.errorImage.setImageResource(R.drawable.ic_no_internet)
                binding.errorMessage.text = getString(R.string.network_error)
            }

            SearchState.NotFound -> {
                showEmptyVacancies()
            }

            SearchState.ServerError -> {
                hideAll(errorPlaceholder = true)
                binding.errorImage.setImageResource(R.drawable.ic_server_error_1)
                binding.errorMessage.text = getString(R.string.server_error)
            }

            SearchState.AuthorizationError -> {
                hideAll(errorPlaceholder = true)
                binding.errorImage.setImageResource(R.drawable.ic_server_error_1)
                binding.errorMessage.text = getString(R.string.api_authorization_error)
            }

            SearchState.StartPage -> {
                hideAll(startImage = true)
                binding.inputStrings.setText("")
            }

            SearchState.LoadingNextPage -> {
                hideAll(progressBar = true, vacanciesList = true, textInfo = true)
            }
        }
    }

    fun showEmptyVacancies() {
        hideAll(textInfo = true, errorPlaceholder = true)
        binding.textInfo.text = resources.getString(R.string.clear_vacancies)
        binding.errorImage.setImageResource(R.drawable.ic_placeholder_nothing_find_vacancy)
        binding.errorMessage.text = getString(R.string.empty_list_vacations)
    }

    fun hideAll(
        startImage: Boolean = false, textInfo: Boolean = false,
        vacanciesList: Boolean = false, progressBar: Boolean = false,
        errorPlaceholder: Boolean = false
    ) {
        binding.startImage.isVisible = startImage
        binding.textInfo.isVisible = textInfo
        binding.vacanciesList.isVisible = vacanciesList
        binding.progressBar.isVisible = progressBar
        binding.errorPlaceholder.isVisible = errorPlaceholder
        if (binding.errorPlaceholder.isVisible) {
            binding.errorImage.isVisible = true
            binding.errorMessage.isVisible = true
        }
    }

    fun openVacancy(vacancy: VacancyCard) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }

    fun showVacancies(list: List<VacancyCard>) {
        adapter.submitList(list)
    }


    fun hideKeyboard() {
        val imm = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun updateFilterIcon(hasFilters: Boolean) {
        binding.searchFragmentTitle.menu
            .findItem(R.id.button_to_filter)
            .setIcon(
                if (hasFilters) {
                    R.drawable.selected_filter_on_24px
                } else {
                    R.drawable.ic_trailing_24px
                }
            )
    }
}

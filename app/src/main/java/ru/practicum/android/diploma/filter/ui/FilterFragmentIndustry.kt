package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterFragmentIndustryBinding
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel
import ru.practicum.android.diploma.filter.view_model.IndustryState
import ru.practicum.android.diploma.filter.view_model.IndustryViewModel
import ru.practicum.android.diploma.util.ErrorType

class FilterFragmentIndustry : Fragment() {
    private var _binding: FilterFragmentIndustryBinding? = null
    private val binding get() = _binding!!

    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)
    private val industryViewModel: IndustryViewModel by viewModel()
    private lateinit var industryAdapter: IndustryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterFragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        industryViewModel.initializeSelection(filtersViewModel.observeFilter().value?.industry)
        industryAdapter = IndustryAdapter(industryViewModel::selectIndustry)
        binding.industryList.adapter = industryAdapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchInputLayout.setEndIconOnClickListener {
            binding.inputIndustry.text?.clear()
        }
        binding.inputIndustry.doOnTextChanged { text, _, _, _ ->
            industryViewModel.searchIndustries(text?.toString().orEmpty())
            binding.searchInputLayout.setEndIconDrawable(
                if (text.isNullOrEmpty()) R.drawable.ic_search_18 else R.drawable.ic_clear
            )
        }
        binding.btnChoose.setOnClickListener {
            industryViewModel.getSelectedIndustry()?.let(filtersViewModel::setIndustry)
            findNavController().popBackStack()
        }
        binding.btnRefresh.setOnClickListener {
            industryViewModel.loadIndustries()
        }

        industryViewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: IndustryState) {
        binding.progressBar.isVisible = state is IndustryState.Loading
        binding.industryList.isVisible = false
        binding.stateMessage.isVisible = false
        binding.btnRefresh.isVisible = false
        binding.btnChoose.isVisible = false

        when (state) {
            IndustryState.Loading -> Unit
            is IndustryState.Error -> {
                binding.stateMessage.setText(
                    when (state.error) {
                        ErrorType.NO_INTERNET -> R.string.network_error
                        ErrorType.AUTHORIZATION_ERROR -> R.string.api_authorization_error
                        else -> R.string.industry_list_error
                    }
                )
                binding.stateMessage.isVisible = true
                binding.btnRefresh.isVisible = true
            }

            is IndustryState.Content -> {
                industryAdapter.update(state.industries, state.selectedIndustryId)
                if (state.industries.isEmpty()) {
                    binding.stateMessage.setText(
                        if (binding.inputIndustry.text.isNullOrBlank()) {
                            R.string.industry_list_error
                        } else {
                            R.string.industry_not_found
                        }
                    )
                    binding.stateMessage.isVisible = true
                } else {
                    binding.industryList.isVisible = true
                }
                binding.btnChoose.isVisible = state.selectedIndustryId != null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

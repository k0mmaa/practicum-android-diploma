package ru.practicum.android.diploma.filter.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterFragmentCountryBinding
import ru.practicum.android.diploma.filter.activity.AreasAdapter
import ru.practicum.android.diploma.filter.ui.place.viewModel.CountryFilterState
import ru.practicum.android.diploma.filter.ui.place.viewModel.FilterCountryViewModel
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel


class FilterFragmentCountry : Fragment() {

    private var _binding: FilterFragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)
    private val countryViewModel: FilterCountryViewModel by viewModel()
    private lateinit var adapterCountry: AreasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterFragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.countryList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterCountry = AreasAdapter { country ->
            filtersViewModel.setCountry(country.toFilterOption())
            findNavController().popBackStack()
        }
        binding.countryList.adapter = adapterCountry

        countryViewModel.observeCountriesState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: CountryFilterState) {
        when (state) {
            is CountryFilterState.Loading -> {
                binding.apply {
                    progressBar.isVisible = true
                    countryList.isVisible = false
                }
            }

            is CountryFilterState.Success -> {
                binding.apply {
                    progressBar.isVisible = false
                    countryList.isVisible = true
                }
                adapterCountry.submitList(state.countries)
            }

            is CountryFilterState.Error -> {
                binding.apply {
                    progressBar.isVisible = false
                    countryList.isVisible = false
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

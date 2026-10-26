package ru.practicum.android.diploma.filter.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterFragmentRegionBinding
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.filters.FilterOption
import ru.practicum.android.diploma.filter.activity.AreasAdapter
import ru.practicum.android.diploma.filter.ui.place.viewModel.FilterRegionViewModel
import ru.practicum.android.diploma.filter.ui.place.viewModel.RegionFilterState
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel

class FilterFragmentRegion : Fragment() {
    private var _binding: FilterFragmentRegionBinding? = null
    private val binding get() = _binding!!

    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)
    private val regionViewModel: FilterRegionViewModel by viewModel()
    private lateinit var regionAdapter: AreasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterFragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        regionViewModel.loadRegion(filtersViewModel.observeFilter().value?.country?.id)

        binding.regionList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        regionAdapter = AreasAdapter { region ->
            val currentCountry = regionViewModel.getCountryByCity(region)
            if (currentCountry != null) filtersViewModel.setCountry(currentCountry.toFilterOption())
            filtersViewModel.setRegion(region.toFilterOption())
            findNavController().popBackStack()
        }
        binding.regionList.adapter = regionAdapter

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchInputLayout.setEndIconOnClickListener {
            binding.inputStrings.text?.clear()
        }
        binding.inputStrings.doOnTextChanged { text, _, _, _ ->
            binding.searchInputLayout.setEndIconDrawable(
                if (text.isNullOrEmpty()) R.drawable.ic_search_18 else R.drawable.ic_clear
            )
            regionViewModel.searchRegion(text?.toString().orEmpty())
        }
        regionViewModel.observeRegionState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: RegionFilterState) {
        when (state) {
            is RegionFilterState.Loading -> {
                binding.apply {
                    progressBar.isVisible = true
                    regionList.isVisible = false
                    placeholderMessage.isVisible = false
                }
            }

            is RegionFilterState.Content -> {
                binding.apply {
                    progressBar.isVisible = false
                    regionList.isVisible = true
                    placeholderMessage.isVisible = false
                    regionAdapter.submitList(state.places)
                }
            }

            is RegionFilterState.Empty -> {
                binding.apply {
                    progressBar.isVisible = false
                    regionList.isVisible = false
                    placeholderMessage.isVisible = true

                    placeholderText.text = getString(R.string.error_region)
                    placeholderImage.setImageResource(R.drawable.ic_placeholder_nothing_find_vacancy)
                }
            }

            is RegionFilterState.Error -> {
                binding.apply {
                    progressBar.isVisible = false
                    regionList.isVisible = false
                    placeholderMessage.isVisible = true

                    placeholderText.text = getString(R.string.empty_list_region)
                    placeholderImage.setImageResource(R.drawable.img_empty_list_region)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun FilterArea.toFilterOption() = FilterOption(
    id = this.id,
    name = this.name
)

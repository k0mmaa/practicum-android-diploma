package ru.practicum.android.diploma.filter.ui.place

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterFragmentRegionBinding
import ru.practicum.android.diploma.filter.activity.AreasAdapter
import ru.practicum.android.diploma.filter.ui.place.viewModel.RegionFilterState
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel
import ru.practicum.android.diploma.filter.view_model.toFilterOption

class FilterFragmentRegion: Fragment() {
    private var _binding: FilterFragmentRegionBinding? = null
    private val binding get() = _binding!!

    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)
    private lateinit var regionAdapter: AreasAdapter
    private lateinit var textWatcher: TextWatcher

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

        binding.toolbar.setOnClickListener {
            findNavController().popBackStack()
        }

        filtersViewModel.observeRegionState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.regionList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        regionAdapter = AreasAdapter { region ->
            filtersViewModel.setRegion(region.toFilterOption())
            findNavController().popBackStack()
        }
        binding.regionList.adapter = regionAdapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtersViewModel.searchRegionDebounce(s?.toString().orEmpty().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputStrings.addTextChangedListener(textWatcher)

    }

    private fun render(state: RegionFilterState) {
        when (state) {
            is RegionFilterState.Start -> {
                binding.apply {
                    progressBar.isVisible = false
                    regionList.isVisible = false
                    placeholderMessage.isVisible = false
                }
            }

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

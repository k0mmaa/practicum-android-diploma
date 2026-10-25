package ru.practicum.android.diploma.filter.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.R.id.action_filterFragmentWorkPlace_to_filterFragmentCountry
import ru.practicum.android.diploma.databinding.FilterFragmentWorkPlaceBinding
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel


class FilterFragmentWorkPlace : Fragment() {

    private var _binding: FilterFragmentWorkPlaceBinding? = null
    private val binding get() = _binding!!

    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterFragmentWorkPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.countryContainer.setOnClickListener {
            findNavController().navigate(action_filterFragmentWorkPlace_to_filterFragmentCountry)
        }

        binding.regionContainer.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragmentWorkPlace_to_filterFragmentRegion)
        }

        filtersViewModel.observeFilter().observe(viewLifecycleOwner) { vacancyFilter ->
            binding.btnChoose.isVisible = vacancyFilter?.country != null || vacancyFilter?.region != null
            if (vacancyFilter != null) {
                if (vacancyFilter.country != null) {
                    binding.apply {
                        countryTitle.isVisible = true
                        countryValue.isVisible = true
                        countryValue.text = vacancyFilter.country.name
                    }

                } else {
                    binding.apply {
                        countryTitle.isVisible = false
                        countryValue.isVisible = true
                        countryValue.text = getString(R.string.cuntry)
                    }
                }
                if (vacancyFilter.region != null) {
                    binding.apply {
                        regionTitle.isVisible = true
                        regionValue.isVisible = true
                        regionValue.text = vacancyFilter.region.name
                    }
                } else {
                    binding.apply {
                        regionTitle.isVisible = false
                        regionValue.isVisible = true
                        regionValue.text = getString(R.string.region)
                    }
                }
            } else {
                binding.apply {
                    countryTitle.isVisible = false
                    regionTitle.isVisible = false
                    countryValue.isVisible = true
                    regionValue.isVisible = true
                    countryValue.text = getString(R.string.cuntry)
                    regionValue.text = getString(R.string.region)
                }
            }
        }

        binding.btnChoose.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

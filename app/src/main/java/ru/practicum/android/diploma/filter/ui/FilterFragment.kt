package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

import ru.practicum.android.diploma.R.id.action_filterFragment_to_filterFragmentWorkPlace
import ru.practicum.android.diploma.databinding.FilterFragmentSettingsBinding
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class FilterFragment : Fragment() {
    private var _binding: FilterFragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterFragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.workPlaceContainer.setOnClickListener {
            findNavController().navigate(action_filterFragment_to_filterFragmentWorkPlace)
        }

        filtersViewModel.observeFilter().observe(viewLifecycleOwner) {
            Log.d("FilterFragment", it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

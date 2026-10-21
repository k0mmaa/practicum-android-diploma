package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterFragmentCountryBinding
import ru.practicum.android.diploma.domain.models.filters.FilterOption
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel


class FilterFragmentCountry : Fragment() {

    private var _binding: FilterFragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)

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
        // тест того что модель меняется потом удалить
        filtersViewModel.setCountry(FilterOption("id_test", "Россия"))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

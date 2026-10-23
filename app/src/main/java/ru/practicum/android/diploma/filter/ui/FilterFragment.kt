package ru.practicum.android.diploma.filter.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.R.id.action_filterFragment_to_filterFragmentWorkPlace
import ru.practicum.android.diploma.databinding.FilterFragmentSettingsBinding
import ru.practicum.android.diploma.filter.view_model.FiltersViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.practicum.android.diploma.domain.models.filters.isModelEmpty

class FilterFragment : Fragment() {
    private var _binding: FilterFragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val filtersViewModel: FiltersViewModel by koinNavGraphViewModel(R.id.filters_graph)

    companion object{
        const val FILTERS_APPLIED_KEY = "filters_applied"
    }

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
        binding.workPlaceClear.setOnClickListener {
            filtersViewModel.clearWorkPlace()
        }

        binding.industryClear.setOnClickListener {
            filtersViewModel.clearIndustry()
        }

        filtersViewModel.observeFilter().observe(viewLifecycleOwner) { filter ->

            binding.btnApply.isInvisible = filter.isModelEmpty() && !filtersViewModel.isFilterWasChanged()
            if (binding.hideSalaryCheckbox.isChecked != filter.onlyWithSalary) {
                binding.hideSalaryCheckbox.isChecked = filter.onlyWithSalary
            }
            binding.btnClear.isInvisible = filter.isModelEmpty()

            val workPlace = listOfNotNull(filter.country?.name, filter.region?.name)
                .joinToString(", ")
                .ifEmpty {null }

            val hasWorkPlace = workPlace != null
            binding.workPlaceTitle.isVisible = true // заголовок никогда не прячем всегда виден. см. Макет
            binding.workPlaceTitle.setTextSize( //в макете размер зависит от наличия workPlace
                TypedValue.COMPLEX_UNIT_SP,
                if (hasWorkPlace) 12f else 16f
            )
            binding.workPlaceValue.isVisible = hasWorkPlace
            binding.workPlaceValue.text = workPlace.orEmpty()
            binding.workPlaceArrow.isVisible = !hasWorkPlace
            binding.workPlaceClear.isVisible = hasWorkPlace

            val industry = filter.industry?.name
            val hasIndustry = industry != null
            binding.industryTitle.isVisible = true // заголовок никогда не прячем всегда виден. см. Макет
            binding.industryTitle.setTextSize( //в макете размер зависит от наличия workPlace
                TypedValue.COMPLEX_UNIT_SP,
                if (hasIndustry) 12f else 16f
            )
            binding.industryValue.isVisible = hasIndustry
            binding.industryValue.text = industry.orEmpty()
            binding.industryArrow.isVisible = !hasIndustry
            binding.industryClear.isVisible = hasIndustry

            val displayedSalary = binding.salaryInputEditText.text
                ?.toString()
                ?.toIntOrNull()
            if (displayedSalary != filter.salary) {
                binding.salaryInputEditText.setText(filter.salary?.toString().orEmpty())
            }
        }

        binding.hideSalaryCheckbox.setOnCheckedChangeListener { button, checked ->
            filtersViewModel.setOnlyWithSalary(checked)
        }
        binding.salaryInputEditText.doOnTextChanged {s: CharSequence?, start: Int, before: Int, count: Int  ->
            filtersViewModel.setSalary(s.toString().toIntOrNull())
        }
        binding.btnApply.setOnClickListener {
            filtersViewModel.save()

            parentFragmentManager.setFragmentResult(
                FILTERS_APPLIED_KEY,
                Bundle.EMPTY
            )

            findNavController().popBackStack()

        }
        binding.btnClear.setOnClickListener {
            filtersViewModel.save()
            filtersViewModel.reset()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

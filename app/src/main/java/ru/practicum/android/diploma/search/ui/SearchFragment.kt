package ru.practicum.android.diploma.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.search.ui.activity.VacanciesListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding
import ru.practicum.android.diploma.domain.models.vacancy.Salary
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.search.ui.view_model.SearchViewModel
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment


class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VacanciesListAdapter

    private var current_search: String = STRING_DEF
    private val viewModel by viewModel<SearchViewModel>()
    companion object {

        const val STRING_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 2000L
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

        binding.buttonToFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }
        binding.searchInputLayout.setEndIconOnClickListener {

        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.inputStrings.doOnTextChanged { s: CharSequence?, start: Int, before: Int, count: Int ->
            if (!s.isNullOrEmpty()) {
                current_search = s.toString()
            } else {
                current_search = STRING_DEF
            }

            if(!s.isNullOrEmpty()){
                binding.searchInputLayout.setEndIconDrawable(R.drawable.ic_clear)
            }else{
                binding.searchInputLayout.setEndIconDrawable(R.drawable.ic_search_18)
            }

            viewModel.searchDebounce(current_search)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Заглушка для показа списка вакансий
    fun renderList() {
        adapter = VacanciesListAdapter({ vacancy ->
            onVacancyClickDebounce(vacancy) // обработка клика
        })
        binding.vacanciesList.adapter = adapter
        adapter.setOnLongClickListener { vacancy ->
            // обработка длинного клика (если понадобится)
        }
        showVacancies(mockVacancies())
    }

    fun showVacancies(list: List<VacancyCard>) {
        binding.vacanciesList.isVisible = true
        adapter.list.clear()
        adapter.list.addAll(list)
        adapter.notifyDataSetChanged()
    }

    fun onVacancyClickDebounce(vacancy: VacancyCard) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }
    fun hideKeyboard() {
        val imm = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
    // Заглушка вакансий
    fun mockVacancies(): List<VacancyCard> {
        return listOf(
            VacancyCard(
                id = "0000258d-fb45-3152-bfeb-250a4c547384",
                name = "Backend-разработчик",
                company = "Intel",
                city = "Новосибирск",
                salary = Salary(
                    from = null,
                    to = null,
                    currency = "AZN",
                    id = "1"
                ),
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Intel_logo_2023.svg/500px-Intel_logo_2023.svg.png"
            ),
            VacancyCard(
                id = "0001db2d-1366-379d-98ce-6f965bbc7816",
                name = "Frontend-разработчик",
                company = "NVIDIA",
                city = "Барнаул",
                salary = Salary(
                    from = 1800,
                    to = 3200,
                    currency = "RUB",
                    id = null
                ),
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/NVIDIA_logo.svg/500px-NVIDIA_logo.svg.png"
            ),
            VacancyCard(
                id = "0000347f-1440-3f6f-a91a-d52c15a41021",
                name = "Android-разработчик",
                company = "Microsoft",
                city = "Самара",
                salary = null,
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/500px-Microsoft_logo.svg.png"
            ),
            VacancyCard(
                id = "test-007",
                name = "QA-инженер",
                company = "Samsung",
                city = "Алматы",
                salary = Salary(
                    id = null,
                    from = 500000,
                    to = 900000,
                    currency = "KZT"
                ),
                logo = null
            ),
            VacancyCard(
                id = "test-008",
                name = "DevOps-инженер",
                company = "IBM",
                city = "Тбилиси",
                salary = Salary(
                    id = null,
                    from = null,
                    to = 6000,
                    currency = "GEL"
                ),
                logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/Adobe_Corporate_logo.svg"
            ),
            VacancyCard(
                id = "test-009",
                name = "Разработчик внутренних сервисов и высоконагруженных систем на Kotlin и Java",
                company = "Компания с очень длинным названием для проверки переноса текста",
                city = "Нижний Новгород",
                salary = Salary(
                    id = null,
                    from = 500,
                    to = null,
                    currency = "RUR"
                ),
                logo = null
            ),
        )
    }
}

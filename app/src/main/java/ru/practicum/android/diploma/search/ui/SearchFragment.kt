package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.search.ui.activity.VacanciesListAdapter

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding
import ru.practicum.android.diploma.domain.models.vacancy.Salary
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment


class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VacanciesListAdapter

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

        binding.buttonToVacancy.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_vacancyFragment,
                VacancyFragment.createArgs("test_id")
            )
        }

        binding.buttonToFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        renderList()
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

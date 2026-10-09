package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.entity.AddressEntity
import ru.practicum.android.diploma.data.db.entity.ContactsEntity
import ru.practicum.android.diploma.data.db.entity.PhoneEntity
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.api.JobsInteractor
import ru.practicum.android.diploma.util.Resource
class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private val jobsInteractor: JobsInteractor by inject()
    private val appDatabase: AppDatabase by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToVacancy.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment)
        }

        binding.buttonToFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }
        testVacancyDetails()
        testDatabase()
    }
    private fun testVacancyDetails() {
        lifecycleScope.launch {

            jobsInteractor.getVacancies(
                text = "Android",
                page = 0
            ).collect { result ->

                if (result is Resource.Success) {
                    val vacancyId = result.data
                        ?.items
                        ?.firstOrNull()
                        ?.id
                        ?: return@collect

                    Log.d("TEST_NETWORK", "Testing vacancy id=$vacancyId")

                    jobsInteractor.getVacancyDetails(vacancyId)
                        .collect { detailsResult ->

                            when (detailsResult) {
                                is Resource.Success -> {
                                    Log.d(
                                        "TEST_NETWORK",
                                        "VACANCY DETAILS SUCCESS: ${detailsResult.data}"
                                    )
                                }

                                is Resource.Error -> {
                                    Log.e(
                                        "TEST_NETWORK",
                                        "VACANCY DETAILS ERROR: ${detailsResult.code}"
                                    )
                                }
                            }
                        }
                }
            }
        }
    }

    private fun testDatabase() {
        lifecycleScope.launch {

            val dao = appDatabase.getVacancyDao()

            val testVacancy = VacancyEntity(
                id = "TEST_VACANCY_ID",
                name = "Test Android Developer",
                description = "Test vacancy description",
                url = "https://test.ru",

                salaryFrom = 100000,
                salaryTo = 200000,
                salaryCurrency = "RUR",

                employerId = "TEST_EMPLOYER",
                employerName = "Test Company",
                employerLogo = null,

                areaId = "113",
                areaName = "Россия",

                industryId = "7",
                industryName = "IT",

                experienceId = "between1And3",
                experienceName = "От 1 года до 3 лет",

                scheduleId = "fullDay",
                scheduleName = "Полный день",

                employmentId = "full",
                employmentName = "Полная занятость",

                address = AddressEntity(
                    id = "1",
                    city = "Москва",
                    street = "Тестовая",
                    building = "1",
                    raw = "Москва, Тестовая, 1"
                ),

                contacts = ContactsEntity(
                    id = "1",
                    name = "Иван",
                    email = "test@test.ru",
                    phones = listOf(
                        PhoneEntity(
                            comment = "Рабочий",
                            formatted = "+7 999 123-45-67"
                        )
                    )
                ),

                skills = listOf(
                    "Kotlin",
                    "Android",
                    "Room"
                ),

                addedAt = System.currentTimeMillis()
            )

            // INSERT
            dao.insertVacancy(testVacancy)

            Log.d(
                "TEST_DB",
                "INSERT DONE"
            )

            // GET BY ID
            val savedVacancy = dao.getVacancy(
                testVacancy.id
            )

            Log.d(
                "TEST_DB",
                "GET: $savedVacancy"
            )

            // CHECK FAVORITE
            val isFavorite = dao
                .isVacancyInFavorites(testVacancy.id)
                .first()

            Log.d(
                "TEST_DB",
                "IS FAVORITE: $isFavorite"
            )

            // GET ALL
            val vacancies = dao
                .getVacancies()
                .first()

            Log.d(
                "TEST_DB",
                "ALL VACANCIES: $vacancies"
            )

            // DELETE
            dao.deleteVacancyById(
                testVacancy.id
            )

            val afterDelete = dao.getVacancy(
                testVacancy.id
            )

            Log.d(
                "TEST_DB",
                "AFTER DELETE: $afterDelete"
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

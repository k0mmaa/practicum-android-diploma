package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FavoriteFragmentBinding
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacanciesState
import ru.practicum.android.diploma.search.ui.activity.VacanciesListAdapter
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class FavoriteFragment : Fragment() {
    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var vacancyAdapter: VacanciesListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeIntent().observe(viewLifecycleOwner) { vacancy ->
            if (vacancy != null) {
                findNavController().navigate(
                    R.id.action_favoriteFragment_to_vacancyFragment,
                    VacancyFragment.createArgs(vacancy.id)
                )
                viewModel.cleanVacancy()
            }
        }

        binding.vacanciesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vacancyAdapter = VacanciesListAdapter { vacancyCard ->
            viewModel.openVacancy(vacancyCard)
        }
        binding.vacanciesList.adapter = vacancyAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun render(state: FavoriteVacanciesState) {
        when (state) {
            is FavoriteVacanciesState.Error -> {
                binding.apply {
                    vacanciesList.visibility = View.GONE
                    placeholderMessage.visibility = View.VISIBLE

                    placeholderText.text = getString(R.string.empty_list_vacations)
                    placeholderImage.setImageResource(R.drawable.ic_placeholder_nothing_find_vacancy)
                }
            }

            is FavoriteVacanciesState.Empty -> {
                binding.apply {
                    vacanciesList.visibility = View.GONE
                    placeholderMessage.visibility = View.VISIBLE

                    placeholderText.text = getString(R.string.empty_list)
                    placeholderImage.setImageResource(R.drawable.img_empty_list_favorites)
                }
            }

            is FavoriteVacanciesState.Content -> {
                vacancyAdapter.list.clear()
                vacancyAdapter.list.addAll(viewModel.vacancyList)
                vacancyAdapter.notifyDataSetChanged()
                binding.apply {
                    placeholderMessage.visibility = View.GONE
                    vacanciesList.visibility = View.VISIBLE
                }
            }
        }
    }
}

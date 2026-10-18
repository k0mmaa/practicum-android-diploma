package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyFragmentBinding

class VacancyFragment : Fragment() {
    private var _binding: VacancyFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyViewModel by viewModel()

    private val vacancyId: String? by lazy {
        requireArguments().getString(ARGS_VACANCY_ID)
    }
    companion object {
        private const val ARGS_VACANCY_ID = "ARGS_VACANCY_ID"
        fun createArgs(vacancyId: String): Bundle {
            return bundleOf(ARGS_VACANCY_ID to vacancyId)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = VacancyFragmentBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // пишем в лог id
        /* TODO потом можно удалить */
        vacancyId?.let {
            Log.d("VacancyFragment_ID" , it)
        }
        binding.vacancyFragmentTitle.setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.observeFavorite().observe(viewLifecycleOwner) {
            requireActivity().invalidateMenu()
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.vacancy_toolbar_menu, menu)
                val likeItem = menu.findItem(R.id.button_like)
                val currentLikeState = viewModel.observeFavorite().value

                if (currentLikeState == true) {
                    likeItem.setIcon(R.drawable.ic_favorites_on__24px)
                } else {
                    likeItem.setIcon(R.drawable.favorites_off__24px)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.button_like -> {
                        viewModel.onLikeButtonClick()
                        true
                    }

                    R.id.button_share -> {

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.databinding.VacancyFragmentBinding

class VacancyFragment : Fragment() {
    private var _binding: VacancyFragmentBinding? = null
    private val binding get() = _binding!!

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


        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

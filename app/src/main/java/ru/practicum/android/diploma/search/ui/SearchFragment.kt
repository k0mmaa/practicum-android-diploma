package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        binding.searchInputLayout.setEndIconOnClickListener {
            binding.inputStrings.text?.clear()
        }

        binding.inputStrings.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                showEmptyPlaceholder()
            }
        }
    }

    private fun showEmptyPlaceholder() {
        binding.emptyPlaceholder.isVisible = true
        binding.trackRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorPlaceholder.isVisible = false
        binding.nothingFinded.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

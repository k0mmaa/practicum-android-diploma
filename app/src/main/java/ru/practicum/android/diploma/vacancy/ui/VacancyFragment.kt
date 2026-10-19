package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyFragmentBinding
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.SalaryFormatter
import ru.practicum.android.diploma.util.loadLogo

class VacancyFragment : Fragment() {
    private var _binding: VacancyFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<VacancyViewModel>()
    private lateinit var skillsAdapter: VacancySkillsAdapter
    private val descriptionParser = VacancyDescriptionParser()
    private var likeMenuItem: MenuItem? = null
    private val vacancyId: String?
        get() = arguments?.getString(ARGS_VACANCY_ID)

    companion object {
        private const val ARGS_VACANCY_ID = "ARGS_VACANCY_ID"

        fun createArgs(vacancyId: String): Bundle = bundleOf(ARGS_VACANCY_ID to vacancyId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VacancyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        skillsAdapter = VacancySkillsAdapter()
        binding.skillsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = skillsAdapter
            isNestedScrollingEnabled = false
        }

        binding.vacancyFragmentTitle.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.retryButton.setOnClickListener {
            vacancyId?.let { viewModel.loadVacancy(it, forceReload = true) }
        }
        viewModel.state.observe(viewLifecycleOwner, ::render)
        val id = vacancyId
        if (id.isNullOrBlank()) {
            render(VacancyState.Error(ErrorType.BAD_REQUEST))
        } else {
            viewModel.loadVacancy(id)
        }

        viewModel.observeFavorite().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite == true) {
                likeMenuItem?.setIcon(R.drawable.ic_favorites_on__24px)
            } else {
                likeMenuItem?.setIcon(R.drawable.favorites_off__24px)
            }
        }

        binding.vacancyFragmentTitle.menu.clear()
        binding.vacancyFragmentTitle.inflateMenu(R.menu.vacancy_toolbar_menu)
        likeMenuItem = binding.vacancyFragmentTitle.menu.findItem(R.id.button_like)
        if (viewModel.observeFavorite().value == true) {
            likeMenuItem?.setIcon(R.drawable.ic_favorites_on__24px)
        } else {
            likeMenuItem?.setIcon(R.drawable.favorites_off__24px)
        }

        binding.vacancyFragmentTitle.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.button_like -> {
                    viewModel.onLikeButtonClick()
                    true
                }

                R.id.button_share -> {
                    viewModel.shareLink()
                    true
                }

                else -> false
            }
        }
    }

    private fun render(state: VacancyState) {
        when (state) {
            VacancyState.Loading -> {
                binding.loadingProgress.isVisible = true
                binding.vacancyContent.isVisible = false
                binding.errorGroup.isVisible = false
            }

            is VacancyState.Content -> {
                binding.loadingProgress.isVisible = false
                binding.errorGroup.isVisible = false
                binding.vacancyContent.isVisible = true
                showVacancy(state.vacancy)
            }

            is VacancyState.Error -> {
                binding.loadingProgress.isVisible = false
                binding.vacancyContent.isVisible = false
                binding.errorGroup.isVisible = true
                binding.errorMessage.setText(
                    when (state.error) {
                        ErrorType.NO_INTERNET -> R.string.network_error
                        ErrorType.NOT_FOUND -> R.string.error_vacancies
                        ErrorType.AUTHORIZATION_ERROR -> R.string.api_authorization_error
                        ErrorType.BAD_REQUEST -> R.string.vacancy_invalid_id
                        ErrorType.SERVER_ERROR -> R.string.server_error
                    }
                )
                binding.retryButton.isVisible =
                    state.error != ErrorType.NOT_FOUND &&
                        state.error != ErrorType.AUTHORIZATION_ERROR &&
                        state.error != ErrorType.BAD_REQUEST
            }
        }
    }

    private fun showVacancy(vacancy: VacancyDetail) {
        binding.vacancyName.text = vacancy.name
        binding.vacancySalary.text = SalaryFormatter.format(
            salary = vacancy.salary,
            resources = resources,
            somSignSupported = binding.vacancySalary.paint.hasGlyph("\u20C0")
        )
        binding.employerName.text = vacancy.employer.name
        binding.vacancyLocation.text = vacancy.address?.raw
            ?.takeIf(String::isNotBlank)
            ?: vacancy.area.name
        binding.vacancyLocation.isVisible = binding.vacancyLocation.text.isNotBlank()

        val experience = vacancy.experience?.name?.takeIf(String::isNotBlank)
        binding.metadataTitle.isVisible = experience != null
        binding.vacancyMetadata.text = experience.orEmpty()
        binding.vacancyMetadata.isVisible = experience != null
        val conditions = listOfNotNull(
            vacancy.employment?.name?.takeIf(String::isNotBlank),
            vacancy.schedule?.name?.takeIf(String::isNotBlank)
        )
        binding.vacancyConditions.text = conditions.joinToString(", ")
        binding.vacancyConditions.isVisible = conditions.isNotEmpty()

        val description = descriptionParser.parse(vacancy.description)
        val intro = formatDescriptionHtml(description.introHtml)
        val responsibilities = formatDescriptionHtml(description.responsibilitiesHtml)
        val requirements = formatDescriptionHtml(description.requirementsHtml)
        val terms = formatDescriptionHtml(description.termsHtml)
        binding.description.text = intro
        binding.description.isVisible = intro.isNotBlank()
        binding.responsibilitiesText.text = responsibilities
        binding.responsibilitiesSection.isVisible = responsibilities.isNotBlank()
        binding.requirementsText.text = requirements
        binding.requirementsSection.isVisible = requirements.isNotBlank()
        binding.termsText.text = terms
        binding.termsSection.isVisible = terms.isNotBlank()
        binding.descriptionSection.isVisible = listOf(
            intro,
            responsibilities,
            requirements,
            terms
        ).any(CharSequence::isNotBlank)

        val skills = vacancy.skills.filter(String::isNotBlank)
        skillsAdapter.submitList(skills)
        binding.skillsSection.isVisible = skills.isNotEmpty()

        binding.employerLogo.loadLogo(vacancy.employer.logo)
    }

    private fun formatDescriptionHtml(html: String): CharSequence {
        val spacedHtml = Regex(
            "(</(?:ul|ol|p)>)\\s*(<p\\b[^>]*>\\s*<(?:strong|b)\\b)",
            RegexOption.IGNORE_CASE
        ).replace(html) { match ->
            "${match.groupValues[1]}<br/>${match.groupValues[2]}"
        }
        val text = SpannableStringBuilder(
            HtmlCompat.fromHtml(spacedHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
        )
        removeDuplicateDescriptionTitle(text)
        return formatDescriptionContent(text)
    }

    private fun removeDuplicateDescriptionTitle(text: SpannableStringBuilder) {
        val headingStart = text.indexOfFirst { !it.isWhitespace() }
        if (headingStart < 0) return
        val newline = text.indexOf("\n", headingStart)
        val headingEnd = if (newline == -1) text.length else newline
        val heading = text.substring(headingStart, headingEnd).trim()
        if (heading.equals(getString(R.string.vacancy_description_title), ignoreCase = true)) {
            text.delete(0, if (newline == -1) text.length else newline + 1)
        }
    }

    private fun formatDescriptionContent(content: CharSequence): CharSequence {
        val formatted = SpannableStringBuilder(content)
        val bulletGap = resources.getDimensionPixelSize(R.dimen.vacancy_bullet_gap)
        formatted.getSpans(0, formatted.length, BulletSpan::class.java)
            .forEach { oldSpan ->
                val start = formatted.getSpanStart(oldSpan)
                val end = formatted.getSpanEnd(oldSpan)
                val flags = formatted.getSpanFlags(oldSpan)
                formatted.removeSpan(oldSpan)
                formatted.setSpan(BulletSpan(bulletGap), start, end, flags)
            }
        return formatted
    }

    override fun onDestroyView() {
        super.onDestroyView()
        likeMenuItem = null
        _binding = null
    }
}

package ru.practicum.android.diploma.vacancy.ui

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyFragmentBinding
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.SalaryFormatter

class VacancyFragment : Fragment() {
    private enum class DescriptionCategory {
        RESPONSIBILITIES,
        REQUIREMENTS,
        TERMS
    }

    private data class DescriptionParts(
        val intro: CharSequence,
        val responsibilities: CharSequence,
        val requirements: CharSequence,
        val terms: CharSequence
    )

    private var _binding: VacancyFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<VacancyViewModel>()
    private lateinit var skillsAdapter: VacancySkillsAdapter

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
            vacancyId?.let(viewModel::loadVacancy)
        }
        viewModel.state.observe(viewLifecycleOwner, ::render)
        val id = vacancyId
        if (id.isNullOrBlank()) {
            render(VacancyState.Error(ErrorType.NOT_FOUND))
        } else {
            viewModel.loadVacancy(id)
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
                        ErrorType.BAD_REQUEST, ErrorType.SERVER_ERROR -> R.string.server_error
                    }
                )
                binding.retryButton.isVisible =
                    state.error != ErrorType.NOT_FOUND &&
                        state.error != ErrorType.AUTHORIZATION_ERROR
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

        val description = parseDescription(vacancy.description)
        binding.description.text = formatDescriptionContent(description.intro)
        binding.description.isVisible = description.intro.isNotBlank()
        binding.responsibilitiesText.text =
            formatDescriptionContent(description.responsibilities)
        binding.responsibilitiesSection.isVisible = description.responsibilities.isNotBlank()
        binding.requirementsText.text = formatDescriptionContent(description.requirements)
        binding.requirementsSection.isVisible = description.requirements.isNotBlank()
        binding.termsText.text = formatDescriptionContent(description.terms)
        binding.termsSection.isVisible = description.terms.isNotBlank()
        binding.descriptionSection.isVisible = listOf(
            description.intro,
            description.responsibilities,
            description.requirements,
            description.terms
        ).any(CharSequence::isNotBlank)

        val skills = vacancy.skills.filter(String::isNotBlank)
        skillsAdapter.submitList(skills)
        binding.skillsSection.isVisible = skills.isNotEmpty()

        val logo = vacancy.employer.logo
        val isSvg = logo
            ?.let { Uri.parse(it).path }
            ?.endsWith(".svg", ignoreCase = true) == true
        binding.employerLogo.setLayerType(
            if (isSvg) View.LAYER_TYPE_SOFTWARE else View.LAYER_TYPE_NONE,
            null
        )
        val requestManager = Glide.with(binding.employerLogo)
        val request = if (isSvg) {
            requestManager
                .`as`(PictureDrawable::class.java)
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .load(logo)
        } else {
            requestManager.asDrawable().load(logo)
        }
        request
            .placeholder(R.drawable.ic_placeholder_32px)
            .error(R.drawable.ic_placeholder_32px)
            .fallback(R.drawable.ic_placeholder_32px)
            .into(binding.employerLogo)
    }

    private fun parseDescription(html: String): DescriptionParts {
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

        val headingPattern = Regex(
            "^(обязанности|требования(?:\\s+к\\s+[^:]+)?|условия(?:\\s+работы)?)\\s*:?\\s*(.*)$",
            RegexOption.IGNORE_CASE
        )
        val intro = SpannableStringBuilder()
        val sections = DescriptionCategory.values().associateWith {
            SpannableStringBuilder()
        }
        var currentCategory: DescriptionCategory? = null
        var segmentStart = 0

        fun appendSegment(end: Int) {
            val segment = trimmedSubsequence(text, segmentStart, end)
            if (segment.isNotBlank()) {
                val destination = currentCategory?.let(sections::get) ?: intro
                if (destination.isNotEmpty()) destination.append("\n")
                destination.append(segment)
            }
        }

        var lineStart = 0
        while (lineStart < text.length) {
            val newline = text.indexOf("\n", lineStart)
            val lineEnd = if (newline == -1) text.length else newline
            val rawLine = text.substring(lineStart, lineEnd)
            val trimmedLine = rawLine.trim()
            val match = headingPattern.matchEntire(trimmedLine)
            if (match != null) {
                appendSegment(lineStart)
                currentCategory = when {
                    match.groupValues[1].startsWith("обязанности", ignoreCase = true) ->
                        DescriptionCategory.RESPONSIBILITIES
                    match.groupValues[1].startsWith("требования", ignoreCase = true) ->
                        DescriptionCategory.REQUIREMENTS
                    else -> DescriptionCategory.TERMS
                }
                val leadingWhitespace = rawLine.indexOfFirst { !it.isWhitespace() }
                val body = match.groupValues[2]
                segmentStart = if (body.isBlank()) {
                    if (newline == -1) lineEnd else lineEnd + 1
                } else {
                    lineStart + leadingWhitespace + match.groups[2]!!.range.first
                }
            }
            lineStart = if (newline == -1) text.length else newline + 1
        }
        appendSegment(text.length)

        return DescriptionParts(
            intro = intro,
            responsibilities = sections.getValue(DescriptionCategory.RESPONSIBILITIES),
            requirements = sections.getValue(DescriptionCategory.REQUIREMENTS),
            terms = sections.getValue(DescriptionCategory.TERMS)
        )
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

    private fun trimmedSubsequence(text: CharSequence, start: Int, end: Int): CharSequence {
        var trimmedStart = start.coerceAtLeast(0)
        var trimmedEnd = end.coerceAtMost(text.length)
        while (trimmedStart < trimmedEnd && text[trimmedStart].isWhitespace()) trimmedStart++
        while (trimmedEnd > trimmedStart && text[trimmedEnd - 1].isWhitespace()) trimmedEnd--
        return text.subSequence(trimmedStart, trimmedEnd)
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
        _binding = null
    }
}

package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.models.area.FilterArea
import ru.practicum.android.diploma.domain.models.industries.FilterIndustry
import ru.practicum.android.diploma.domain.models.vacancy.Employer
import ru.practicum.android.diploma.domain.models.vacancy.Employment
import ru.practicum.android.diploma.domain.models.vacancy.Experience
import ru.practicum.android.diploma.domain.models.vacancy.Salary
import ru.practicum.android.diploma.domain.models.vacancy.Schedule
import ru.practicum.android.diploma.domain.models.vacancy.VacancyCard
import ru.practicum.android.diploma.domain.models.vacancy.VacancyDetail

class VacancyDbMapper {

    fun map(vacancy: VacancyDetail): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            url = vacancy.url,
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
            salaryCurrency = vacancy.salary?.currency,
            employerId = vacancy.employer.id,
            employerName = vacancy.employer.name,
            employerLogo = vacancy.employer.logo,
            areaId = vacancy.area.id,
            areaName = vacancy.area.name,
            industryId = vacancy.industry.id,
            industryName = vacancy.industry.name,
            experienceId = vacancy.experience?.id,
            experienceName = vacancy.experience?.name,
            scheduleId = vacancy.schedule?.id,
            scheduleName = vacancy.schedule?.name,
            employmentId = vacancy.employment?.id,
            employmentName = vacancy.employment?.name,
            address = vacancy.address,
            contacts = vacancy.contacts,
            skills = vacancy.skills,
            addedAt = System.currentTimeMillis()
        )
    }

    fun map(entity: VacancyEntity): VacancyDetail {
        return VacancyDetail(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            url = entity.url,
            salary = mapSalary(entity.salaryFrom, entity.salaryTo, entity.salaryCurrency),
            employer = Employer(
                id = entity.employerId,
                name = entity.employerName,
                logo = entity.employerLogo ?: ""
            ),
            area = FilterArea(
                id = entity.areaId,
                parentId = null,
                name = entity.areaName,
                areas = emptyList()
            ),
            industry = FilterIndustry(
                id = entity.industryId,
                name = entity.industryName
            ),
            experience = mapExperience(entity.experienceId, entity.experienceName),
            schedule = mapSchedule(entity.scheduleId, entity.scheduleName),
            employment = mapEmployment(entity.employmentId, entity.employmentName),
            address = entity.address,
            contacts = entity.contacts,
            skills = entity.skills
        )
    }

    fun mapToCard(entity: VacancyEntity): VacancyCard {
        return VacancyCard(
            id = entity.id,
            name = entity.name,
            company = entity.employerName,
            city = entity.areaName,
            salary = mapSalary(entity.salaryFrom, entity.salaryTo, entity.salaryCurrency),
            logo = entity.employerLogo
        )
    }

    fun mapToCardList(entities: List<VacancyEntity>): List<VacancyCard> {
        return entities.map { mapToCard(it) }
    }

    private fun mapSalary(from: Int?, to: Int?, currency: String?): Salary? {
        return if (from == null && to == null && currency == null) {
            null
        } else {
            Salary(
                id = null,
                from = from,
                to = to,
                currency = currency
            )
        }
    }

    private fun mapExperience(id: String?, name: String?): Experience? {
        return if (id != null && name != null) Experience(id = id, name = name) else null
    }

    private fun mapSchedule(id: String?, name: String?): Schedule? {
        return if (id != null && name != null) Schedule(id = id, name = name) else null
    }

    private fun mapEmployment(id: String?, name: String?): Employment? {
        return if (id != null && name != null) Employment(id = id, name = name) else null
    }
}

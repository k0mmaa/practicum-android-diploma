package ru.practicum.android.diploma.data.dto.requests

data class VacanciesRequest(
    val area: String? = null,
    val industry: String? = null,
    val text: String? = null,
    val salary: Int? = null,
    val page: Int? = null,
    val onlyWithSalary: Boolean? = null
) {
    fun toQueryMap(): Map<String, String> {
        val params = mutableMapOf<String, String>()

        area?.let {
            params["area"] = it
        }

        industry?.let {
            params["industry"] = it
        }

        text?.let {
            params["text"] = it
        }

        salary?.let {
            params["salary"] = it.toString()
        }

        page?.let {
            params["page"] = it.toString()
        }

        onlyWithSalary?.let {
            params["only_with_salary"] = it.toString()
        }

        return params
    }
}

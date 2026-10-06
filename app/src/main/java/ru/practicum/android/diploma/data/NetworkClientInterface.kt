package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.response.Response

interface NetworkClientInterface {

    suspend fun doRequest(dto: Any): Response
}

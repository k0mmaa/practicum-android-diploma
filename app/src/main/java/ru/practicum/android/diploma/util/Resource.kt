package ru.practicum.android.diploma.util

sealed class Resource<T>(val data: T? = null, val code: ErrorType? = null ) {

    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(code: ErrorType, data: T? = null) : Resource<T>(data, code)
}
enum class ErrorType{
    NO_INTERNET,
    NOT_FOUND,
    SERVER_ERROR,
    BAD_REQUEST
}

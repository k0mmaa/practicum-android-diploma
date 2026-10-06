package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClientInterface
import ru.practicum.android.diploma.data.dto.requests.AreasRequest
import ru.practicum.android.diploma.data.dto.response.area.AreasResponse
import ru.practicum.android.diploma.data.dto.requests.IndustriesRequest
import ru.practicum.android.diploma.data.dto.requests.VacanciesRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.response.industries.IndustriesResponse
import ru.practicum.android.diploma.data.dto.response.Response
import java.io.IOException
import ru.practicum.android.diploma.data.network.NetworkResultCode.BAD_REQUEST
import ru.practicum.android.diploma.data.network.NetworkResultCode.NO_INTERNET
import ru.practicum.android.diploma.data.network.NetworkResultCode.NOT_FOUND
import ru.practicum.android.diploma.data.network.NetworkResultCode.SERVER_ERROR
import ru.practicum.android.diploma.data.network.NetworkResultCode.SUCCESS
class JobsApiClient(
    private val jobsApiService: JobsApiService,
    private val context: Context
) : NetworkClientInterface {

    override  suspend fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            Log.e("JobsApiClient", "No internet connection")
            return Response().apply {  resultCode = NO_INTERNET }
        }

        return withContext(Dispatchers.IO){
            try {
                val response =  when (dto) {
                    is AreasRequest -> AreasResponse(areas = jobsApiService.areas())
                    is IndustriesRequest -> IndustriesResponse(industries = jobsApiService.industries())
                    is VacanciesRequest -> jobsApiService.vacancies(dto.toQueryMap())
                    is VacancyDetailsRequest -> jobsApiService.vacancy(dto.id)
                    else -> {
                        return@withContext Response().apply { resultCode = BAD_REQUEST }
                    }
                }
                response.apply {
                    resultCode = SUCCESS
                }
            }catch (e: HttpException) {
                if( e.code() == 404){
                    Response().apply { resultCode = NOT_FOUND  }
                }else{
                    Response().apply { resultCode = SERVER_ERROR }
                }
            }catch (e: IOException) {
                Response().apply { resultCode = NO_INTERNET }
            } catch (e: Exception){
                Log.e("JobsApiClient", "Network is unavailable or a connection error has occurred", e)
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }


    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

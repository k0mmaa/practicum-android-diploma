package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.network.JobsApiService

class RootActivity : AppCompatActivity() {
    private val apiService: JobsApiService by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.API_ACCESS_TOKEN)
        Log.i("ssaddsadas", BuildConfig.API_BASE_URL)

        lifecycleScope.launch {
            try {
                val areas = apiService.areas()
                Log.d("API_TEST", "areas = $areas")

                val industries = apiService.industries()
                Log.d("API_TEST", "industries = $industries")
            } catch (e: Exception) {
                Log.e("API_TEST", "error", e)
            }
        }
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}

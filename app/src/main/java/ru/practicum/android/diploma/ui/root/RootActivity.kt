package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView) { v, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(bottom = navBarInsets.bottom)
            insets
        }

        // Пример использования access token для HeadHunter API test
        networkRequestExample(accessToken = BuildConfig.API_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}

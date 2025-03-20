package ru.juni.rentcar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Проверка, был ли показан онбординг
        if (!isOnboardingCompleted()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        } else {
            setupMainScreen()
        }
        

        // Обработка системных окон (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Проверка, был ли завершен онбординг
    private fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", false)
    }

    // Настройка основного экрана
    private fun setupMainScreen() {
        // Здесь можно добавить логику для основного экрана
        // Например, загрузка данных, настройка UI и т.д.
    }
}
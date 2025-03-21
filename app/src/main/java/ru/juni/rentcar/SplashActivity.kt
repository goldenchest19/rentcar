package ru.juni.rentcar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Задержка перед переходом на следующий экран
        Handler(Looper.getMainLooper()).postDelayed({
            // Проверка, авторизован ли пользователь
            if (isUserLoggedIn()) {
                // Переход на главный экран
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Переход на экран онбординга
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish() // Закрыть SplashActivity
        }, 3000) // Задержка 3 секунды
    }

    private fun isUserLoggedIn(): Boolean {
        // Временно возвращаем false для тестирования онбординга
        return false
    }
}
package ru.juni.rentcar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import ru.juni.rentcar.auth.AuthChoiceActivity
import ru.juni.rentcar.base.BaseActivity
import ru.juni.rentcar.databinding.ActivitySplashBinding
import ru.juni.rentcar.onboarding.OnboardingActivity
import ru.juni.rentcar.utils.TokenManager

/**
 * Стартовая активность, которая отображает splash screen при запуске приложения
 */
class SplashActivity : BaseActivity() {

    companion object {
        private const val TAG = "SplashActivity"
        private const val SPLASH_DELAY = 3000L // 3 секунды
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate")

        // Задержка для отображения splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
            finish() // Закрываем SplashActivity
        }, SPLASH_DELAY)
    }

    /**
     * Определяет, какой следующий экран должен быть показан пользователю
     * и осуществляет переход к нему
     */
    private fun navigateToNextScreen() {
        Log.d(TAG, "Переход к следующему экрану")

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isOnboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)
        val tokenManager = TokenManager.getInstance(this)

        val intent = when {
            !isOnboardingCompleted -> Intent(this, OnboardingActivity::class.java)
            !tokenManager.isTokenValid() -> Intent(this, AuthChoiceActivity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
    }
}
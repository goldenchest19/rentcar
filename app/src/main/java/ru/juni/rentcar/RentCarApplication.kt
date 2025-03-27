package ru.juni.rentcar

import android.app.Application
import android.util.Log
import ru.juni.rentcar.utils.NetworkManager

/**
 * Класс приложения для инициализации глобальных компонентов
 */
class RentCarApplication : Application() {

    companion object {
        private const val TAG = "RentCarApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Инициализация приложения")

        // Инициализируем NetworkManager при запуске приложения
        NetworkManager.getInstance(applicationContext)
    }
} 
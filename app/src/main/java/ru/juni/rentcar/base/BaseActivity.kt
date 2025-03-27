package ru.juni.rentcar.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.utils.NetworkManager
import ru.juni.rentcar.utils.NoConnectionActivity

/**
 * Базовый класс для всех активностей приложения с проверкой интернет-соединения
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
        const val KEY_FROM_NO_CONNECTION = "from_no_connection"
    }

    private lateinit var networkManager: NetworkManager
    private var isObservingNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ${javaClass.simpleName}")

        // Инициализируем менеджер сети
        networkManager = NetworkManager.getInstance(applicationContext)

        // Проверяем, пришли ли мы с экрана отсутствия соединения
        val fromNoConnection = intent.getBooleanExtra(KEY_FROM_NO_CONNECTION, false)

        // Проверяем соединение только если не пришли с экрана отсутствия соединения
        if (!fromNoConnection) {
            checkInternetConnection()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${javaClass.simpleName}")

        // Начинаем наблюдать за состоянием сети если еще не наблюдаем
        if (!isObservingNetwork) {
            startObservingNetwork()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ${javaClass.simpleName}")
        stopObservingNetwork()
    }

    /**
     * Начинаем наблюдать за состоянием сети
     */
    private fun startObservingNetwork() {
        isObservingNetwork = true

        networkManager.isNetworkAvailable.observe(this) { isAvailable ->
            Log.d(TAG, "Состояние сети изменилось: доступна = $isAvailable")
            if (!isAvailable) {
                showNoConnectionScreen()
            }
        }

        // Принудительно проверяем соединение
        networkManager.checkNetworkState()
    }

    /**
     * Прекращаем наблюдать за состоянием сети
     */
    private fun stopObservingNetwork() {
        if (isObservingNetwork) {
            networkManager.isNetworkAvailable.removeObservers(this)
            isObservingNetwork = false
        }
    }

    /**
     * Проверяет наличие интернет-соединения
     */
    protected fun checkInternetConnection() {
        Log.d(TAG, "Проверка интернет-соединения")

        if (!networkManager.isInternetAvailable()) {
            Log.d(TAG, "Интернет недоступен, показываем экран отсутствия соединения")
            showNoConnectionScreen()
        } else {
            Log.d(TAG, "Интернет доступен")
        }
    }

    /**
     * Показывает экран отсутствия интернет-соединения
     */
    private fun showNoConnectionScreen() {
        Log.d(TAG, "Переход на экран отсутствия соединения")
        val intent = NoConnectionActivity.createIntent(this, this.javaClass)
        startActivity(intent)
    }
} 
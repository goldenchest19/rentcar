package ru.juni.rentcar.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.utils.NetworkManager
import ru.juni.rentcar.utils.NoConnectionActivity

/**
 * Базовый класс для всех активностей приложения.
 *
 * Этот класс реализует базовую функциональность для проверки и мониторинга интернет-соединения.
 * Все активности приложения должны наследоваться от этого класса для обеспечения
 * единообразной обработки состояния сети во всем приложении.
 *
 * Основные функции:
 * 1. Автоматическая проверка интернет-соединения при создании активности
 * 2. Непрерывный мониторинг состояния сети во время работы активности
 * 3. Автоматический переход на экран отсутствия соединения при потере связи
 * 4. Предотвращение циклических переходов между экранами при отсутствии соединения
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"

        /**
         * Ключ для определения, пришла ли активность с экрана отсутствия соединения
         * Используется для предотвращения бесконечных переходов между экранами
         */
        const val KEY_FROM_NO_CONNECTION = "from_no_connection"
    }

    /**
     * Менеджер для работы с сетью, реализующий паттерн Singleton
     */
    private lateinit var networkManager: NetworkManager

    /**
     * Флаг, указывающий, ведется ли в данный момент наблюдение за состоянием сети
     */
    private var isObservingNetwork = false

    /**
     * Инициализация активности.
     * Выполняет следующие действия:
     * 1. Инициализирует менеджер сети
     * 2. Проверяет, пришла ли активность с экрана отсутствия соединения
     * 3. При необходимости проверяет наличие интернет-соединения
     */
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

    /**
     * Возобновление активности.
     * Начинает наблюдение за состоянием сети, если оно еще не начато.
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${javaClass.simpleName}")

        // Начинаем наблюдать за состоянием сети если еще не наблюдаем
        if (!isObservingNetwork) {
            startObservingNetwork()
        }
    }

    /**
     * Приостановка активности.
     * Прекращает наблюдение за состоянием сети для оптимизации ресурсов.
     */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ${javaClass.simpleName}")
        stopObservingNetwork()
    }

    /**
     * Инициализация наблюдения за состоянием сети.
     *
     * Выполняет следующие действия:
     * 1. Устанавливает флаг наблюдения
     * 2. Подписывается на изменения состояния сети
     * 3. При потере соединения показывает экран отсутствия связи
     * 4. Выполняет начальную проверку состояния сети
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
     * Прекращение наблюдения за состоянием сети.
     *
     * Выполняет следующие действия:
     * 1. Отписывается от наблюдения за состоянием сети
     * 2. Сбрасывает флаг наблюдения
     */
    private fun stopObservingNetwork() {
        if (isObservingNetwork) {
            networkManager.isNetworkAvailable.removeObservers(this)
            isObservingNetwork = false
        }
    }

    /**
     * Проверка наличия интернет-соединения.
     *
     * Если соединение отсутствует, показывает экран отсутствия связи.
     * Используется при создании активности и может быть вызван вручную
     * из дочерних классов при необходимости.
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
     * Переход на экран отсутствия интернет-соединения.
     *
     * Создает и запускает NoConnectionActivity, передавая в нее информацию
     * о текущей активности для возможности возврата после восстановления соединения.
     */
    private fun showNoConnectionScreen() {
        Log.d(TAG, "Переход на экран отсутствия соединения")
        val intent = NoConnectionActivity.createIntent(this, this.javaClass)
        startActivity(intent)
    }
} 
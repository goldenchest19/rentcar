package ru.juni.rentcar.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Менеджер для мониторинга сетевого соединения.
 * Использует ConnectivityManager для определения наличия сетевого подключения
 * и уведомляет через LiveData об изменениях.
 */
class NetworkManager private constructor(private val applicationContext: Context) {
    
    companion object {
        private const val TAG = "NetworkManager"
        private const val PING_INTERVAL = 5000L  // 5 секунд
        
        @Volatile
        private var INSTANCE: NetworkManager? = null
        
        fun getInstance(context: Context): NetworkManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable
    
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d(TAG, "Сеть стала доступна")
            updateNetworkState(true)
        }
        
        override fun onLost(network: Network) {
            Log.d(TAG, "Сеть потеряна")
            updateNetworkState(false)
        }
        
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val hasValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            
            Log.d(TAG, "Изменились возможности сети: " +
                "INTERNET=${hasInternet}, " +
                "VALIDATED=${hasValidated}")
            
            updateNetworkState(hasInternet && hasValidated)
        }
    }
    
    private val handler = Handler(Looper.getMainLooper())
    private val pingRunnable = object : Runnable {
        override fun run() {
            Log.d(TAG, "Периодическая проверка состояния сети")
            checkNetworkState()
            handler.postDelayed(this, PING_INTERVAL)
        }
    }
    
    init {
        Log.d(TAG, "Инициализация NetworkManager")
        startMonitoring()
        startPeriodicChecking()
        
        // Начальная проверка состояния сети
        val initialState = isNetworkConnected()
        Log.d(TAG, "Начальное состояние сети: ${if (initialState) "доступна" else "недоступна"}")
        _isNetworkAvailable.postValue(initialState)
    }
    
    /**
     * Запускает мониторинг состояния сети через NetworkCallback
     */
    private fun startMonitoring() {
        try {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
                
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            Log.d(TAG, "Мониторинг сети запущен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при запуске мониторинга сети", e)
        }
    }
    
    /**
     * Запускает периодическую проверку доступа к интернету
     */
    private fun startPeriodicChecking() {
        handler.post(pingRunnable)
    }
    
    /**
     * Останавливает мониторинг сети
     */
    fun stopMonitoring() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            handler.removeCallbacks(pingRunnable)
            Log.d(TAG, "Мониторинг сети остановлен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при остановке мониторинга сети", e)
        }
    }
    
    /**
     * Проверяет текущее состояние сети
     */
    fun checkNetworkState() {
        val isConnected = isNetworkConnected()
        Log.d(TAG, "Проверка состояния сети: ${if (isConnected) "доступна" else "недоступна"}")
        updateNetworkState(isConnected)
    }
    
    /**
     * Обновляет состояние сети
     */
    private fun updateNetworkState(isAvailable: Boolean) {
        Log.d(TAG, "Обновление состояния сети: ${if (isAvailable) "доступна" else "недоступна"}")
        
        // Публикуем обновление только если состояние изменилось
        val currentValue = _isNetworkAvailable.value
        if (currentValue != isAvailable) {
            Log.d(TAG, "Состояние сети изменилось с ${currentValue} на ${isAvailable}")
            _isNetworkAvailable.postValue(isAvailable)
        }
    }
    
    /**
     * Проверяет наличие активного сетевого подключения
     */
    private fun isNetworkConnected(): Boolean {
        val network = connectivityManager.activeNetwork
        if (network == null) {
            Log.d(TAG, "Активная сеть отсутствует")
            return false
        }
        
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities == null) {
            Log.d(TAG, "Возможности сети недоступны")
            return false
        }
        
        // Проверяем, есть ли у сети возможность подключения к интернету
        val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        // Проверяем, есть ли у сети подтвержденное соединение
        val hasValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        
        Log.d(TAG, "Проверка возможностей сети: " +
            "INTERNET=${hasInternet}, " +
            "VALIDATED=${hasValidated}, " +
            "WIFI=${capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}, " +
            "CELLULAR=${capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}")
        
        // Сеть считается подключенной, если есть возможность подключения к интернету
        // и соединение подтверждено системой
        return hasInternet && hasValidated
    }
    
    /**
     * Синхронно проверяет наличие интернет-соединения
     */
    fun isInternetAvailable(): Boolean {
        return isNetworkAvailable.value ?: isNetworkConnected()
    }
} 
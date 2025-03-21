package ru.juni.rentcar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.juni.rentcar.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import java.net.HttpURLConnection
import java.net.URL

/**
 * Главный экран приложения.
 * Отвечает за мониторинг состояния интернет-соединения и перенаправление на экран
 * отсутствия подключения при необходимости.
 */
class MainActivity : AppCompatActivity() {

    // View Binding для доступа к элементам интерфейса
    private lateinit var binding: ActivityMainBinding
    
    // Менеджер для отслеживания состояния сети
    private lateinit var connectivityManager: ConnectivityManager
    
    // Callback для получения уведомлений об изменении состояния сети
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Настройка отступов для работы с системными панелями
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNetworkCallback()
        checkInternetConnection()
    }

    /**
     * Проверяет наличие интернет-соединения при запуске приложения.
     * Если соединение отсутствует, перенаправляет пользователя на экран NoConnectionActivity.
     */
    private fun checkInternetConnection() {
        thread {
            if (!isInternetAvailable() || !isOnline()) {
                runOnUiThread {
                    startActivity(NoConnectionActivity.createIntent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    /**
     * Проверяет наличие активного сетевого подключения через ConnectivityManager.
     * @return true если есть активное сетевое подключение, false в противном случае
     */
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Проверяет реальное наличие доступа к интернету путем попытки подключения к Google.
     * @return true если удалось подключиться к Google, false в случае ошибки
     */
    private fun isOnline(): Boolean {
        return try {
            val connection = URL("https://www.google.com").openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Test")
            connection.setRequestProperty("Connection", "close")
            connection.connectTimeout = 3000
            connection.connect()
            connection.responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Настраивает callback для отслеживания состояния сети.
     * При потере соединения проверяет реальную доступность интернета
     * и при необходимости показывает экран NoConnectionActivity.
     */
    private fun setupNetworkCallback() {
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                thread {
                    if (!isOnline()) {
                        runOnUiThread {
                            startActivity(NoConnectionActivity.createIntent(this@MainActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }

        // Создаем запрос на отслеживание состояния сети
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Регистрируем callback для получения уведомлений
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    /**
     * Отменяет регистрацию callback при уничтожении активности
     * для предотвращения утечек памяти.
     */
    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
package ru.juni.rentcar

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.auth.RegisterActivity
import ru.juni.rentcar.auth.RegisterStep2Activity
import ru.juni.rentcar.auth.RegisterStep3Activity
import ru.juni.rentcar.databinding.ActivityNoConnectionBinding
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * Activity для отображения экрана отсутствия интернет-соединения.
 * Позволяет пользователю повторить попытку подключения и вернуться к предыдущему экрану
 * при восстановлении соединения.
 */
class NoConnectionActivity : AppCompatActivity() {

    // View Binding для доступа к элементам интерфейса
    private lateinit var binding: ActivityNoConnectionBinding
    
    // Хранит имя класса предыдущей активности, чтобы вернуться к ней при восстановлении соединения
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем информацию о предыдущей активности из Intent
        previousActivity = intent.getStringExtra(EXTRA_PREVIOUS_ACTIVITY)

        setupClickListeners()
    }

    /**
     * Настраивает обработчики нажатий для кнопок.
     * При нажатии на кнопку "Повторить" запускает проверку интернет-соединения.
     */
    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            checkInternetConnection()
        }
    }

    /**
     * Проверяет наличие интернет-соединения в отдельном потоке.
     * Если соединение доступно, возвращает пользователя к предыдущему экрану.
     * Если соединение отсутствует, показывает Toast с сообщением об ошибке.
     */
    private fun checkInternetConnection() {
        thread {
            if (isInternetAvailable() && isOnline()) {
                runOnUiThread {
                    navigateBack()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Подключение к интернету отсутствует",
                        Toast.LENGTH_SHORT
                    ).show()
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
     * Осуществляет навигацию обратно к предыдущему экрану.
     * Создает Intent на основе сохраненного имени класса предыдущей активности.
     */
    private fun navigateBack() {
        val intent = when (previousActivity) {
            MainActivity::class.java.name -> Intent(this, MainActivity::class.java)
            RegisterActivity::class.java.name -> Intent(this, RegisterActivity::class.java)
            RegisterStep2Activity::class.java.name -> Intent(this, RegisterStep2Activity::class.java)
            RegisterStep3Activity::class.java.name -> Intent(this, RegisterStep3Activity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
        
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    companion object {
        // Ключ для передачи имени предыдущей активности через Intent
        private const val EXTRA_PREVIOUS_ACTIVITY = "extra_previous_activity"

        /**
         * Создает Intent для запуска NoConnectionActivity с информацией о предыдущей активности.
         * @param context контекст приложения
         * @param previousActivityClass класс предыдущей активности
         * @return Intent для запуска NoConnectionActivity
         */
        fun createIntent(context: Context, previousActivityClass: Class<*>): Intent {
            return Intent(context, NoConnectionActivity::class.java).apply {
                putExtra(EXTRA_PREVIOUS_ACTIVITY, previousActivityClass.name)
            }
        }
    }
}
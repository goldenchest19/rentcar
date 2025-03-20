package ru.juni.rentcar

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NoConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)

        val retryButton: Button = findViewById(R.id.retryButton)

        // Обработка нажатия кнопки "Повторить попытку"
        retryButton.setOnClickListener {
            if (isInternetAvailable()) {
                // Переход на главный экран или предыдущий процесс
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // Показ сообщения об ошибке
                Toast.makeText(this, "Подключение к интернету отсутствует", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Проверка доступности интернета
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
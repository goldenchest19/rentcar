package ru.juni.rentcar

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.auth.RegisterActivity
import ru.juni.rentcar.auth.RegisterStep2Activity
import ru.juni.rentcar.auth.RegisterStep3Activity
import ru.juni.rentcar.databinding.ActivityNoConnectionBinding

class NoConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoConnectionBinding
    private var previousActivity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем информацию о предыдущей активности
        previousActivity = intent.getStringExtra(EXTRA_PREVIOUS_ACTIVITY)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRetry.setOnClickListener {
            if (isInternetAvailable()) {
                navigateBack()
            } else {
                Toast.makeText(
                    this,
                    "Подключение к интернету отсутствует",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

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
        private const val EXTRA_PREVIOUS_ACTIVITY = "extra_previous_activity"

        fun createIntent(context: Context, previousActivityClass: Class<*>): Intent {
            return Intent(context, NoConnectionActivity::class.java).apply {
                putExtra(EXTRA_PREVIOUS_ACTIVITY, previousActivityClass.name)
            }
        }
    }
}
package ru.juni.rentcar.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.base.BaseActivity
import ru.juni.rentcar.databinding.ActivityNoConnectionBinding

/**
 * Activity для отображения экрана отсутствия интернет-соединения.
 * Позволяет пользователю повторить попытку подключения и вернуться к предыдущему экрану
 * только по нажатию кнопки "Повторить", даже если соединение было восстановлено.
 */
class NoConnectionActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NoConnectionActivity"
        private const val EXTRA_RETURN_CLASS = "extra_return_class"
        
        /**
         * Создает Intent для запуска NoConnectionActivity с указанием активности для возврата
         */
        fun createIntent(context: Context, returnActivity: Class<*>): Intent {
            return Intent(context, NoConnectionActivity::class.java).apply {
                putExtra(EXTRA_RETURN_CLASS, returnActivity)
            }
        }
    }
    
    private lateinit var binding: ActivityNoConnectionBinding
    private lateinit var networkManager: NetworkManager
    private var returnActivityClass: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Log.d(TAG, "onCreate")
        
        // Получаем класс активности для возврата
        returnActivityClass = intent.getSerializableExtra(EXTRA_RETURN_CLASS) as? Class<*>
        
        // Инициализируем менеджер сети
        networkManager = NetworkManager.getInstance(applicationContext)
        
        // Настраиваем кнопку повторной попытки
        binding.btnRetry.setOnClickListener {
            checkInternetAndReturn()
        }
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        
        // Принудительно запускаем проверку для обновления статуса интернета
        networkManager.checkNetworkState()
    }
    
    /**
     * Проверяет наличие интернет-соединения и возвращается на предыдущий экран
     * при его наличии. Вызывается только при нажатии на кнопку "Повторить".
     */
    private fun checkInternetAndReturn() {
        Log.d(TAG, "Проверка интернет-соединения по кнопке")
        
        // Обновляем состояние интернета
        networkManager.checkNetworkState()
        
        // Если интернет доступен - возвращаемся
        if (networkManager.isInternetAvailable()) {
            returnToPreviousScreen()
        }
    }
    
    /**
     * Возвращается на предыдущий экран
     */
    private fun returnToPreviousScreen() {
        Log.d(TAG, "Возвращаемся на предыдущий экран")
        
        if (returnActivityClass != null) {
            // Создаем Intent для возврата на указанную активность
            val returnIntent = Intent(this, returnActivityClass).apply {
                // Устанавливаем флаг, что мы возвращаемся с экрана ошибки
                putExtra(BaseActivity.KEY_FROM_NO_CONNECTION, true)
                // Очищаем стек активностей
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(returnIntent)
        }
        
        finish()
    }
}
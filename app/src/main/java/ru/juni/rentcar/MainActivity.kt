package ru.juni.rentcar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import ru.juni.rentcar.auth.AuthChoiceActivity
import ru.juni.rentcar.base.BaseActivity
import ru.juni.rentcar.databinding.ActivityMainBinding
import ru.juni.rentcar.home.HomeFragment
import ru.juni.rentcar.settings.SettingsFragment
import ru.juni.rentcar.utils.TokenManager

/**
 * Главный экран приложения.
 * Использует BaseActivity для автоматической проверки интернет-соединения.
 */
class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    // View Binding для доступа к элементам интерфейса
    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Инициализация менеджера токенов
        tokenManager = TokenManager.getInstance(this)
        
        // Настройка отступов для работы с системными панелями
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Инициализация UI-компонентов и загрузка данных
        setupUI()
        
        // По умолчанию показываем фрагмент главной страницы
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance())
        }
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }
    
    /**
     * Инициализирует пользовательский интерфейс и настраивает действия пользователя.
     */
    private fun setupUI() {
        Log.d(TAG, "Инициализация UI")
        
        // Настройка обработчика нажатия на нижнюю панель навигации
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment.newInstance())
                    true
                }
                R.id.navigation_bookmarks -> {
                    loadFragment(BookmarksFragment.newInstance())
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }
    
    /**
     * Загружает указанный фрагмент в контейнер.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    /**
     * Выполняет выход пользователя из аккаунта:
     * - очищает токен
     * - перенаправляет на экран выбора авторизации/регистрации
     */
    private fun logout() {
        Log.d(TAG, "Выход из аккаунта")
        
        // Очищаем токен авторизации
        tokenManager.clearToken()
        
        // Показываем сообщение об успешном выходе
        Toast.makeText(this, "Вы успешно вышли из аккаунта", Toast.LENGTH_SHORT).show()
        
        // Перенаправляем на экран выбора авторизации/регистрации
        val intent = Intent(this, AuthChoiceActivity::class.java)
        // Очищаем стек активностей, чтобы пользователь не мог вернуться на главный экран
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
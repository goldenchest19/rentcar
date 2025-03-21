package ru.juni.rentcar.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.MainActivity
import ru.juni.rentcar.R
import ru.juni.rentcar.databinding.ActivityLoginBinding

/**
 * Экран входа в приложение.
 * Позволяет пользователю войти в существующий аккаунт или перейти к регистрации.
 */
class LoginActivity : AppCompatActivity() {

    // View Binding для доступа к элементам интерфейса
    private lateinit var binding: ActivityLoginBinding
    // Флаг для отслеживания видимости пароля
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupClickListeners()
    }

    /**
     * Настраивает слушатели изменений текста для полей ввода.
     * При каждом изменении текста вызывается валидация полей.
     */
    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }
        }

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
    }

    /**
     * Настраивает обработчики нажатий для всех интерактивных элементов экрана.
     */
    private fun setupClickListeners() {
        // Переключение видимости пароля
        binding.btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }

        // Обработка нажатия кнопки входа
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            performLogin(email, password)
        }

        // Переход к экрану регистрации
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.tvForgotPassword.setOnClickListener {
        }
    }

    /**
     * Проверяет корректность введенных данных.
     * Включает/выключает кнопку входа в зависимости от валидности данных.
     */
    private fun validateInputs() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        binding.btnLogin.isEnabled = isEmailValid && isPasswordValid
    }

    /**
     * Переключает видимость пароля и обновляет иконку.
     */
    private fun togglePasswordVisibility() {
        binding.etPassword.transformationMethod = if (isPasswordVisible) {
            binding.btnTogglePassword.setImageResource(R.drawable.ic_eye_off)
            PasswordTransformationMethod.getInstance()
        } else {
            binding.btnTogglePassword.setImageResource(R.drawable.ic_eye)
            HideReturnsTransformationMethod.getInstance()
        }
        // Сохраняем позицию курсора
        binding.etPassword.setSelection(binding.etPassword.text.length)
    }

    /**
     * Выполняет процесс входа в систему.
     * TODO: Реализовать API запрос для аутентификации
     * @param email Email пользователя
     * @param password Пароль пользователя
     */
    private fun performLogin(email: String, password: String) {
        // TODO: Реализовать API запрос для входа
        // При успешном входе:
        saveAuthToken("your_auth_token")
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    /**
     * Сохраняет токен авторизации в SharedPreferences.
     * TODO: Реализовать безопасное хранение токена
     * @param token Токен авторизации
     */
    private fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
} 
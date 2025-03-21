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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupClickListeners()
    }

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

    private fun setupClickListeners() {
        binding.btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }

        binding.btnLogin.setOnClickListener {
            // TODO: Реализовать логику входа
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            performLogin(email, password)
        }

        binding.btnGoogleLogin.setOnClickListener {
            // TODO: Реализовать вход через Google
            performGoogleLogin()
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.tvForgotPassword.setOnClickListener {
            // TODO: Реализовать восстановление пароля
        }
    }

    private fun validateInputs() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        binding.btnLogin.isEnabled = isEmailValid && isPasswordValid
    }

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

    private fun performLogin(email: String, password: String) {
        // TODO: Реализовать API запрос для входа
        // При успешном входе:
        saveAuthToken("your_auth_token")
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun performGoogleLogin() {
        // TODO: Реализовать вход через Google OAuth
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
} 
package ru.juni.rentcar.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import ru.juni.rentcar.R
import ru.juni.rentcar.base.BaseActivity
import ru.juni.rentcar.databinding.ActivityRegisterBinding

/**
 * Экран регистрации пользователя.
 * Позволяет пользователю создать новый аккаунт.
 */
class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupClickListeners()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInputs(false)
            }
        }

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etConfirmPassword.addTextChangedListener(textWatcher)
        binding.cbTerms.setOnCheckedChangeListener { _, _ -> validateInputs(false) }
    }

    private fun setupClickListeners() {
        binding.btnTogglePassword.setOnClickListener {
            togglePasswordVisibility(true)
        }

        binding.btnToggleConfirmPassword.setOnClickListener {
            togglePasswordVisibility(false)
        }
        
        binding.btnBack.setOnClickListener {
            finish() // Закрываем текущую активность и возвращаемся к предыдущей
        }

        binding.btnNext.setOnClickListener {
            if (validateInputs(true)) {
                checkInternetConnection()
                startActivity(Intent(this, RegisterStep2Activity::class.java))
                finish()
            }
        }
    }

    private fun togglePasswordVisibility(isPassword: Boolean) {
        if (isPassword) {
            isPasswordVisible = !isPasswordVisible
            binding.etPassword.transformationMethod = if (isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(
                0, 0,
                if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                0
            )
            binding.etPassword.setSelection(binding.etPassword.text.length)
        } else {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            binding.etConfirmPassword.transformationMethod = if (isConfirmPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            binding.etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                0, 0,
                if (isConfirmPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off,
                0
            )
            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
        }
    }

    private fun validateInputs(showErrors: Boolean = false): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val isTermsAccepted = binding.cbTerms.isChecked

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6
        val doPasswordsMatch = password == confirmPassword

        if (showErrors) {
            var errorMessage = ""
            when {
                !isEmailValid -> errorMessage = "Пожалуйста, введите корректный email"
                !isPasswordValid -> errorMessage = "Пароль должен содержать минимум 6 символов"
                !doPasswordsMatch -> errorMessage = "Пароли не совпадают"
                !isTermsAccepted -> errorMessage = "Необходимо принять условия использования"
            }

            binding.tvError.text = errorMessage
            binding.tvError.visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val isValid = isEmailValid && isPasswordValid && doPasswordsMatch && isTermsAccepted
        binding.btnNext.isEnabled = isValid

        return isValid
    }
} 
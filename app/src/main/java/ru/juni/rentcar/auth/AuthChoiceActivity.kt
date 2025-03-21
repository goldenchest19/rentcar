package ru.juni.rentcar.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.databinding.ActivityAuthChoiceBinding

class AuthChoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            // Переход на экран входа
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            // Переход на экран регистрации
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
} 
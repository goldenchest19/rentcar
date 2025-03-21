package ru.juni.rentcar.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.MainActivity
import ru.juni.rentcar.databinding.ActivityRegistrationSuccessBinding

/**
 * Экран успешного завершения регистрации.
 * Показывает пользователю сообщение об успешной регистрации и перенаправляет на главный экран.
 */
class RegistrationSuccessActivity : AppCompatActivity() {

    // View Binding для доступа к элементам интерфейса
    private lateinit var binding: ActivityRegistrationSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    /**
     * Настраивает обработчики нажатий для всех интерактивных элементов экрана.
     */
    private fun setupClickListeners() {
        // Переход на главный экран с очисткой стека активностей
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
} 
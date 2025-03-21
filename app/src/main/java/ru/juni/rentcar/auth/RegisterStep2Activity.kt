package ru.juni.rentcar.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.databinding.ActivityRegisterStep2Binding
import java.text.SimpleDateFormat
import java.util.*

class RegisterStep2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStep2Binding
    private var selectedDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RegisterStep2Activity", "onCreate called")
        
        // Получаем данные из Intent
        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        
        Log.d("RegisterStep2Activity", "Received email: $email")
        
        binding = ActivityRegisterStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupClickListeners()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInputs(true)
            }
        }

        binding.etLastName.addTextChangedListener(textWatcher)
        binding.etFirstName.addTextChangedListener(textWatcher)
        binding.etMiddleName.addTextChangedListener(textWatcher)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.etBirthDate.setOnClickListener {
            showDatePicker()
        }

        binding.rgGender.setOnCheckedChangeListener { _, _ ->
            validateInputs()
        }

        binding.btnNext.setOnClickListener {
            if (validateInputs(true)) {
                val intent = Intent(this, RegisterStep3Activity::class.java).apply {
                    putExtra("email", email)
                    putExtra("password", password)
                    putExtra("lastName", binding.etLastName.text.toString().trim())
                    putExtra("firstName", binding.etFirstName.text.toString().trim())
                    putExtra("middleName", binding.etMiddleName.text.toString().trim())
                    putExtra("birthDate", binding.etBirthDate.text.toString())
                    putExtra("gender", if (binding.rbMale.isChecked) "male" else "female")
                }
                startActivity(intent)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = selectedDate ?: Calendar.getInstance()
        
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectedDate = calendar
                binding.etBirthDate.setText(dateFormat.format(calendar.time))
                validateInputs()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Установка максимальной даты (18 лет назад)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, -18)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        // Установка минимальной даты (100 лет назад)
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -100)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        datePickerDialog.show()
    }

    private fun validateInputs(showErrors: Boolean = false): Boolean {
        val lastName = binding.etLastName.text.toString().trim()
        val firstName = binding.etFirstName.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString()
        val isGenderSelected = binding.rgGender.checkedRadioButtonId != -1

        val isLastNameValid = lastName.isNotEmpty()
        val isFirstNameValid = firstName.isNotEmpty()
        val isBirthDateValid = birthDate.isNotEmpty()

        if (showErrors) {
            var errorMessage = ""
            when {
                !isLastNameValid -> errorMessage = "Пожалуйста, введите фамилию"
                !isFirstNameValid -> errorMessage = "Пожалуйста, введите имя"
                !isBirthDateValid -> errorMessage = "Пожалуйста, укажите дату рождения"
                !isGenderSelected -> errorMessage = "Пожалуйста, укажите пол"
            }

            binding.tvError.text = errorMessage
            binding.tvError.visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val isValid = isLastNameValid && isFirstNameValid && isBirthDateValid && isGenderSelected
        binding.btnNext.isEnabled = isValid

        return isValid
    }
} 
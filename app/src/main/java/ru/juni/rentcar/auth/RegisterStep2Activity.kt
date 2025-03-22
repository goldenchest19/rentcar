package ru.juni.rentcar.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.R
import ru.juni.rentcar.base.BaseActivity
import ru.juni.rentcar.databinding.ActivityRegisterStep2Binding

/**
 * Второй шаг регистрации пользователя.
 * Позволяет пользователю указать личные данные.
 */
class RegisterStep2Activity : BaseActivity() {

    companion object {
        private const val TAG = "RegisterStep2Activity"
    }

    private lateinit var binding: ActivityRegisterStep2Binding
    private var surname: String = ""
    private var name: String = ""
    private var patronymic: String = ""
    private var birthDate: String = ""
    private var gender: String = "male" // По умолчанию мужской пол
    
    // Календарь для работы с датой
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupClickListeners()
        setupDatePicker()
        setupRadioButtons()
    }

    private fun setupTextWatchers() {
        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                surname = s.toString()
                validateInputs()
            }
        })

        binding.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                name = s.toString()
                validateInputs()
            }
        })
        
        binding.etMiddleName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                patronymic = s.toString()
                validateInputs()
            }
        })
    }

    private fun setupRadioButtons() {
        // Установка обработчика выбора пола
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            gender = when (checkedId) {
                binding.rbMale.id -> "male"
                binding.rbFemale.id -> "female"
                else -> "male"
            }
            validateInputs()
        }
        
        // По умолчанию выбираем мужской пол
        binding.rbMale.isChecked = true
    }

    private fun setupDatePicker() {
        // Устанавливаем текущую дату как начальную
        val today = Calendar.getInstance()
        
        // Настраиваем диалог выбора даты
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            },
            today.get(Calendar.YEAR) - 18, // По умолчанию устанавливаем 18 лет назад
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )
        
        // Ограничиваем максимальную дату (сегодня)
        datePickerDialog.datePicker.maxDate = today.timeInMillis
        
        // Ограничиваем минимальную дату (100 лет назад)
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -100)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        
        // Устанавливаем обработчик нажатия на поле ввода даты
        binding.etBirthDate.setOnClickListener {
            datePickerDialog.show()
        }
    }
    
    private fun updateDateInView() {
        birthDate = dateFormat.format(calendar.time)
        binding.etBirthDate.setText(birthDate)
        validateInputs()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener {
            if (validateInputs()) {
                checkInternetConnection()
                val intent = Intent(this, RegisterStep3Activity::class.java)
                intent.putExtra("surname", surname)
                intent.putExtra("name", name)
                intent.putExtra("patronymic", patronymic)
                intent.putExtra("birthDate", birthDate)
                intent.putExtra("gender", gender)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        
        if (surname.isEmpty()) {
            binding.etLastName.error = "Введите фамилию"
            isValid = false
        } else {
            binding.etLastName.error = null
        }

        if (name.isEmpty()) {
            binding.etFirstName.error = "Введите имя"
            isValid = false
        } else {
            binding.etFirstName.error = null
        }
        
        if (birthDate.isEmpty()) {
            binding.etBirthDate.error = "Выберите дату рождения"
            isValid = false
        } else {
            binding.etBirthDate.error = null
        }
        
        // Отчество необязательное поле
        
        // Обновляем состояние кнопки "Далее"
        binding.btnNext.isEnabled = isValid
        
        return isValid
    }
} 
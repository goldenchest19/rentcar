package ru.juni.rentcar.auth

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.juni.rentcar.databinding.ActivityRegisterStep3Binding
import java.text.SimpleDateFormat
import java.util.*

class RegisterStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStep3Binding
    private var selectedDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var profilePhotoUri: Uri? = null
    private var licensePhotoUri: Uri? = null
    private var passportPhotoUri: Uri? = null

    private val profilePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            profilePhotoUri = result.data?.data
            binding.ivProfilePhoto.setImageURI(profilePhotoUri)
            validateInputs(true)
        }
    }

    private val licensePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            licensePhotoUri = result.data?.data
            binding.btnUploadLicense.text = "Фото загружено"
            validateInputs(true)
        }
    }

    private val passportPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            passportPhotoUri = result.data?.data
            binding.btnUploadPassport.text = "Фото загружено"
            validateInputs(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStep3Binding.inflate(layoutInflater)
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

        binding.etLicenseNumber.addTextChangedListener(textWatcher)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddPhoto.setOnClickListener {
            openGallery(profilePhotoLauncher)
        }

        binding.etIssueDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnUploadLicense.setOnClickListener {
            openGallery(licensePhotoLauncher)
        }

        binding.btnUploadPassport.setOnClickListener {
            openGallery(passportPhotoLauncher)
        }

        binding.btnNext.setOnClickListener {
            if (validateInputs(true)) {
                // TODO: Отправить данные на сервер
                val intent = Intent(this, RegistrationSuccessActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun openGallery(launcher: androidx.activity.result.ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
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
                binding.etIssueDate.setText(dateFormat.format(calendar.time))
                validateInputs(true)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Установка максимальной даты (сегодня)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Установка минимальной даты (10 лет назад)
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -10)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        datePickerDialog.show()
    }

    private fun validateInputs(showErrors: Boolean = false): Boolean {
        val licenseNumber = binding.etLicenseNumber.text.toString().trim()
        val issueDate = binding.etIssueDate.text.toString()

        val isLicenseNumberValid = licenseNumber.length == 10
        val isIssueDateValid = issueDate.isNotEmpty()
        val isLicensePhotoUploaded = licensePhotoUri != null
        val isPassportPhotoUploaded = passportPhotoUri != null

        if (showErrors) {
            var errorMessage = ""
            when {
                !isLicenseNumberValid -> errorMessage = "Введите корректный номер водительского удостоверения"
                !isIssueDateValid -> errorMessage = "Введите дату выдачи"
                !isLicensePhotoUploaded -> errorMessage = "Загрузите фото водительского удостоверения"
                !isPassportPhotoUploaded -> errorMessage = "Загрузите фото паспорта"
            }

            binding.tvError.text = errorMessage
            binding.tvError.visibility = if (errorMessage.isNotEmpty()) View.VISIBLE else View.GONE
        }

        val isValid = isLicenseNumberValid && isIssueDateValid && isLicensePhotoUploaded && isPassportPhotoUploaded
        binding.btnNext.isEnabled = isValid

        return isValid
    }
} 
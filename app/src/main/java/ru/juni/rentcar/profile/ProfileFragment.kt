package ru.juni.rentcar.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ru.juni.rentcar.R
import ru.juni.rentcar.auth.AuthChoiceActivity
import ru.juni.rentcar.databinding.FragmentProfileBinding
import ru.juni.rentcar.utils.TokenManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * Фрагмент профиля пользователя.
 * Отображает информацию о пользователе и предоставляет возможность редактирования.
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var tokenManager: TokenManager

    // Лаунчер для выбора изображения из галереи
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                // Обновляем аватар
                binding.ivUserAvatar.setImageURI(it)
                // Сохраняем путь к изображению
                saveAvatarPath(it.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Инициализация менеджера токенов
        tokenManager = TokenManager.getInstance(requireContext())
        
        // Загрузка данных пользователя
        loadUserData()
        
        // Настройка обработчиков нажатий
        setupClickListeners()
    }
    
    /**
     * Загружает данные пользователя из SharedPreferences.
     */
    private fun loadUserData() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        
        // Получаем данные пользователя
        val userName = sharedPreferences.getString("user_name", "Имя пользователя")
        val userEmail = sharedPreferences.getString("user_email", "email@example.com")
        val userGender = sharedPreferences.getString("user_gender", "Мужской")
        val userGoogleEmail = sharedPreferences.getString("user_google_email", "")
        val joinDate = sharedPreferences.getString("join_date", SimpleDateFormat("MMMM yyyy", Locale("ru")).format(Date()))
        val avatarPath = sharedPreferences.getString("avatar_path", null)
        
        // Отображаем данные на экране
        binding.tvUserName.text = userName
        binding.tvEmailValue.text = userEmail
        binding.tvGenderValue.text = userGender
        binding.tvJoinDate.text = getString(R.string.joined, joinDate)
        
        // Отображаем Google аккаунт, если он привязан
        if (userGoogleEmail.isNullOrEmpty()) {
            binding.tvGoogleValue.text = "Не привязан"
        } else {
            binding.tvGoogleValue.text = userGoogleEmail
        }
        
        // Загружаем аватар, если путь сохранен
        if (avatarPath != null) {
            try {
                binding.ivUserAvatar.setImageURI(Uri.parse(avatarPath))
            } catch (e: Exception) {
                // Если не удалось загрузить изображение, используем стандартный аватар
                binding.ivUserAvatar.setImageResource(R.drawable.ic_person)
            }
        } else {
            // Убедимся, что стандартная иконка отображается
            binding.ivUserAvatar.setImageResource(R.drawable.ic_person)
        }
    }
    
    /**
     * Сохраняет путь к выбранному изображению аватара.
     */
    private fun saveAvatarPath(path: String) {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("avatar_path", path).apply()
    }
    
    /**
     * Настраивает обработчики нажатий на элементы экрана.
     */
    private fun setupClickListeners() {
        // Нажатие на кнопку редактирования аватара
        binding.ivEditAvatar.setOnClickListener {
            openGallery()
        }
        
        // Нажатие на раздел аватара (также для выбора нового изображения)
        binding.flUserAvatar.setOnClickListener {
            openGallery()
        }
        
        // Нажатие на кнопку изменения пароля
        binding.clPassword.setOnClickListener {
            // TODO: Реализовать функционал изменения пароля
            Toast.makeText(requireContext(), "Функция изменения пароля будет доступна в следующей версии", Toast.LENGTH_SHORT).show()
        }
        
        // Нажатие на кнопку выхода из профиля
        binding.clLogout.setOnClickListener {
            logout()
        }
    }
    
    /**
     * Открывает галерею для выбора нового изображения аватара.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
    
    /**
     * Выполняет выход пользователя из аккаунта.
     */
    private fun logout() {
        // Очищаем токен авторизации
        tokenManager.clearToken()
        
        // Показываем сообщение об успешном выходе
        Toast.makeText(requireContext(), "Вы успешно вышли из аккаунта", Toast.LENGTH_SHORT).show()
        
        // Перенаправляем на экран выбора авторизации/регистрации
        val intent = Intent(requireContext(), AuthChoiceActivity::class.java)
        // Очищаем стек активностей, чтобы пользователь не мог вернуться на главный экран
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        fun newInstance() = ProfileFragment()
    }
} 
package ru.juni.rentcar.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.juni.rentcar.R
import ru.juni.rentcar.databinding.FragmentSettingsBinding
import ru.juni.rentcar.profile.ProfileFragment
import ru.juni.rentcar.utils.TokenManager

/**
 * Фрагмент настроек приложения.
 * Отображает профиль пользователя и опции настроек.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация менеджера токенов
        tokenManager = TokenManager.getInstance(requireContext())

        // Загрузка данных пользователя
        loadUserData()

        // Настройка обработчиков нажатий на пункты меню
        setupClickListeners()
    }

    /**
     * Загружает данные пользователя из SharedPreferences и отображает их в интерфейсе.
     */
    private fun loadUserData() {
        // Получение данных пользователя из SharedPreferences
        val sharedPreferences =
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", "Имя пользователя")
        val userEmail = sharedPreferences.getString("user_email", "email@example.com")

        // Отображение данных пользователя
        binding.tvUserName.text = userName
        binding.tvUserEmail.text = userEmail
    }

    /**
     * Настраивает обработчики нажатий на пункты меню настроек.
     */
    private fun setupClickListeners() {
        // Обработчик нажатия на профиль пользователя
        binding.cvProfile.setOnClickListener {
            // Переход на экран профиля
            navigateToProfile()
        }

        // Мои бронирования
        binding.flMyBookings.setOnClickListener {
            // TODO: Переход на экран бронирований
            showMessage(getString(R.string.my_bookings))
        }

        // Тема
        binding.flTheme.setOnClickListener {
            // TODO: Переход на экран выбора темы
            showMessage(getString(R.string.theme))
        }

        // Уведомления
        binding.flNotifications.setOnClickListener {
            // TODO: Переход на экран настроек уведомлений
            showMessage(getString(R.string.notifications))
        }

        // Подключить свой автомобиль
        binding.flConnectCar.setOnClickListener {
            // TODO: Переход на экран добавления автомобиля
            showMessage(getString(R.string.connect_car))
        }

        // Помощь
        binding.flHelp.setOnClickListener {
            // TODO: Переход на экран помощи
            showMessage(getString(R.string.help))
        }

        // Пригласи друга
        binding.flInviteFriend.setOnClickListener {
            // TODO: Реализация приглашения друга
            showMessage(getString(R.string.invite_friend))
        }
    }

    /**
     * Переходит на экран профиля пользователя.
     */
    private fun navigateToProfile() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    /**
     * Показывает уведомление пользователю.
     */
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
} 
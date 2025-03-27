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
 * 
 * Этот фрагмент является центральным местом для управления настройками приложения и профилем пользователя.
 * Основные функции:
 * 1. Отображение информации о пользователе
 * 2. Навигация к различным разделам настроек
 * 3. Управление темами приложения
 * 4. Настройка уведомлений
 * 5. Управление бронированиями
 * 6. Подключение собственного автомобиля
 * 7. Доступ к помощи и поддержке
 * 8. Приглашение друзей
 */
class SettingsFragment : Fragment() {

    /**
     * ViewBinding для доступа к элементам интерфейса.
     * Используется для безопасного доступа к view-элементам без необходимости использования findViewById.
     */
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    /**
     * Менеджер для работы с токенами авторизации.
     * Используется для управления состоянием авторизации пользователя.
     */
    private lateinit var tokenManager: TokenManager

    /**
     * Создание и инициализация view-элементов фрагмента.
     * 
     * @param inflater Инфлейтер для создания view из layout-файла
     * @param container Родительский контейнер для view
     * @param savedInstanceState Сохраненное состояние фрагмента
     * @return Корневой view фрагмента
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Инициализация фрагмента после создания view.
     * Выполняет следующие действия:
     * 1. Инициализирует менеджер токенов
     * 2. Загружает данные пользователя
     * 3. Настраивает обработчики нажатий на пункты меню
     */
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
     * 
     * Загружает и отображает:
     * - Имя пользователя
     * - Email пользователя
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
     * 
     * Обрабатывает нажатия на следующие разделы:
     * 1. Профиль пользователя
     * 2. Мои бронирования
     * 3. Настройки темы
     * 4. Настройки уведомлений
     * 5. Подключение автомобиля
     * 6. Помощь и поддержка
     * 7. Приглашение друзей
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
     * 
     * Заменяет текущий фрагмент на ProfileFragment и добавляет транзакцию
     * в стек возврата для возможности возврата на экран настроек.
     */
    private fun navigateToProfile() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    /**
     * Показывает уведомление пользователю.
     * 
     * @param message Текст сообщения для отображения
     */
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Очистка ресурсов при уничтожении view.
     * Освобождает ссылку на binding для предотвращения утечек памяти.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Создает новый экземпляр фрагмента настроек.
         * 
         * @return Новый экземпляр SettingsFragment
         */
        fun newInstance() = SettingsFragment()
    }
} 
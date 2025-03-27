package ru.juni.rentcar.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import ru.juni.rentcar.R
import ru.juni.rentcar.adapters.CarAdapter
import ru.juni.rentcar.databinding.FragmentSearchResultsBinding
import ru.juni.rentcar.home.ShimmerAdapter
import ru.juni.rentcar.models.Car
import java.util.UUID

/**
 * Фрагмент для отображения результатов поиска автомобилей.
 */
class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!

    private lateinit var carAdapter: CarAdapter
    private lateinit var shimmerAdapter: ShimmerAdapter
    private var shimmerFrameLayout: ShimmerFrameLayout? = null

    private val searchQuery: String by lazy { arguments?.getString(ARG_SEARCH_QUERY) ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        setupToolbar()
        setupRecyclerView()
        setupShimmer()
        setupErrorHandling()

        // Запускаем поиск с полученным запросом
        performSearch(searchQuery)
    }

    private fun setupStatusBar() {
        // Делаем статус-бар прозрачным
        val window = requireActivity().window
        window.statusBarColor =
            ContextCompat.getColor(requireContext(), android.R.color.transparent)

        // Устанавливаем темные иконки на светлом фоне
        try {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } catch (e: Exception) {
            // Игнорируем возможные ошибки на старых устройствах
        }
    }

    private fun setupToolbar() {
        // Настраиваем заголовок
        binding.tvTitle.text = "Результаты поиска: \"$searchQuery\""

        // Настраиваем кнопку "Назад"
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            carList = emptyList(),
            onBookClickListener = { car ->
                // В реальном приложении здесь была бы логика бронирования
                Toast.makeText(
                    requireContext(),
                    "Автомобиль ${car.brand} ${car.model} забронирован",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onDetailsClickListener = { car ->
                // В реальном приложении здесь был бы переход на экран деталей
                Toast.makeText(
                    requireContext(),
                    "Детали автомобиля ${car.brand} ${car.model}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
        }
    }

    private fun setupShimmer() {
        // Инициализируем ShimmerFrameLayout
        shimmerFrameLayout = view?.findViewById(R.id.shimmerFrameLayout)

        // Настраиваем адаптер для отображения заглушек во время загрузки
        shimmerAdapter = ShimmerAdapter(4) // Показываем 4 заглушки
        val rvShimmer =
            view?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvShimmer)
        rvShimmer?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shimmerAdapter
        }
    }

    private fun setupErrorHandling() {
        binding.btnRetry.setOnClickListener {
            performSearch(searchQuery)
        }
    }

    private fun performSearch(query: String) {
        // Показываем анимацию загрузки
        showShimmer(true)

        // В реальном приложении здесь был бы запрос к API или базе данных
        // Имитация задержки поиска
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) {
                val results = searchMockCars(query)

                if (results.isEmpty()) {
                    showError("По запросу \"$query\" ничего не найдено")
                } else {
                    carAdapter.updateData(results)
                    showResults()
                }
            }
        }, 1500) // Задержка для демонстрации загрузки
    }

    private fun searchMockCars(query: String): List<Car> {
        val allCars = createMockCarList()

        return if (query.isEmpty()) {
            allCars
        } else {
            allCars.filter { car ->
                car.brand.lowercase().contains(query.lowercase()) ||
                        car.model.lowercase().contains(query.lowercase())
            }
        }
    }

    private fun showShimmer(show: Boolean) {
        if (show) {
            binding.shimmerFrameLayout.visibility = View.VISIBLE
            binding.shimmerFrameLayout.startShimmer()

            binding.rvSearchResults.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.llError.visibility = View.GONE
        } else {
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.GONE
        }
    }

    private fun showResults() {
        showShimmer(false)
        binding.progressBar.visibility = View.GONE
        binding.rvSearchResults.visibility = View.VISIBLE
        binding.llError.visibility = View.GONE
    }

    private fun showError(message: String) {
        showShimmer(false)
        binding.progressBar.visibility = View.GONE
        binding.rvSearchResults.visibility = View.GONE
        binding.llError.visibility = View.VISIBLE
        binding.tvErrorMessage.text = message
    }

    private fun createMockCarList(): List<Car> {
        return listOf(
            Car(
                id = UUID.randomUUID().toString(),
                brand = "Mercedes-Benz",
                model = "S 500 Sedan",
                price = 5000,
                imageUrl = "https://avatars.mds.yandex.net/get-verba/1540742/2a0000017761d120cb174f35f4f8717d1635/cattouchret",
                transmission = "А/Т",
                fuelType = "Бензин",
                year = 2023,
                doors = 4,
                seats = 5,
                description = "Mercedes-Benz S-класса — это воплощение роскоши и комфорта. Автомобиль оснащен передовыми технологиями и инновационными системами помощи водителю."
            ),
            Car(
                id = UUID.randomUUID().toString(),
                brand = "BMW",
                model = "7 Series",
                price = 4500,
                imageUrl = "https://avatars.mds.yandex.net/get-verba/1030388/2a0000017de3f10c8ffeb3f0ea0e0d5f6638/cattouchret",
                transmission = "А/Т",
                fuelType = "Дизель",
                year = 2023,
                doors = 4,
                seats = 5,
                description = "BMW 7 серии сочетает в себе спортивную динамику и элегантный дизайн. Этот автомобиль создан для тех, кто ценит престиж и высокие технологии."
            ),
            Car(
                id = UUID.randomUUID().toString(),
                brand = "Audi",
                model = "A8 L",
                price = 4800,
                imageUrl = "https://avatars.mds.yandex.net/get-verba/1540742/2a0000017619d7adec33b0bfa407fb367eca/cattouchret",
                transmission = "А/Т",
                fuelType = "Бензин",
                year = 2023,
                doors = 4,
                seats = 5,
                description = "Audi A8 L — это флагманский седан с инновационными технологиями и превосходной динамикой. Он обеспечивает непревзойденный комфорт и безопасность."
            ),
            Car(
                id = UUID.randomUUID().toString(),
                brand = "Tesla",
                model = "Model S",
                price = 5500,
                imageUrl = "https://avatars.mds.yandex.net/get-verba/1540742/2a0000017de40da84e23f5ce7a5ca75a40ba/cattouchret",
                transmission = "А/Т",
                fuelType = "Электро",
                year = 2023,
                doors = 4,
                seats = 5,
                description = "Tesla Model S — это полностью электрический седан с впечатляющим запасом хода и ускорением. Он предлагает передовые технологии автопилота и обширный сенсорный экран."
            ),
            Car(
                id = UUID.randomUUID().toString(),
                brand = "Toyota",
                model = "Camry",
                price = 2500,
                imageUrl = "https://avatars.mds.yandex.net/get-verba/1030388/2a0000017de41218f38f4cd9e8af56ed0ca4/cattouchret",
                transmission = "А/Т",
                fuelType = "Бензин",
                year = 2023,
                doors = 4,
                seats = 5,
                description = "Toyota Camry — это надежный и комфортабельный бизнес-седан, который сочетает в себе элегантный дизайн и экономичный расход топлива."
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Восстанавливаем прозрачный статус-бар при уходе с фрагмента
        val window = requireActivity().window
        window.statusBarColor =
            ContextCompat.getColor(requireContext(), android.R.color.transparent)

        // Возвращаем темные иконки на светлом фоне
        try {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } catch (e: Exception) {
            // Игнорируем возможные ошибки на старых устройствах
        }

        _binding = null
    }

    companion object {
        private const val ARG_SEARCH_QUERY = "search_query"

        fun newInstance(searchQuery: String): SearchResultsFragment {
            return SearchResultsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SEARCH_QUERY, searchQuery)
                }
            }
        }
    }
} 
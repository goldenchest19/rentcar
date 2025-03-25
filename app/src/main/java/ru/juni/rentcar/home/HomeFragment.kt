package ru.juni.rentcar.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.juni.rentcar.R
import ru.juni.rentcar.adapters.CarAdapter
import ru.juni.rentcar.databinding.FragmentHomeBinding
import ru.juni.rentcar.models.Car
import ru.juni.rentcar.search.SearchResultsFragment
import java.util.UUID

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var carAdapter: CarAdapter
    private val allCars = mutableListOf<Car>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupStatusBar()
        setupRecyclerView()
        setupSearchListener()
        setupErrorHandling()
        loadCars()
    }
    
    private fun setupStatusBar() {
        // Делаем статус-бар прозрачным
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
        
        // Устанавливаем темные иконки на светлом (розовом) фоне
        try {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } catch (e: Exception) {
            // Игнорируем возможные ошибки на старых устройствах
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
        
        binding.rvCars.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
        }
    }
    
    private fun setupSearchListener() {
        // Обработка клика на иконку поиска
        binding.ivSearch.setOnClickListener {
            performSearch()
        }
        
        // Обработка нажатия Enter в строке поиска
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun setupErrorHandling() {
        binding.btnRetry.setOnClickListener {
            loadCars()
        }
    }
    
    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        
        if (query.isEmpty()) {
            Toast.makeText(requireContext(), "Введите поисковый запрос", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Переходим на экран результатов поиска
        val searchResultsFragment = SearchResultsFragment.newInstance(query)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchResultsFragment)
            .addToBackStack(null)
            .commit()
    }
    
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvCars.visibility = View.GONE
        binding.llError.visibility = View.GONE
    }
    
    private fun loadCars() {
        // Имитация загрузки данных
        showLoading()
        
        // В реальном приложении здесь был бы запрос к API или базе данных
        allCars.clear()
        allCars.addAll(createMockCarList())
        
        // Имитация задержки загрузки
        binding.root.postDelayed({
            if (isAdded) {
                binding.progressBar.visibility = View.GONE
                
                if (allCars.isEmpty()) {
                    showError("Не удалось загрузить список автомобилей")
                } else {
                    showCarList()
                    carAdapter.updateData(allCars)
                }
            }
        }, 1500)
    }
    
    private fun showCarList() {
        binding.progressBar.visibility = View.GONE
        binding.rvCars.visibility = View.VISIBLE
        binding.llError.visibility = View.GONE
    }
    
    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.rvCars.visibility = View.GONE
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
        // Восстанавливаем обычный статус-бар при уходе с фрагмента
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        
        // Возвращаем светлые иконки на темном фоне
        try {
            window.decorView.systemUiVisibility = 0
        } catch (e: Exception) {
            // Игнорируем возможные ошибки на старых устройствах
        }
        
        _binding = null
    }
    
    companion object {
        fun newInstance() = HomeFragment()
    }
} 
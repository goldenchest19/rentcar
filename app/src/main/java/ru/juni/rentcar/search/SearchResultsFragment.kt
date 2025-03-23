package ru.juni.rentcar.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.juni.rentcar.R
import ru.juni.rentcar.adapters.CarAdapter
import ru.juni.rentcar.databinding.FragmentSearchResultsBinding
import ru.juni.rentcar.models.Car
import java.util.UUID

/**
 * Фрагмент для отображения результатов поиска автомобилей.
 */
class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var carAdapter: CarAdapter
    private var searchQuery = ""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Получаем строку поиска из аргументов
        arguments?.let {
            searchQuery = it.getString("searchQuery", "")
        }
        
        setupToolbar()
        setupRecyclerView()
        loadSearchResults(searchQuery)
    }
    
    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            // Возвращаемся назад по стеку фрагментов
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    
    private fun setupRecyclerView() {
        carAdapter = CarAdapter(
            carList = emptyList(),
            onBookClickListener = { car ->
                // В реальном приложении здесь был бы переход на экран оформления аренды
                Toast.makeText(
                    requireContext(), 
                    "Переход к оформлению аренды ${car.brand} ${car.model}", 
                    Toast.LENGTH_SHORT
                ).show()
                
                // Имитация перехода на экран оформления аренды с использованием Fragment Transaction
                // val bookingFragment = BookingFragment.newInstance(car.id)
                // requireActivity().supportFragmentManager.beginTransaction()
                //     .replace(R.id.fragment_container, bookingFragment)
                //     .addToBackStack(null)
                //     .commit()
            },
            onDetailsClickListener = { car ->
                // В реальном приложении здесь был бы переход на экран деталей
                Toast.makeText(
                    requireContext(), 
                    "Переход к деталям ${car.brand} ${car.model}", 
                    Toast.LENGTH_SHORT
                ).show()
                
                // Имитация перехода на экран деталей автомобиля с использованием Fragment Transaction
                // val detailsFragment = CarDetailsFragment.newInstance(car.id)
                // requireActivity().supportFragmentManager.beginTransaction()
                //     .replace(R.id.fragment_container, detailsFragment)
                //     .addToBackStack(null)
                //     .commit()
            }
        )
        
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
        }
    }
    
    private fun loadSearchResults(query: String) {
        showLoading()
        
        // В реальном приложении здесь был бы запрос к API или базе данных
        // Имитация задержки загрузки (1 секунда)
        binding.root.postDelayed({
            if (isAdded) {
                val searchResults = performSearch(query)
                
                if (searchResults.isEmpty()) {
                    showError(getString(R.string.no_search_results))
                } else {
                    showResults(searchResults)
                }
            }
        }, 1000)
    }
    
    private fun performSearch(query: String): List<Car> {
        // Имитация поиска в базе данных
        // В реальном приложении здесь был бы запрос к API с параметром поиска
        val allCars = createMockCarList()
        
        return if (query.isEmpty()) {
            allCars
        } else {
            allCars.filter { car ->
                car.brand.contains(query, ignoreCase = true) || 
                car.model.contains(query, ignoreCase = true)
            }
        }
    }
    
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvSearchResults.visibility = View.GONE
        binding.llError.visibility = View.GONE
    }
    
    private fun showResults(cars: List<Car>) {
        binding.progressBar.visibility = View.GONE
        binding.rvSearchResults.visibility = View.VISIBLE
        binding.llError.visibility = View.GONE
        
        carAdapter.updateData(cars)
    }
    
    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.rvSearchResults.visibility = View.GONE
        binding.llError.visibility = View.VISIBLE
        binding.tvErrorMessage.text = message
        
        binding.btnRetry.setOnClickListener {
            loadSearchResults(searchQuery)
        }
    }
    
    private fun createMockCarList(): List<Car> {
        return listOf(
            Car(
                id = UUID.randomUUID().toString(),
                brand = "Mercedes-Benz",
                model = "S 500 Sedan",
                price = 2500,
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
                price = 3000,
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
                price = 2800,
                imageUrl = "https://avatars.mds.yandex.net/get-verba/1540742/2a0000017619d7adec33b0bfa407fb367eca/cattouchret",
                transmission = "А/Т",
                fuelType = "Бензин",
                year = 2023,
                doors = 4,
                seats = 5,
                description = "Audi A8 L — это флагманский седан с инновационными технологиями и превосходной динамикой. Он обеспечивает непревзойденный комфорт и безопасность."
            )
        )
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        fun newInstance(searchQuery: String) = SearchResultsFragment().apply {
            arguments = Bundle().apply {
                putString("searchQuery", searchQuery)
            }
        }
    }
} 
package ru.juni.rentcar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.juni.rentcar.R
import ru.juni.rentcar.models.Car

/**
 * Адаптер для отображения списка автомобилей в RecyclerView.
 * Этот класс отвечает за:
 * 1. Создание и управление представлениями для каждого элемента списка автомобилей
 * 2. Привязку данных автомобиля к соответствующим View-элементам
 * 3. Обработку взаимодействий пользователя (нажатия на кнопки)
 */
class CarAdapter(
    // Список автомобилей для отображения
    private var carList: List<Car>,
    // Callback-функция, вызываемая при нажатии на кнопку бронирования
    private val onBookClickListener: (Car) -> Unit,
    // Callback-функция, вызываемая при нажатии на кнопку деталей
    private val onDetailsClickListener: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    /**
     * Создает новый ViewHolder для элемента списка.
     * @param parent Родительский ViewGroup
     * @param viewType Тип представления (не используется в данном случае)
     * @return Новый экземпляр CarViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    /**
     * Привязывает данные автомобиля к ViewHolder.
     * @param holder ViewHolder для элемента списка
     * @param position Позиция элемента в списке
     */
    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.bind(car)
    }

    /**
     * Возвращает общее количество элементов в списке.
     * @return Количество автомобилей в списке
     */
    override fun getItemCount(): Int = carList.size

    /**
     * Обновляет данные в адаптере.
     * @param newCarList Новый список автомобилей
     */
    fun updateData(newCarList: List<Car>) {
        this.carList = newCarList
        notifyDataSetChanged()
    }

    /**
     * ViewHolder для хранения ссылок на View-элементы элемента списка.
     * Улучшает производительность RecyclerView, избегая повторных поисков View-элементов.
     */
    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // View-элементы для отображения информации об автомобиле
        private val ivCarImage: ImageView = itemView.findViewById(R.id.ivCarImage)
        private val tvCarModel: TextView = itemView.findViewById(R.id.tvCarModel)
        private val tvCarBrand: TextView = itemView.findViewById(R.id.tvCarBrand)
        private val tvCarPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        private val tvTransmission: TextView = itemView.findViewById(R.id.tvTransmission)
        private val tvFuelType: TextView = itemView.findViewById(R.id.tvFuelType)
        private val btnBook: Button = itemView.findViewById(R.id.btnBook)
        private val btnDetails: Button = itemView.findViewById(R.id.btnDetails)

        /**
         * Привязывает данные автомобиля к View-элементам.
         * @param car Объект автомобиля с данными для отображения
         */
        fun bind(car: Car) {
            // Установка текстовых данных
            tvCarModel.text = car.model
            tvCarBrand.text = car.brand
            tvCarPrice.text = "${car.price}₽ в день"
            tvTransmission.text = car.transmission
            tvFuelType.text = car.fuelType

            // Загрузка изображения автомобиля с помощью библиотеки Glide
            Glide.with(itemView.context)
                .load(R.drawable.car)
                .into(ivCarImage)

            // Установка обработчиков нажатий на кнопки
            btnBook.setOnClickListener {
                onBookClickListener(car)
            }

            btnDetails.setOnClickListener {
                onDetailsClickListener(car)
            }
        }
    }
} 
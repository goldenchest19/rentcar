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

class CarAdapter(
    private var carList: List<Car>,
    private val onBookClickListener: (Car) -> Unit,
    private val onDetailsClickListener: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.bind(car)
    }

    override fun getItemCount(): Int = carList.size

    fun updateData(newCarList: List<Car>) {
        this.carList = newCarList
        notifyDataSetChanged()
    }

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCarImage: ImageView = itemView.findViewById(R.id.ivCarImage)
        private val tvCarModel: TextView = itemView.findViewById(R.id.tvCarModel)
        private val tvCarBrand: TextView = itemView.findViewById(R.id.tvCarBrand)
        private val tvCarPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        private val tvTransmission: TextView = itemView.findViewById(R.id.tvTransmission)
        private val tvFuelType: TextView = itemView.findViewById(R.id.tvFuelType)
        private val btnBook: Button = itemView.findViewById(R.id.btnBook)
        private val btnDetails: Button = itemView.findViewById(R.id.btnDetails)

        fun bind(car: Car) {
            tvCarModel.text = car.model
            tvCarBrand.text = car.brand
            tvCarPrice.text = "${car.price}₽ в день"
            tvTransmission.text = car.transmission
            tvFuelType.text = car.fuelType

            // Загрузка изображения с помощью Glide
            Glide.with(itemView.context)
                .load(car.imageUrl)
                .placeholder(R.drawable.ic_car_placeholder)
                .error(R.drawable.ic_car_placeholder)
                .into(ivCarImage)

            btnBook.setOnClickListener {
                onBookClickListener(car)
            }

            btnDetails.setOnClickListener {
                onDetailsClickListener(car)
            }
        }
    }
} 
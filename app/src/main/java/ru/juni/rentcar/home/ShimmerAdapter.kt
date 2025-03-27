package ru.juni.rentcar.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.juni.rentcar.databinding.ItemCarShimmerBinding

class ShimmerAdapter(private val itemCount: Int = 3) :
    RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    inner class ShimmerViewHolder(binding: ItemCarShimmerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarShimmerBinding.inflate(inflater, parent, false)
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // Ничего не делаем, т.к. это просто заглушка
    }

    override fun getItemCount(): Int = itemCount
} 
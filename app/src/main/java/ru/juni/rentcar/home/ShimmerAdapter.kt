package ru.juni.rentcar.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.juni.rentcar.databinding.ItemCarShimmerBinding

/**
 * Адаптер для отображения эффекта мерцания (shimmer) во время загрузки данных.
 *
 * Этот адаптер используется для создания эффекта загрузки в RecyclerView,
 * когда данные еще не загружены. Он отображает пустые элементы с анимацией мерцания,
 * что улучшает пользовательский опыт во время ожидания данных.
 */
class ShimmerAdapter(private val itemCount: Int = 3) :
    RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    /**
     * ViewHolder для элемента с эффектом мерцания.
     *
     * @param binding ViewBinding для доступа к элементам интерфейса
     */
    inner class ShimmerViewHolder(binding: ItemCarShimmerBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Создание нового ViewHolder для элемента списка.
     *
     * @param parent Родительский ViewGroup
     * @param viewType Тип представления (не используется)
     * @return Новый экземпляр ShimmerViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarShimmerBinding.inflate(inflater, parent, false)
        return ShimmerViewHolder(binding)
    }

    /**
     * Привязка данных к ViewHolder.
     * В данном случае ничего не делает, так как это просто заглушка для эффекта мерцания.
     *
     * @param holder ViewHolder для элемента списка
     * @param position Позиция элемента в списке
     */
    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // Ничего не делаем, т.к. это просто заглушка
    }

    /**
     * Возвращает количество элементов в списке.
     *
     * @return Количество элементов с эффектом мерцания
     */
    override fun getItemCount(): Int = itemCount
} 
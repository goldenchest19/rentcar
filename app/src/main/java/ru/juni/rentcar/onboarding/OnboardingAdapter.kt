package ru.juni.rentcar.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.juni.rentcar.R
import ru.juni.rentcar.databinding.ItemOnboardingBinding

/**
 * Адаптер для отображения слайдов онбординга в ViewPager2.
 * Управляет отображением элементов онбординга, включая изображение, заголовок и описание.
 *
 * @property onboardingItems Список элементов онбординга для отображения
 */
class OnboardingAdapter(private val context: Context) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val onboardingItems = listOf(
        OnboardingItem(
            R.drawable.img_fragment_onb1,
            "Аренда автомобилей",
            "Открой для себя удобный и доступный способ передвижения"
        ),
        OnboardingItem(
            R.drawable.ic_logo_onb2,
            "Лучшие предложения",
            "Выбирай понравившееся среди сотен доступных автомобилей"
        ),
        OnboardingItem(
            R.drawable.ic_logo_onb3,
            "Безопасно и удобно",
            "Арендуй автомобиль и наслаждайся его удобством"
        )
    )

    /**
     * ViewHolder для элемента онбординга.
     * Содержит и управляет отображением одного слайда онбординга.
     *
     * @property binding View Binding для доступа к элементам разметки
     */
    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Привязывает данные элемента онбординга к соответствующим view.
         *
         * @param onboardingItem Элемент онбординга для отображения
         */
        fun bind(item: OnboardingItem, position: Int) {
            with(binding) {
                imageOnboarding.setImageResource(item.image)
                titleOnboarding.text = item.title
                descriptionOnboarding.text = item.description

                // Обновляем индикаторы
                indicator1.setBackgroundResource(if (position == 0) R.drawable.indicator_active else R.drawable.indicator_inactive)
                indicator2.setBackgroundResource(if (position == 1) R.drawable.indicator_active else R.drawable.indicator_inactive)
                indicator3.setBackgroundResource(if (position == 2) R.drawable.indicator_active else R.drawable.indicator_inactive)
            }
        }
    }

    /**
     * Создает новый ViewHolder для элемента онбординга.
     *
     * @param parent Родительская ViewGroup
     * @param viewType Тип представления (не используется в данном случае)
     * @return Новый экземпляр OnboardingViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    /**
     * Привязывает данные к ViewHolder в указанной позиции.
     *
     * @param holder ViewHolder для привязки данных
     * @param position Позиция элемента в списке
     */
    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position], position)
    }

    /**
     * Возвращает общее количество элементов в адаптере.
     *
     * @return Количество элементов онбординга
     */
    override fun getItemCount(): Int = onboardingItems.size

    data class OnboardingItem(
        val image: Int,
        val title: String,
        val description: String
    )
}
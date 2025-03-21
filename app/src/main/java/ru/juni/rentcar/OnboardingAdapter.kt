package ru.juni.rentcar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.juni.rentcar.databinding.ItemOnboardingBinding

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingItems[position], position)
    }

    override fun getItemCount(): Int = onboardingItems.size

    inner class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingItem, position: Int) {
            binding.imageOnboarding.setImageResource(item.imageRes)
            binding.titleOnboarding.text = item.title
            binding.descriptionOnboarding.text = item.description

            // Обновляем индикаторы
            binding.indicator1.setBackgroundResource(if (position == 0) R.drawable.indicator_active else R.drawable.indicator_inactive)
            binding.indicator2.setBackgroundResource(if (position == 1) R.drawable.indicator_active else R.drawable.indicator_inactive)
            binding.indicator3.setBackgroundResource(if (position == 2) R.drawable.indicator_active else R.drawable.indicator_inactive)
        }
    }

    data class OnboardingItem(
        val imageRes: Int,
        val title: String,
        val description: String
    )
}
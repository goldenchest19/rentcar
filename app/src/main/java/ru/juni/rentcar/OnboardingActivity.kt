package ru.juni.rentcar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ru.juni.rentcar.auth.AuthChoiceActivity
import ru.juni.rentcar.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка ViewPager
        viewPager = binding.onboardingViewPager
        val adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter

        // Обработка кнопок
        binding.skipButton.setOnClickListener {
            finishOnboarding()
        }

        binding.nextButton.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }

        binding.startButton.setOnClickListener {
            finishOnboarding()
        }

        // Скрыть/показать кнопки при перелистывании
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == adapter.itemCount - 1) {
                    binding.skipButton.visibility = View.GONE
                    binding.nextButton.visibility = View.GONE
                    binding.startButton.visibility = View.VISIBLE
                } else {
                    binding.skipButton.visibility = View.VISIBLE
                    binding.nextButton.visibility = View.VISIBLE
                    binding.startButton.visibility = View.GONE
                }
            }
        })
    }

    private fun finishOnboarding() {
        saveOnboardingState()
        startActivity(Intent(this, AuthChoiceActivity::class.java))
        finish()
    }

    private fun saveOnboardingState() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
    }
}
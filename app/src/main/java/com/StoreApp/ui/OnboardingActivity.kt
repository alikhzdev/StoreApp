package com.StoreApp.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.StoreApp.Adapter.OnboardingAdapter
import com.StoreApp.KotlinActivity
import com.StoreApp.R
import com.StoreApp.databinding.ActivityOnboardingBinding


class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up ViewPager2
        binding.viewPager.adapter = OnboardingAdapter(this)
        binding.viewPager.isUserInputEnabled = true // Enable swiping

        // Update indicators based on ViewPager position
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })
    }

    fun moveToNextPage() {
        val currentItem = binding.viewPager.currentItem
        if (currentItem < 2) {
            binding.viewPager.currentItem = currentItem + 1
        } else {
            completeOnboarding()
        }
    }

    @SuppressLint("UseKtx")
    fun completeOnboarding() {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("isOnboardingCompleted", true)
            .apply()
        startActivity(Intent(this, KotlinActivity::class.java))
        finish()
    }

    private fun updateIndicators(position: Int) {
        val fragment = supportFragmentManager.findFragmentByTag("f${binding.viewPager.currentItem}")
        val view = fragment?.view
        val indicatorLayout = view?.findViewById<LinearLayout>(R.id.indicatorLayout)
        indicatorLayout?.let {
            for (i in 0 until it.childCount) {
                val indicator = it.getChildAt(i)
                indicator.setBackgroundResource(
                    if (i == position) R.drawable.rectangle_7 else R.drawable.rectangle_8
                )
            }
        }
    }
}
package com.StoreApp.Adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.StoreApp.R
import com.StoreApp.onboarding.Onboarding1Fragment
import com.StoreApp.onboarding.Onboarding2Fragment
import com.StoreApp.onboarding.Onboarding3Fragment

class OnboardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Onboarding1Fragment.newInstance(R.layout.page_one_onboarding)
            1 -> Onboarding2Fragment.newInstance(R.layout.page_tuo_onboarding)
            2 -> Onboarding3Fragment.newInstance(R.layout.page_tree_onboarding)
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
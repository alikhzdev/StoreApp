package com.StoreApp.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.StoreApp.R
import com.StoreApp.ui.OnboardingActivity

class Onboarding3Fragment : Fragment() {
    companion object {
        private const val ARG_LAYOUT_ID = "layout_id"

        fun newInstance(layoutId: Int): Onboarding3Fragment {
            val fragment = Onboarding3Fragment()
            val args = Bundle()
            args.putInt(ARG_LAYOUT_ID, layoutId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getInt(ARG_LAYOUT_ID) ?: R.layout.page_tree_onboarding
        val view = inflater.inflate(layoutId, container, false)

        // Handle Skip/Get Started button
        view.findViewById<TextView>(R.id.tvSkip)?.setOnClickListener {
            (requireActivity() as OnboardingActivity).completeOnboarding()
        }

        // Handle Next button (image_front)
        view.findViewById<ImageView>(R.id.image_front)?.setOnClickListener {
            (requireActivity() as OnboardingActivity).moveToNextPage()
        }

        return view
    }
}
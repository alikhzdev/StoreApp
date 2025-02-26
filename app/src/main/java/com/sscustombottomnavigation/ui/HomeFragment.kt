package com.sscustombottomnavigation.ui

import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sscustombottomnavigation.R
import com.sscustombottomnavigation.VerticalAdapter
import com.sscustombottomnavigation.classData_for_Home
import com.sscustombottomnavigation.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    val data = listOf(
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        classData_for_Home("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1)
    )


    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView:RecyclerView = binding.recyclerViewHome
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // دو ستونه
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = VerticalAdapter(data)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val newHeight = if (dy > 0) 1395 else 1500 // ارتفاع‌های موردنظر
                val animator = ValueAnimator.ofInt(recyclerView.height, newHeight)
                animator.addUpdateListener { valueAnimator ->
                    recyclerView.layoutParams.height = valueAnimator.animatedValue as Int
                    recyclerView.requestLayout()
                }
                animator.duration = 300 // زمان انیمیشن
                animator.start()
            }
        })

    }



}
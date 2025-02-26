package com.sscustombottomnavigation.ui

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sscustombottomnavigation.ClassData_for_favorite
import com.sscustombottomnavigation.HorizontalAdapter
import com.sscustombottomnavigation.HorizontalAdapter2
import com.sscustombottomnavigation.R
import com.sscustombottomnavigation.databinding.FragmentFavoriteBinding
import java.sql.Ref

class FavoriteFragment : Fragment() {
    private lateinit var buttons: List<Button>

    val data1 = listOf(
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1)
    )
    val data2 = listOf(
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1),
        ClassData_for_favorite("وظایف کامل شده\n من نمی دانم", "6استکر", R.drawable.background_cardview1)
    )

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttons = listOf(
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                highlightButton(button)
            }
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = HorizontalAdapter(data1)

        val recyclerView2 = binding.recyclerView2
        recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView2.adapter = HorizontalAdapter2(data2)


        recyclerView2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView2, dx, dy)

                val newHeight = if (dy > 0) 850 else 900 // ارتفاع‌های موردنظر
                val animator = ValueAnimator.ofInt(recyclerView2.height, newHeight)
                animator.addUpdateListener { valueAnimator ->
                    recyclerView2.layoutParams.height = valueAnimator.animatedValue as Int
                    recyclerView2.requestLayout()
                }
                animator.duration = 300 // زمان انیمیشن
                animator.start()
            }
        })





        //val adapterTest = Adapter_for_favorite(data)
        //val recycler = binding.gridfavorit
       //recycler.adapter = adapterTest

    }







    private fun highlightButton(selectedButton: Button) {
        // همه دکمه‌ها را به حالت عادی برگردان
        buttons.forEach { it.setTypeface(null, Typeface.NORMAL) }

        // دکمه‌ای که کلیک شده را برجسته کن
        selectedButton.setTypeface(null, Typeface.BOLD)
    }
}
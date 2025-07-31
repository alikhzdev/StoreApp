package com.StoreApp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.Adapter.AdapterNotification
import com.StoreApp.databinding.FragmentNotificationsBinding
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var viewModel: EmojiStoreViewModel
    val layoutManager = object : LinearLayoutManager(context) {
        override fun canScrollVertically(): Boolean {
            return false // جلوگیری از اسکرول عمودی
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation", "DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(EmojiStoreViewModel::class.java)
        val adapter = AdapterNotification(requireContext(), emptyList(),viewModel)
        val recyclerView: RecyclerView = binding.recyclerViewNotif
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // دو ستونه
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
//        val itemHeight = 300 // ارتفاع هر آیتم به پیکسل (یا dp تبدیل شده)
//        val itemCount = AdapterNotification(requireContext(),data).itemCount
//        val totalHeight = itemHeight * itemCount
//        recyclerView.layoutParams.height = totalHeight
//        recyclerView.requestLayout()

        viewModel.cartDisplayItems.observe(viewLifecycleOwner) { cartDisplayItems ->
            Log.e("aaaaa","$cartDisplayItems")
            adapter.updateList(cartDisplayItems)
        }


        viewModel.totalCartPrice.observe(viewLifecycleOwner) { totalPrice ->
            binding.textpol.text = "${String.format("%.2f", totalPrice)} تومان "

        viewModel.discountAmount.observe(viewLifecycleOwner) { discountAmount ->
            binding.texttakhfif.text = "${String.format("%.2f", discountAmount)} تومان"
        }

        binding.buttonPardakht.setOnClickListener{
            val cartnewMy = viewModel.cartDisplayItems.value?: emptyList()
                if(cartnewMy.isNotEmpty()){
                    cartnewMy.forEach{pack ->
                        Log.e("alikhpk12","$pack")
                        viewModel.addToMy(pack.id)
                    }
                }
                viewModel.clearCart()

            binding.buttonPardakht.text = "حالشا ببر "
            Toast.makeText(requireContext(),"حالشا ببر", Toast.LENGTH_SHORT).show()
        }





//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val newHeight = if (dy > 0) 1895 else 2000 // ارتفاع‌های موردنظر
//                val animator = ValueAnimator.ofInt(recyclerView.height, newHeight)
//                animator.addUpdateListener { valueAnimator ->
//                    recyclerView.layoutParams.height = valueAnimator.animatedValue as Int
//                    recyclerView.requestLayout()
//                }
//                animator.duration = 300 // زمان انیمیشن
//                animator.start()
//            }
//        })
    }


    }
}
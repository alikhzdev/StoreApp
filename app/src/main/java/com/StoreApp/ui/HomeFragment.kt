package com.StoreApp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.Adapter.VerticalAdapter
import com.StoreApp.databinding.FragmentHomeBinding
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel

class HomeFragment : Fragment() {



    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: EmojiStoreViewModel


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





        binding.switchfavorite.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.switchfavorite.isChecked = false
                }, 200)
                Toast.makeText(requireContext(),"این بخش به زودی اضافه میشه",Toast.LENGTH_SHORT).show()
            }
        }

        viewModel = ViewModelProvider(this).get(EmojiStoreViewModel::class.java)
        val adapter = VerticalAdapter(requireContext(), emptyList(),viewModel)
        val recyclerView:RecyclerView = binding.recyclerViewHome
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // دو ستونه
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter


        viewModel.cartDisplayMys.observe(viewLifecycleOwner){cartDisplayMys ->
            Log.e("alikhni","$cartDisplayMys")
            adapter.updateList(cartDisplayMys)
        }







    }



}
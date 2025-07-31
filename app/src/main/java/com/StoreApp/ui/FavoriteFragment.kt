package com.StoreApp.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.StoreApp.Adapter.AutocompleteAdapter
import com.StoreApp.Adapter.HorizontalAdapter2
import com.StoreApp.Adapter.HorizontalAdapter3
import com.StoreApp.R
import com.StoreApp.databinding.FragmentFavoriteBinding
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel

class FavoriteFragment : Fragment() {
    private lateinit var buttons: List<Button>
    private val viewModel: EmojiStoreViewModel by viewModels()
    val list = listOf(
        R.drawable.ic_emoji,
        R.drawable.ic_emoji,
        R.drawable.ic_emoji,
        R.drawable.ic_emoji,
        R.drawable.ic_emoji,
        R.drawable.ic_emoji
    )
    private lateinit var adapter3: HorizontalAdapter3
    private lateinit var adapter2: HorizontalAdapter2
    private lateinit var autoCompleteAdapter: AutocompleteAdapter

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


    @SuppressLint("FragmentLiveDataObserve", "ClickableViewAccessibility")
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
                when (button) {
                    binding.btn1 -> {
                        binding.categoryFirstContent.visibility = View.VISIBLE
                        binding.LinearLayout.visibility = View.GONE
                    }

                    else -> {
                        binding.categoryFirstContent.visibility = View.GONE
                        binding.LinearLayout.visibility = View.VISIBLE
                    }

                }
            }


        }


//


        val recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter3 = HorizontalAdapter3(requireContext(), emptyList())
        recyclerView.adapter = adapter3

        val recyclerView2 = binding.recyclerView2
        recyclerView2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter2 = HorizontalAdapter2(requireContext(), emptyList())
        recyclerView2.adapter = adapter2

        val recyclerView0 = binding.recyclerView0
        recyclerView0.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView0.adapter = adapter2

        val autoCompleteTextView = binding.editText as AutoCompleteTextView
        autoCompleteTextView.dropDownHeight = 300 // ارتفاع دلخواه
        autoCompleteAdapter = AutocompleteAdapter(requireContext(), R.layout.autocomplete_item, emptyList())
        autoCompleteTextView.setAdapter(autoCompleteAdapter)
        autoCompleteTextView.threshold = 1 // لیست با هر تایپ باز بشه
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCategory = autoCompleteAdapter.getItem(position)
            selectedCategory?.let { category ->
                viewModel.filterPacksByCategory(category.name)
                autoCompleteTextView.setText(category.name)
            }
        }
        autoCompleteTextView.setOnTouchListener { _, event ->
            Log.e("FavoriteFragment", "Touch detected, showing dropdown")
            autoCompleteTextView.showDropDown()
            false
        }
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            autoCompleteTextView.dismissDropDown()
        }


        viewModel.packDisplayList.observe(this) { packList ->
            Log.e("FavoriteFragment", "Pack list size: ${packList.size}")
            adapter3.updateList(packList)
            adapter2.updateList(packList)
            // استخراج دسته‌بندی‌های منحصربه‌فرد
            val categories = packList.flatMap { it.categories }.distinctBy { it.name }
            Log.e("FavoriteFragment", "Categories size: ${categories.size}, Categories: ${categories.map { it.name }}")
            autoCompleteAdapter =
                AutocompleteAdapter(requireContext(), R.layout.autocomplete_item, categories)
            autoCompleteTextView.setAdapter(autoCompleteAdapter)
            autoCompleteAdapter.notifyDataSetChanged()
        }


//        binding.editText.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let { viewModel.filterPacks(it) }
//                Log.e("MainActivity", "Search submitted: $query")
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { viewModel.filterPacks(it) }
//                Log.e("MainActivity", "Search changed: $newText")
//                return true
//
//            }
//        })


        viewModel.filteredPackList.observe(this) { filteredList ->
            Log.e("FavoriteFragment", "Filtered list size: ${filteredList?.size ?: 0}")
            if (filteredList != null) {
                adapter2.updateList(filteredList)
                adapter3.updateList(filteredList)
            } else {
                Toast.makeText(requireContext(), "هیچ داده‌ای یافت نشد", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun highlightButton(selectedButton: Button) {
        // همه دکمه‌ها را به حالت عادی برگردان
        buttons.forEach { it.textSize = 14F }

        // دکمه‌ای که کلیک شده را برجسته کن
        selectedButton.textSize = 17F
    }
}
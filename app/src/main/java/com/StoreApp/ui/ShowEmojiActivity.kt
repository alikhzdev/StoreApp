package com.StoreApp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.Adapter.EmojiAdapter
import com.StoreApp.R
import com.StoreApp.databinding.ActivityShowEmojiAvtivityBinding
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel
import kotlin.properties.Delegates

class ShowEmojiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowEmojiAvtivityBinding
    lateinit var emojis : ArrayList<String>
    var ids by Delegates.notNull<Int>()
    lateinit var viewModel: EmojiStoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShowEmojiAvtivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(EmojiStoreViewModel::class.java)



        binding.imageView.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }




        val source = intent.getStringExtra("source")
        when(source){
            "From_Favorite_part1" -> getDataFavorite()
            "From_Favorite_part2" ->  getDataFavorite1()
            "from_notifications" ->  getDataNotifications()
        }


        val recyclerView: RecyclerView = binding.recyclerViewEmoji
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = gridLayoutManager
        Log.e("emoji","$emojis")
        recyclerView.adapter = EmojiAdapter(emojis)


        binding.button.setOnClickListener{
            viewModel.addToCart(ids)
            binding.button.text = "به سبد خرید اضافه شد "
            Toast.makeText(this,"پک به سد خرید اضافه شد",Toast.LENGTH_SHORT).show()
        }
    }


    private fun setSingleButtonMode() {
        val id_btnpol = binding.btnpol
        val parent = binding.parentLayout

        Log.d("ShowEmojiActivity", "Setting single button mode for $id_btnpol")

        // حذف دکمه از LinearLayout فعلی
        (id_btnpol.parent as? ViewGroup)?.removeView(id_btnpol)

        // مخفی کردن LinearLayout اصلی
        binding.linearButton.visibility = View.GONE

        // ایجاد پارامترهای جدید برای ConstraintLayout
        val newParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // تنظیم constraints جدید
            topToBottom = R.id.cartView  // قرار گرفتن زیر CardView اصلی
            bottomToTop = R.id.button    // قرار گرفتن بالای دکمه "افزودن به سبد خرید"
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

            // تنظیم marginها
            topMargin = 16.dpToPx(this@ShowEmojiActivity)  // تبدیل dp به px
            bottomMargin = 16.dpToPx(this@ShowEmojiActivity)
        }

        // افزودن دکمه به ConstraintLayout اصلی
        parent.addView(id_btnpol, newParams)

    }


    private fun buttonvizi(){
        binding.linearButton.visibility = View.GONE
        binding.linearButton2.visibility = View.VISIBLE
        val constraintLayout = findViewById<ConstraintLayout>(R.id.parentLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(
            R.id.button,
            ConstraintSet.TOP,
            R.id.linear_button2,
            ConstraintSet.BOTTOM,
            16
        )

        constraintSet.applyTo(constraintLayout)

    }

    // اکستنشن برای تبدیل dp به px
    fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()


    private fun getDataFavorite(){
        buttonvizi()
        val namF = intent.getStringExtra("emoji_nameF")
        val countF = intent.getIntExtra("emoji_countF",0)
        val emoji = intent.getStringArrayListExtra("emoji")!!
        val price = intent.getIntExtra("emoji_PriceF",0)
        val id = intent.getIntExtra("Id",0)
        ids = id
        emojis = emoji as ArrayList<String>
        binding.text.text = namF
        binding.text2.text = countF.toString()
        binding.btnpol2.text = price.toString()

    }


    private fun getDataFavorite1() {
        val source = intent.getStringExtra("source_type")
        val nameF : String
        val countF : String
        val price : Int
        val takhfif : Int
        val emoji : List<String>
        val id : Int
        when(source){
            "ItemFavorite" ->{
                nameF = intent.getStringExtra("emoji_nameF1").toString()
                countF = intent.getStringExtra("emoji_countF1").toString()
                emoji = intent.getStringArrayListExtra("emoji")!!
                price =  intent.getIntExtra("discountedPrice",0)
                takhfif =  intent.getIntExtra("discountPercentage",0)
                id = intent.getIntExtra("Id",0)
                ids = id
                emojis = emoji as ArrayList<String>
                binding.text.text = nameF
                binding.text2.text = countF
                binding.btnpol.text = price.toString()
                binding.btntakhfif.text = takhfif.toString()
            }

            "ItemFavorite2" ->{
                buttonvizi()
                nameF = intent.getStringExtra("emoji_nameF2").toString()
                countF = intent.getStringExtra("emoji_countF2").toString()
                emoji = intent.getStringArrayListExtra("emoji")!!
                price =  intent.getIntExtra("discountedPrice",0)
                id = intent.getIntExtra("Id",0)
                ids = id
                emojis = emoji as ArrayList<String>
                binding.text.text = nameF
                binding.text2.text = countF
                binding.btnpol2.text = price.toString()

            }

            else -> {
                buttonvizi()
                nameF = intent.getStringExtra("emoji_nameF3").toString()
                countF = intent.getStringExtra("emoji_countF3").toString()
                price =  intent.getIntExtra("discountedPrice",0)
                emoji = intent.getStringArrayListExtra("emoji")!!
                id = intent.getIntExtra("Id",0)
                ids = id
                emojis = emoji as ArrayList<String>
                binding.text.text = nameF
                binding.text2.text = countF
                binding.btnpol2.text = price.toString()
            }
        }
        
    }

    private fun getDataNotifications(){
        val source = intent.getStringExtra("source_type")
        val nameN : String
        val countN : String
        val emoji : List<String>
        when(source){
            "Notification1" ->{
                nameN = intent.getStringExtra("emoji_nameN1").toString()
                countN = intent.getStringExtra("emoji_countN1").toString()
                emoji = intent.getStringArrayListExtra("emoji")!!
                emojis = emoji as ArrayList<String>
                binding.text.text = nameN
                binding.text2.text = countN
            }

            "Notification2" ->{
                nameN = intent.getStringExtra("emoji_nameN2").toString()
                countN = intent.getStringExtra("emoji_countN2").toString()
                emoji = intent.getStringArrayListExtra("emoji")!!
                emojis = emoji as ArrayList<String>
                binding.text.text = nameN
                binding.text2.text = countN
            }

            else -> {
                nameN = intent.getStringExtra("emoji_nameN3").toString()
                countN = intent.getStringExtra("emoji_countN3").toString()
                emoji = intent.getStringArrayListExtra("emoji")!!
                emojis = emoji as ArrayList<String>
                binding.text.text = nameN
                binding.text2.text = countN
            }
        }




    }
}
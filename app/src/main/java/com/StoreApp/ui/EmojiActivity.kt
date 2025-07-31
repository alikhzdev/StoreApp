package com.StoreApp.ui



import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.Adapter.EmojiAdapter
import com.StoreApp.databinding.ActivityEmojiBinding
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel

class EmojiActivity : AppCompatActivity() {
    lateinit var emojis : List<String>
    private lateinit var binding: ActivityEmojiBinding
    private val viewModel: EmojiStoreViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmojiBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)



        binding.iconBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }


        val source = intent.getStringExtra("source")
        when(source){
            "from_Home" -> getDataHome()
        }

        binding.FragWhatsapp.setOnClickListener {
            openApp( "com.whatsapp")
        }
        binding.FragTelegram.setOnClickListener {
            openApp( "org.telegram.messenger")
        }
        binding.FragEtaa.setOnClickListener {
            openApp( "ir.eitaa.messenger")
        }


        val recyclerView: RecyclerView = binding.recyclerViewEmoji
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = gridLayoutManager
        Log.e("emoji","$emojis")
        recyclerView.adapter = EmojiAdapter(emojis)





    }

    fun openApp(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            Log.d("LauncherTest", "App is installed: $packageName")
            startActivity(launchIntent)
        } else {
            Log.d("LauncherTest", "App is NOT installed: $packageName")
            Toast.makeText(this, "برنامه نصب نیست", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataHome(){
        val nameH = intent.getStringExtra("emoji_nameH")
        val countH = intent.getStringExtra("emoji_countH")
        val emoji = intent.getStringArrayListExtra("emoji")
        emojis = emoji!!
        binding.text.text = nameH
        binding.text2.text = countH


    }


    private fun getDataNotifications(){
        val source = intent.getStringExtra("source_type")
        val nameN : String
        val descriptionN : String
        val backgroundN : Int
        val emoji : List<String>
        when(source){
            "Notification1" ->{
                nameN = intent.getStringExtra("emoji_nameN1").toString()
                descriptionN = intent.getStringExtra("emoji_descriptionN1").toString()
                backgroundN = intent.getIntExtra("emoji_backgroundN1",0)
                emoji = intent.getStringArrayListExtra("emoji")!!
                emojis = emoji
                binding.text.text = nameN
                binding.text2.text = descriptionN
                binding.RelativeItemFavoritee.setBackgroundResource(backgroundN)
            }

            "Notification2" ->{
                nameN = intent.getStringExtra("emoji_nameN2").toString()
                descriptionN = intent.getStringExtra("emoji_descriptionN2").toString()
                backgroundN = intent.getIntExtra("emoji_backgroundN2",0)
                binding.text.text = nameN
                binding.text2.text = descriptionN
                binding.RelativeItemFavoritee.setBackgroundResource(backgroundN)
            }

            else -> {
                nameN = intent.getStringExtra("emoji_nameN3").toString()
                descriptionN = intent.getStringExtra("emoji_descriptionN3").toString()
                binding.text.text = nameN
                binding.text2.text = descriptionN
            }
        }




    }


}


package com.StoreApp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.R
import com.StoreApp.ui.ShowEmojiActivity
import info.example.projectnewali.Roomdatabase2.model.PackDisplayData
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel

class AdapterNotification(private val context: Context,private var items: List<PackDisplayData>,private val viewModel: EmojiStoreViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private const val Notification1 = 1
        private const val Notification2 = 2
        private const val Notification3 = 3
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isSpecial == "discounted") {
            Notification1
        } else {
            if (items[position].isSpecial == "No") Notification2 else Notification3
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            Notification1 -> {
                val view = inflater.inflate(R.layout.item_in_notification1, parent, false)
                ViewHolderNotification1(view)
            }
            Notification2 -> {
                val view = inflater.inflate(R.layout.item_in_notification2, parent, false)
                ViewHolderNotification2(view)
            }
            Notification3 -> {
                val view = inflater.inflate(R.layout.item_in_notification3, parent, false)
                ViewHolderNotification3(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }


    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (items[position].isSpecial) {
            "discounted" -> (holder as ViewHolderNotification1).bind(items[position],viewModel)
            "No" -> (holder as ViewHolderNotification2).bind(items[position],viewModel)
            "No discount" -> (holder as ViewHolderNotification3).bind(items[position],viewModel)
        }



        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShowEmojiActivity::class.java)
            intent.putExtra("source", "from_notifications")

            // شناسایی نوع آیتم و ارسال داده‌های مربوطه
            when (items[position].isSpecial) {
                "discounted" -> {
                    val unicodeList = ArrayList<String>(items[position].emojis.map { it.unicode })
                    intent.putExtra("source_type", "Notification1")
                    intent.putExtra("emoji_nameN1", items[position].name) // یا هر فیلد مناسب برای نام
                    intent.putExtra("emoji_countN1", items[position].count)
                    intent.putExtra("emoji", unicodeList)
                }

                "No" -> {
                    val unicodeList = ArrayList<String>(items[position].emojis.map { it.unicode })
                    intent.putExtra("source_type", "Notification2")
                    intent.putExtra("emoji_nameN2", items[position].name) // یا هر فیلد مناسب برای نام
                    intent.putExtra("emoji_countN2", items[position].count)
                    intent.putExtra("emoji", unicodeList)
                }

                "No discount" -> {
                    val unicodeList = ArrayList<String>(items[position].emojis.map { it.unicode })
                    intent.putExtra("source_type", "Notification3")
                    intent.putExtra("emoji_nameN3",items[position].name ) // یا هر فیلد مناسب برای نام
                    intent.putExtra("emoji_countN3", items[position].count)
                    intent.putExtra("emoji", unicodeList)
                }
            }
            context.startActivity(intent)

        }




    }


    fun updateList(newList: List<PackDisplayData>) {
        items = newList
        notifyDataSetChanged()
    }

    class ViewHolderNotification1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(R.id.text1_item_notif3)
        private val text2: TextView = itemView.findViewById(R.id.text2_item_notif3)
        private val button1: Button = itemView.findViewById(R.id.button1_item_notif3)
        private val button2: Button = itemView.findViewById(R.id.button2_item_notif3)
        private val iconDlelte : FrameLayout = itemView.findViewById(R.id.FragDeletenotif3)

        @SuppressLint("SetTextI18n")
        fun bind(item: PackDisplayData,viewModel: EmojiStoreViewModel) {
            text1.text = item.name
            text2.text = item.count
            button1.text =  "${item.discountedPrice?.toString() ?: "N/A"} ت"
            button2.text =  "${item.discountPercentage?.toString() ?: "0"}%"
            iconDlelte.setOnClickListener {
                Log.e("DEBUG", "Delete icon clicked: ${item.id}")
                viewModel.removeFromCart(item.id) }

        }

    }

    class ViewHolderNotification2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(R.id.text1_item_notif2)
        private val text2: TextView = itemView.findViewById(R.id.text2_item_notif2)
        private val button1: Button = itemView.findViewById(R.id.button1_item_notif2)
        private val iconDlelte : FrameLayout = itemView.findViewById(R.id.FragDeletenotif2)


        fun bind(item: PackDisplayData,viewModel: EmojiStoreViewModel) {
            text1.text = item.name
            text2.text = item.count
            iconDlelte.setOnClickListener {
                Log.e("DEBUG", "Delete icon clicked: ${item.id}")
                viewModel.removeFromCart(item.id) }
        }
    }


    class ViewHolderNotification3(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(R.id.text1_item_notif1)
        private val text2: TextView = itemView.findViewById(R.id.text2_item_notif1)
        private val button1: Button = itemView.findViewById(R.id.button_item_notif1)
        private val iconDlelte : FrameLayout = itemView.findViewById(R.id.FragDeleteNotif1)


        @SuppressLint("SetTextI18n")
        fun bind(item: PackDisplayData,viewModel: EmojiStoreViewModel) {
            text1.text = item.name
            text2.text = item.count
            button1.text = "${item.discountedPrice?.toString() ?: "N/A"} ت"
            iconDlelte.setOnClickListener {
                Log.e("DEBUG", "Delete icon clicked: ${item.id}")
                viewModel.removeFromCart(item.id) }

        }
    }


}
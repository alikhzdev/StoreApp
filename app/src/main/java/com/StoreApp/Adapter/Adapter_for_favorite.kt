package com.StoreApp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.model.ClassData_for_favorite
import com.StoreApp.R
import com.StoreApp.ui.ShowEmojiActivity
import info.example.projectnewali.Roomdatabase2.model.PackDisplayData

class HorizontalAdapterr(private val itemList: List<ClassData_for_favorite>) :
    RecyclerView.Adapter<HorizontalAdapterr.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val imageView:ImageView = view.findViewById(R.id.Image_item_favorit2)
        val textView1: TextView = view.findViewById(R.id.text1_item_favorit)
        val textView2: TextView = view.findViewById(R.id.text2_item_favorit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_favorit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        //holder.imageView.setImageResource(item.Image)
        holder.textView1.text = item.test1
        holder.textView2.text = item.test2
    }

    override fun getItemCount() = itemList.size
}


class HorizontalAdapter2(private val context: Context, private var itemList: List<PackDisplayData>) :
    RecyclerView.Adapter<HorizontalAdapter2.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val RelativeLayout :RelativeLayout = view.findViewById(R.id.Relative_item_favorite)
        val textView1: TextView = view.findViewById(R.id.text1_item_favorite)
        val textView2: TextView = view.findViewById(R.id.text2_item_favorite)
        val button: Button = view.findViewById(R.id.button_item_favorite)
        val RecyclerView: RecyclerView = view.findViewById(R.id.RecyclerView_item_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_favorite, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView1.text = item.name
        holder.textView2.text = item.count.toString()
        holder.button.text = "${item.originalPrice?.toString()} ت"
        holder.RecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val emoji = item.emojis.map { it.unicode }
        holder.RecyclerView.adapter = EmojiAdapter(emoji)


        holder.itemView.setOnClickListener {

            val unicodeList = ArrayList<String>(item.emojis.map { it.unicode })
            val intent = Intent(context, ShowEmojiActivity::class.java)
            intent.putExtra("source","From_Favorite_part1")
            intent.putExtra("emoji_nameF", item.name)
            intent.putExtra("emoji_countF", item.count)
            intent.putExtra("emoji_PriceF", item.originalPrice)
            intent.putStringArrayListExtra("emoji", unicodeList)
            context.startActivity(intent)

        }
    }

    override fun getItemCount() = itemList.size

    fun updateList(newList: List<PackDisplayData>) {
        Log.e("MainActivity", "updateList: $newList")
        itemList = newList
        notifyDataSetChanged()
    }
}


class HorizontalAdapter3(private val context: Context,private var items: List<PackDisplayData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private const val favorite = 1
        private const val favorite2 = 2
        private const val favorite3 = 3
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isSpecial == "discounted") {
            favorite
        } else {
            if (items[position].isSpecial == "No") favorite2 else favorite3
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            favorite -> {
                val view = inflater.inflate(R.layout.item_in_favorit, parent, false)
                ViewHolderFavorite(view)
            }

            favorite2 -> {
                val view = inflater.inflate(R.layout.item_in_favorit2, parent, false)
                ViewHolderFavorite2(view)
            }

            favorite3 -> {
                val view = inflater.inflate(R.layout.item_in_favorit3, parent, false)
                ViewHolderFavorite3(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }


    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (items[position].isSpecial) {
            "discounted" -> (holder as ViewHolderFavorite).bind(items[position])
            "No" -> (holder as ViewHolderFavorite2).bind(items[position])
            "No discount" -> (holder as ViewHolderFavorite3).bind(items[position])

        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShowEmojiActivity::class.java)
            intent.putExtra("source", "From_Favorite_part2")

            // شناسایی نوع آیتم و ارسال داده‌های مربوطه
            when (items[position].isSpecial) {
                "discounted" -> {
                    val unicodeList = ArrayList<String>(items[position].emojis.map { it.unicode })
                    intent.putExtra("source_type", "ItemFavorite")
                    intent.putExtra("Id",items[position].id)
                    intent.putExtra("emoji_nameF1", items[position].name) // یا هر فیلد مناسب برای نام
                    intent.putExtra("emoji_countF1", items[position].count)
                    intent.putExtra("discountPercentage",items[position].discountPercentage)
                    intent.putExtra("discountedPrice",items[position].discountedPrice)
                    intent.putStringArrayListExtra("emoji",unicodeList)
                }

                "No" -> {
                    val unicodeList = ArrayList<String>(items[position].emojis.map { it.unicode })
                    intent.putExtra("source_type", "ItemFavorite2")
                    intent.putExtra("Id",items[position].id)
                    intent.putExtra("emoji_nameF2", items[position].name) // یا هر فیلد مناسب برای نام
                    intent.putExtra("emoji_countF2", items[position].count)
                    intent.putExtra("discountedPrice",items[position].discountedPrice)
                    intent.putStringArrayListExtra("emoji",unicodeList)


                }

                "No discount" -> {
                    val unicodeList = ArrayList<String>(items[position].emojis.map { it.unicode })
                    intent.putExtra("source_type", "ItemFavorite3")
                    intent.putExtra("Id",items[position].id)
                    intent.putExtra("emoji_nameF3", items[position].name)  //یا هر فیلد مناسب برای نام
                    intent.putExtra("emoji_countF3", items[position].count)
                    intent.putExtra("discountedPrice",items[position].discountedPrice)
                    intent.putStringArrayListExtra("emoji",unicodeList)
                }
            }
            context.startActivity(intent)
        }




    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<PackDisplayData>) {
        Log.e("MainActivity", "updateList: $newList")
        items = newList
        notifyDataSetChanged()
    }

    class ViewHolderFavorite(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(R.id.text1_item_favorit)
        private val text2: TextView = itemView.findViewById(R.id.text2_item_favorit)
        private val button: Button = itemView.findViewById(R.id.button_item_favorit)
        private val button2: Button = itemView.findViewById(R.id.button2_item_favorit)

        @SuppressLint("SetTextI18n")
        fun bind(item: PackDisplayData) {
            text1.text = item.name
            text2.text = item.count
            button.text = "${item.discountedPrice?.toString() ?: "N/A"} ت"
            button2.text = "${item.discountPercentage?.toString() ?: "0"}%"
        }
    }

    class ViewHolderFavorite2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(R.id.text1_item_favorit2)
        private val text2: TextView = itemView.findViewById(R.id.text2_item_favorit2)
        private val RelativeLayout: RelativeLayout =
            itemView.findViewById(R.id.Relative_item_favorite2)
        private val button: Button = itemView.findViewById(R.id.button1_favorite2)
        private val image: ImageView = itemView.findViewById(R.id.imagePack)


        fun bind(item: PackDisplayData) {
            text1.text = item.name
            text2.text = item.count.toString()
            button.text = item.originalPrice.toString()
            image.setImageResource(R.drawable.ic_emoji)
        }
    }




    class ViewHolderFavorite3(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(R.id.text1_item_favorit3)
        private val text2: TextView = itemView.findViewById(R.id.text2_item_favorit3)
        private val button: Button = itemView.findViewById(R.id.button_item_favorit3)
        private val RelativeLayout: RelativeLayout =
            itemView.findViewById(R.id.Relative_item_favorite3)
        private val image: ImageView = itemView.findViewById(R.id.imagePack)

        @SuppressLint("SetTextI18n")
        fun bind(item: PackDisplayData) {
            text1.text = item.name
            text2.text = item.count.toString()
            button.text = "${item.discountedPrice?.toString() ?: "N/A"} ت"
            image.setImageResource(R.drawable.ic_emoji)
        }

    }
}





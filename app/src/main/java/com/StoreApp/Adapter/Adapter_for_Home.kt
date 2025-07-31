package com.StoreApp.Adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.R
import com.StoreApp.ui.EmojiActivity
import info.example.projectnewali.Roomdatabase2.model.PackDisplayData
import info.example.projectnewali.Roomdatabase2.viewmodel.EmojiStoreViewModel


class VerticalAdapter(private val context: Context, private var itemList: List<PackDisplayData>, val viewModel: EmojiStoreViewModel) :
    RecyclerView.Adapter<VerticalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val RelativeLayout: RelativeLayout = view.findViewById(R.id.Relative_item_home)
        val textView1: TextView = view.findViewById(R.id.text1_item_home)
        val textView2: TextView = view.findViewById(R.id.text2_item_home)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView1.text = item.name
        holder.textView2.text = item.count

        holder.itemView.setOnClickListener {
            val intent = Intent(context,EmojiActivity::class.java)
            val unicodeList = ArrayList<String>(item.emojis.map { it.unicode })
            intent.putExtra("source", "from_Home")
            intent.putExtra("emoji_nameH", item.name)
            intent.putExtra("emoji_countH", item.count)
            intent.putStringArrayListExtra("emoji", unicodeList)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = itemList.size

    fun updateList(newList: List<PackDisplayData>) {
        itemList = newList
        notifyDataSetChanged()
    }

}
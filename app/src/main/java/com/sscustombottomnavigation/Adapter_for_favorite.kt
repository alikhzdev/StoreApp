package com.sscustombottomnavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HorizontalAdapter(private val itemList: List<ClassData_for_favorite>) :
    RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView:ImageView = view.findViewById(R.id.Image_item_favorit)
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
        holder.imageView.setImageResource(item.Image)
        holder.textView1.text = item.test1
        holder.textView2.text = item.test2
    }

    override fun getItemCount() = itemList.size
}



class HorizontalAdapter2(private val itemList: List<ClassData_for_favorite>) :
    RecyclerView.Adapter<HorizontalAdapter2.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView:ImageView = view.findViewById(R.id.Image_item_favorite)
        val textView1: TextView = view.findViewById(R.id.text1_item_favorite)
        val textView2: TextView = view.findViewById(R.id.text2_item_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_in_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(item.Image)
        holder.textView1.text = item.test1
        holder.textView2.text = item.test2
    }

    override fun getItemCount() = itemList.size
}



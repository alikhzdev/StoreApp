package com.StoreApp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.StoreApp.R

class EmojiAdapter(
    private var emojiResList: List<String>
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    inner class EmojiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageEmoji: TextView = itemView.findViewById(R.id.image_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)
        return EmojiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        val resId = emojiResList[position]
        holder.imageEmoji.text = resId
    }

    override fun getItemCount(): Int = emojiResList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newEmojis: List<String>) {
        emojiResList = newEmojis
        notifyDataSetChanged()
    }
}

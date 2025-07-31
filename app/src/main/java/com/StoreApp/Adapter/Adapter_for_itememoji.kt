//package com.sscustombottomnavigation.Adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.sscustombottomnavigation.R
//class ImageAdapter(private val imageList: List<Int>) :
//    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
//
//    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val imageView: ImageView = view.findViewById(R.id.image_item)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_emoji, parent, false)
//        return ImageViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val imagePath = "file:///android_asset/" + imageList[position]
//
//        Glide.with(holder.imageView.context)
//            .load(imagePath)
//            .into(holder.imageView)
//    }
//
//    override fun getItemCount() = imageList.size
//}

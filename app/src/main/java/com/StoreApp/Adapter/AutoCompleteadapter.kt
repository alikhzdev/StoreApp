package com.StoreApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.StoreApp.R
import info.example.projectnewali.Roomdatabase2.model.Category1


class AutocompleteAdapter(
    context: Context,
    resource: Int,
    private val items: List<Category1>
) : ArrayAdapter<Category1>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.autocomplete_item, parent, false)

        val item = getItem(position)
        val textView = view.findViewById<TextView>(R.id.item_text)

        if (item != null) {
            textView.text = item.name // فقط اسم دسته‌بندی
        }

        return view
    }

    override fun getItem(position: Int): Category1? {
        return items.getOrNull(position)
    }

    override fun getCount(): Int {
        return items.size
    }
}
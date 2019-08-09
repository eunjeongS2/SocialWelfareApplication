package com.example.socialwelfareapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.models.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Contact) {
        view.name.text = item.name

        Glide.with(view.context).load(item.image)
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view.image)
    }
}

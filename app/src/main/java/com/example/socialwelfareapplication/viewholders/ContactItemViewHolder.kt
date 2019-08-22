package com.example.socialwelfareapplication.viewholders

import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Contact
import kotlinx.android.synthetic.main.item_contact_select.view.*

class ContactItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Contact) {
        view.name.text = item.name

        Glide.with(view.context).load(item.image)
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view.image)
    }

    fun selectVisitPlace(isSelected: Boolean) {
        view.isSelected = isSelected

        val textColor = if (isSelected) {
            Color.WHITE
        } else {
            ResourcesCompat.getColor(
                view.resources,
                R.color.colorLightGray,
                null
            )
        }
        view.name.setTextColor(textColor)

    }
}

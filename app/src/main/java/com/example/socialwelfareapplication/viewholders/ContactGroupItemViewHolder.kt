package com.example.socialwelfareapplication.viewholders

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import kotlinx.android.synthetic.main.item_contact_group.view.*

class ContactGroupItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

    fun bind(name: String, isSelected: Boolean) {
        view.groupName.text = name
        view.isSelected = isSelected

        when(name){
            "전체" -> {
                view.groupIcon.visibility = View.GONE
            }
            "중요" -> {
                view.groupIcon.setImageResource(R.drawable.ic_star)
            }
            else -> {
                view.groupIcon.setImageResource(R.drawable.ic_group)
            }
        }

        val textColor = if (isSelected) { Color.WHITE } else { Color.BLACK }
        view.groupName.setTextColor(textColor)

    }

}

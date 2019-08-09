package com.example.socialwelfareapplication.viewholders

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import kotlinx.android.synthetic.main.item_contact_group.view.*

class ContactGroupItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

    fun bind(name: String, isSelected: Boolean) {
        view.groupName.text = name

        when(name){
            "전체" -> {
                view.groupIcon.visibility = View.GONE
            }
            "중요" -> {
                view.groupIcon.setImageResource(R.drawable.ic_home)
            }
            else -> {
                view.groupIcon.setImageResource(R.drawable.ic_map)
            }

        }

        if (isSelected) {
            view.changeBackgroundColor(R.color.colorAccent)
            view.groupName.setTextColor(Color.WHITE)
        }
        else {
            view.changeBackgroundColor(R.color.colorWhite)
            view.groupName.setTextColor(Color.BLACK)
        }

    }

    private fun View.changeBackgroundColor(color: Int){
        val gradientDrawable = this.background as GradientDrawable
        gradientDrawable.setColor(ContextCompat.getColor(this.context, color))

    }

}

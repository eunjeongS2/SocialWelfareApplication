package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.viewholders.ContactGroupItemViewHolder

class ContactGroupItemListAdapter : RecyclerView.Adapter<ContactGroupItemViewHolder>() {

    var contactGroupList: List<String> = listOf()
    lateinit var selectGroup : String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactGroupItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_group, parent, false)

        return ContactGroupItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactGroupList.size
    }

    override fun onBindViewHolder(holder: ContactGroupItemViewHolder, position: Int) {
        val groupName = contactGroupList[position]

        holder.itemView.setOnClickListener {
            selectGroup = contactGroupList[position]

            notifyDataSetChanged()
        }

        val isSelected = selectGroup == contactGroupList[position]
        holder.bind(groupName, isSelected)
    }

}

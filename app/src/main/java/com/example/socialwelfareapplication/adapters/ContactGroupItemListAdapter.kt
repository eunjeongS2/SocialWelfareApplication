package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.viewholders.ContactGroupItemViewHolder
import com.example.socialwelfareapplication.viewmodels.UserViewModel

class ContactGroupItemListAdapter(private val viewModel: UserViewModel) : RecyclerView.Adapter<ContactGroupItemViewHolder>() {

    var contactGroupList: List<String> = listOf(
        "전체",
        "중요",
        "어르신",
        "광교복지관"
    )

    var selectGroup : String = contactGroupList[2]


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactGroupItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_group, parent, false)

        viewModel.group = selectGroup

        return ContactGroupItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactGroupList.size
    }

    override fun onBindViewHolder(holder: ContactGroupItemViewHolder, position: Int) {
        val groupName = contactGroupList[position]

        holder.itemView.setOnClickListener {
            selectGroup = contactGroupList[position]
            viewModel.group = selectGroup

            notifyDataSetChanged()
        }

        val isSelected = selectGroup == contactGroupList[position]
        holder.bind(groupName, isSelected)
    }

}

package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.viewholders.ContactItemViewHolder
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.item_contact_select.view.*

class ContactItemListAdapter(private val viewModel: UserViewModel, private val layout: Int) : RecyclerView.Adapter<ContactItemViewHolder>() {

    var contactList: List<Contact> = emptyList()
    var selectVisitPlace = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ContactItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        val item = contactList[position]
        holder.bind(item)

        when(layout) {
            R.layout.item_contact_select -> {

                holder.itemView.checkBox.setOnCheckedChangeListener { _, b ->
                    if (b) { viewModel.selectList.add(item) }
                    else { viewModel.selectList.remove(item) }
                }
            }
            R.layout.item_contact_simple -> {

                holder.itemView.setOnClickListener {
                    selectVisitPlace = position
                    notifyDataSetChanged()
                }

                val isSelected = selectVisitPlace == position
                holder.selectVisitPlace(isSelected)
            }
            R.layout.item_contact -> {

            }
        }
    }


}

package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.viewholders.ContactItemViewHolder

class ContactItemListAdapter : RecyclerView.Adapter<ContactItemViewHolder>() {

    var contactList: List<Contact> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactItemViewHolder(view)        }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        val item = contactList[position]
        holder.bind(item)
    }

}
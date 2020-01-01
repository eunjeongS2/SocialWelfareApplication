package com.example.socialwelfareapplication.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.fragments.ContactDetailFragment
import com.example.socialwelfareapplication.fragments.intent
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.viewholders.ContactItemViewHolder
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.item_contact_select.view.*
import java.util.concurrent.TimeUnit

class ContactItemListAdapter(private val viewModel: UserViewModel, private val layout: Int) :
    RecyclerView.Adapter<ContactItemViewHolder>() {

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

        when (layout) {
            R.layout.item_contact_select -> {
                holder.itemView.checkBox.setOnCheckedChangeListener(null)
                holder.itemView.checkBox.isChecked = viewModel.selectList.contains(item)

                holder.itemView.checkBox.setOnCheckedChangeListener { _, b ->
                    if (b) {
                        viewModel.selectList.add(item)
                    } else {
                        viewModel.selectList.remove(item)
                    }
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

                holder.itemView.clicks()
                    .throttleFirst(600, TimeUnit.MILLISECONDS)
                    .subscribe({
                        val detailFragment = ContactDetailFragment(item, viewModel)
                        if (detailFragment.isAdded) {
                            return@subscribe
                        }

                        val transaction =
                            (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()

                        transaction.add(R.id.fragmentContainer, detailFragment).commit()

                    }, { e ->
                        Log.d(ContactItemListAdapter::class.java.name, "view click failed : ", e)

                    })


                holder.itemView.callButton.setOnClickListener {
                    intent(Intent.ACTION_DIAL, "tel:${item.phoneNumber}", it.context)
                }

                holder.itemView.messageButton.setOnClickListener {
                    intent(Intent.ACTION_SENDTO, "sms:${item.phoneNumber}", it.context)

                }

            }
        }
    }


}

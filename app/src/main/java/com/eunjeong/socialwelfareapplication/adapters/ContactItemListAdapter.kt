package com.eunjeong.socialwelfareapplication.adapters

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.dialog.SendMessageDialog
import com.eunjeong.socialwelfareapplication.fragments.ContactDetailFragment
import com.eunjeong.socialwelfareapplication.fragments.intent
import com.eunjeong.socialwelfareapplication.models.Contact
import com.eunjeong.socialwelfareapplication.viewholders.ContactItemViewHolder
import com.eunjeong.socialwelfareapplication.viewmodels.UserViewModel
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.item_contact_select.view.*
import kotlinx.android.synthetic.main.item_contact_simple.view.*
import java.util.concurrent.TimeUnit

class ContactItemListAdapter(private val viewModel: UserViewModel, private val layout: Int) :
    RecyclerView.Adapter<ContactItemViewHolder>() {

    var contactList: List<Contact> = emptyList()
    var selectVisitPlace = 0
    private var selectLayout = "normal"
        set(value) {
            layoutPublisher.onNext(value)
        }
    var layoutPublisher = PublishSubject.create<String>()

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

                holder.itemView.cancelButton.setOnClickListener {
                    if (viewModel.selectList.size <= 1) {
                        Toast.makeText(holder.itemView.context, "최소 한명은 입력해주세요!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    viewModel.selectList.remove(item)
                    notifyDataSetChanged()
                }
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


//                val dialogListener = DialogInterface.OnClickListener { _, p1 ->
//                    when (p1) {
//                        DialogInterface.BUTTON_POSITIVE -> {
//                            selectLayout = "check"
//                            viewModel.selectList.add(item)
//                        }
//                        DialogInterface.BUTTON_NEGATIVE -> {
//                            intent(Intent.ACTION_SENDTO, "sms:${item.phoneNumber}", holder.itemView.context)
//                        }
//
//                    }
//                }

                holder.itemView.messageButton.setOnClickListener {

                    val messageDialog = SendMessageDialog()

                    if(messageDialog.isAdded) {
                        return@setOnClickListener
                    }

                    messageDialog.sendText = item.name
                    messageDialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, SendMessageDialog.TAG)

//                    val builder = AlertDialog.Builder(holder.itemView.context)
//                    builder.setPositiveButton("단체문자", dialogListener)
//                        .setNegativeButton("보내기", dialogListener).show()

                }


                holder.itemView.callButton.setOnClickListener {
                    intent(Intent.ACTION_DIAL, "tel:${item.phoneNumber}", it.context)
                }


            }
        }
    }


}

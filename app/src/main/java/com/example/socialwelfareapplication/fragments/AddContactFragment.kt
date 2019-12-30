package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_contact.*
import kotlinx.android.synthetic.main.fragment_add_contact.view.*

class AddContactFragment(private val item: Contact?, private val group: String, private val viewModel: UserViewModel) : Fragment() {

    private val disposeBag = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)


        view.backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this).commit()
        }

        viewModel.userPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.progressBar.visibility = View.INVISIBLE

                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this).commit()

            }, { e ->
                Log.d(ContactFragment.TAG, "e : ", e)
            }).addTo(disposeBag)


        view.star.setOnClickListener { it.isSelected = !it.isSelected }

        view.saveButton.setOnClickListener {

            if (nameText.text.isBlank() || phoneEditText.text.isBlank() || emergencyEditText.text.isBlank() || addressEditText.text.isBlank()) {
                Toast.makeText(context, "항목을 모두 채워주세요 !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(item == null) {
                viewModel.addData(
                    Contact(
                        group = group,
                        name = nameText.text.toString(),
                        image = "",
                        phoneNumber = phoneEditText.text.toString(),
                        emergencyNumber = emergencyEditText.text.toString(),
                        remark = remarkText.text.toString(),
                        address = addressEditText.text.toString(),
                        star = star.isSelected

                    )
                )

            } else {
                viewModel.updateDate(
                    item.key,
                    item.group,
                    mapOf(
                        "group" to item.group,
                        "name" to nameText.text.toString(),
                        "image" to item.image,
                        "phoneNumber" to phoneEditText.text.toString(),
                        "emergencyNumber" to emergencyEditText.text.toString(),
                        "remark" to remarkText.text.toString(),
                        "address" to addressEditText.text.toString(),
                        "star" to star.isSelected
                    )
                )
            }

            view.progressBar.visibility = View.VISIBLE
        }


        item?.let {
            with(view) {
                nameText.setText(item.name)
                phoneEditText.setText(item.phoneNumber)
                emergencyEditText.setText(item.emergencyNumber)
                addressEditText.setText(item.address)
                star.isSelected = item.star

                if (item.image == "") {
                    image.setImageResource(R.drawable.ic_contact)
                } else {
                    Glide.with(view.context).load(item.image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(image)
                }
            }
        }

        return view
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

}
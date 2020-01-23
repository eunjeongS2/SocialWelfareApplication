package com.eunjeong.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.adapters.ContactGroupItemListAdapter
import com.eunjeong.socialwelfareapplication.adapters.ContactItemListAdapter
import com.eunjeong.socialwelfareapplication.viewmodels.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_contact.view.*

class AddMonitoringSelectContactFragment : Fragment() {

    private lateinit var groupAdapter: ContactGroupItemListAdapter
    private lateinit var contactAdapter: ContactItemListAdapter

    private lateinit var viewModel: UserViewModel
    private val disposeBag = CompositeDisposable()

    companion object {
        const val REQUEST_CODE = 400
        const val TAG = "AddMonitoringFragment"

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(UserViewModel::class.java)
        }

        groupAdapter = ContactGroupItemListAdapter(viewModel)

        contactAdapter = ContactItemListAdapter(viewModel, R.layout.item_contact_select)

        view?.let {
            setupRecyclerView(it.groupRecyclerView, groupAdapter, RecyclerView.VERTICAL)
            setupRecyclerView(it.contactRecyclerView, contactAdapter, RecyclerView.VERTICAL)
        }

        viewModel.userPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ contactList ->

                view?.let {
                    setupItems(contactAdapter, contactList)
                    val selectGroupText = "${groupAdapter.selectGroup}(${contactAdapter.itemCount})"
                    it.selectGroup.text = selectGroupText
                }

            }, { e ->
                Log.d(TAG, "e : ", e)

            }).addTo(disposeBag)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        view.saveButton.visibility = View.GONE
        view.backButton.setOnClickListener { parentFragmentManager.popBackStackImmediate() }

        view.addButton.setOnClickListener {
            if (viewModel.selectList.size == 0) {
                Toast.makeText(context, "대상자를 선택해주세요 ~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val fragment = AddMonitoringDescriptionFragment(viewModel)
            val transaction = parentFragmentManager.beginTransaction()
            fragment.setTargetFragment(this, REQUEST_CODE)

            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view

    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }
}

package com.eunjeong.socialwelfareapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.adapters.ContactGroupItemListAdapter
import com.eunjeong.socialwelfareapplication.adapters.ContactItemListAdapter
import com.eunjeong.socialwelfareapplication.models.Contact
import com.eunjeong.socialwelfareapplication.viewmodels.UserViewModel
import com.jakewharton.rxbinding3.widget.queryTextChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.backButton
import kotlinx.android.synthetic.main.fragment_contact.view.*
import java.util.concurrent.TimeUnit

class ContactFragment : Fragment() {

    private lateinit var groupAdapter: ContactGroupItemListAdapter
    private lateinit var contactAdapter: ContactItemListAdapter
    private lateinit var checkContactAdapter: ContactItemListAdapter

    private lateinit var viewModel: UserViewModel
    private val disposeBag = CompositeDisposable()
    private val searchDisposeBag = CompositeDisposable()
    private var selectLayout = "normal"

    companion object {
        const val TAG = "ContactFragment"
        const val QUERY_TIMEOUT = 500L

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (selectLayout == "check") {
                    viewModel.layoutPublisher.onNext("normal")
                    viewModel.selectList.clear()
                    view?.selectAllButton?.isChecked = false

                }
            }
        })

        activity?.let {
            viewModel = ViewModelProvider(it).get(UserViewModel::class.java)

            groupAdapter = ContactGroupItemListAdapter(viewModel)
            contactAdapter = ContactItemListAdapter(viewModel, R.layout.item_contact)
            checkContactAdapter = ContactItemListAdapter(viewModel, R.layout.item_contact_select)

            viewModel.layoutPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ layout ->
                    selectLayout = layout
                    view?.let { view ->
                        val adapter: ContactItemListAdapter
                        if (layout == "check") {
                            adapter = checkContactAdapter
                            view.addButton.text = "보내기"
                            view.backButton.visibility = View.VISIBLE
                            view.selectAllText.visibility = View.VISIBLE
                            view.selectAllButton.visibility = View.VISIBLE
                        } else {
                            adapter = contactAdapter
                            view.addButton.text = "추가"
                            view.backButton.visibility = View.GONE
                            view.selectAllButton.visibility = View.GONE
                            view.selectAllText.visibility = View.GONE
                        }
                        setupRecyclerView(view.contactRecyclerView, adapter, RecyclerView.VERTICAL)
                        setupItems(adapter, contactAdapter.contactList)

                    }

                }, { e ->
                    Log.d(TAG, "e : ", e)
                }).addTo(disposeBag)
        }

        view?.let {
            setupRecyclerView(it.contactRecyclerView, contactAdapter, RecyclerView.VERTICAL)
            setupRecyclerView(it.groupRecyclerView, groupAdapter, RecyclerView.VERTICAL)

            it.backButton?.setOnClickListener { _ ->
                viewModel.layoutPublisher.onNext("normal")
                viewModel.selectList.clear()
                it.selectAllButton.isChecked = false

            }

            it.selectAllButton.setOnCheckedChangeListener { _, b ->
                if (b) {
                    viewModel.selectList = contactAdapter.contactList.toMutableList()
                } else {
                    viewModel.selectList.clear()
                }
                checkContactAdapter.notifyDataSetChanged()
            }
        }

        viewModel.userPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ contactList ->
                view?.let {
                    val adapter: ContactItemListAdapter =
                        if (selectLayout == "normal") {
                            contactAdapter
                        } else {
                            checkContactAdapter
                        }
                    setupItems(adapter, contactList)
                    val selectGroupText = "${groupAdapter.selectGroup}(${adapter.itemCount})"
                    it.selectGroup.text = selectGroupText

                    setView(it, groupAdapter.selectGroup)

                    view?.searchView?.setSearch(contactList)
                }

            }, { e ->
                Log.d(TAG, "e : ", e)

            }).addTo(disposeBag)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        view.backButton.visibility = View.GONE
        view.saveButton.visibility = View.GONE
        view.selectAllButton.visibility = View.GONE
        view.selectAllText.visibility = View.GONE

        view.addButton.setOnClickListener {

            when(it.addButton.text) {
                "추가" -> {
                    val contactDetailFragment = AddContactFragment(null, groupAdapter.selectGroup, viewModel)

                    if (contactDetailFragment.isAdded) {
                        return@setOnClickListener
                    }

                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.fragmentContainer, contactDetailFragment).commit()
                }

                "보내기" -> {
                    val numberList = viewModel.selectList.map {contact ->
                        contact.phoneNumber
                    }
                    if (numberList.isNullOrEmpty()) {
                        Toast.makeText(context, "연락처를 선택해주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    intent(Intent.ACTION_SENDTO, "sms:$numberList", it.context)
                }
            }

        }


        return view
    }

    override fun onDestroy() {
        disposeBag.dispose()
        searchDisposeBag.dispose()
        super.onDestroy()
    }

    private fun setView(view: View, group: String) {
        when(selectLayout) {
            "normal" -> if (group == "전체" || group == "중요") {
                view.addButton.visibility = View.INVISIBLE
            } else {
                view.addButton.visibility = View.VISIBLE
            }

            "check" ->
                view.addButton.visibility = View.VISIBLE
        }

    }

    private fun SearchView.setSearch(contactList: List<Contact>) {
        searchDisposeBag.clear()
        this.queryTextChanges().debounce(QUERY_TIMEOUT, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ search ->
                if (search.isBlank()) {
                    contactAdapter.contactList = contactList
                    contactAdapter.notifyDataSetChanged()

                    return@subscribe
                }
                val result = query(contactList, search.toString())

                contactAdapter.contactList = result
                contactAdapter.notifyDataSetChanged()

            }, { e ->
                e.printStackTrace()
            })?.addTo(disposeBag)

    }

    private fun query(contactList: List<Contact>, search: String): List<Contact> {
        return contactList.filter {
            it.name.contains(search) || it.phoneNumber.contains(search) || it.emergencyNumber.contains(search)
        }
    }


}

fun setupItems(adapter: ContactItemListAdapter, itemList: List<Contact>) {
    adapter.contactList = itemList
    adapter.notifyDataSetChanged()
}

fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, layout: Int) {
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, layout, false)

}


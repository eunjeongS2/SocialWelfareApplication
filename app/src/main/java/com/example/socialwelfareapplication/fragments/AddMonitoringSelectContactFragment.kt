package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.ContactGroupItemListAdapter
import com.example.socialwelfareapplication.adapters.ContactItemListAdapter
import com.example.socialwelfareapplication.models.Contact
import kotlinx.android.synthetic.main.fragment_contact.view.*

class AddMonitoringSelectContactFragment : Fragment() {

    private lateinit var groupAdapter: ContactGroupItemListAdapter
    private lateinit var contactAdapter: ContactItemListAdapter

    companion object {
        const val REQUEST_CODE = 400
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val groupList = listOf(
            "전체",
            "중요",
            "어르신",
            "광교복지관"
        )

        val contactList = listOf(
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            ),Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540",
                "010-1234-5678",
                "수원시 영통구 이의동"
            )
        )

        view.groupAddButton.visibility = View.GONE
        view.groupSettingButton.visibility = View.GONE
        view.editButton.visibility = View.GONE

        groupAdapter = ContactGroupItemListAdapter()
        setupRecyclerView(view.groupRecyclerView, groupAdapter)
        groupAdapter.contactGroupList = groupList
        groupAdapter.selectGroup = groupList[2]
        groupAdapter.notifyDataSetChanged()


        contactAdapter = ContactItemListAdapter(R.layout.item_contact_select)
        setupRecyclerView(view.contactRecyclerView, contactAdapter)
        contactAdapter.contactList = contactList
        contactAdapter.notifyDataSetChanged()


        val selectGroupText = "${groupAdapter.selectGroup}(${contactAdapter.itemCount})"
        view.selectGroup.text = selectGroupText

        view.backButton.setOnClickListener { parentFragmentManager.popBackStackImmediate() }

        view.addButton.setOnClickListener {
            val fragment = AddMonitoringDescriptionFragment()
            val transaction = parentFragmentManager.beginTransaction()
            fragment.setTargetFragment(this, REQUEST_CODE)

            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view

    }
}

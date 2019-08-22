package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.ContactItemListAdapter
import com.example.socialwelfareapplication.models.Contact
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.*
import org.threeten.bp.LocalDate

class AddMonitoringDescriptionFragment : Fragment() {

    private lateinit var adapter: ContactItemListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_monitoring_description, container, false)

        val contactList = listOf(
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            ),
            Contact(
                "김한나 어르신",
                "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"
            )
        )

        adapter = ContactItemListAdapter(R.layout.item_contact_select)

        setUpRecyclerView(view.selectContactRecyclerView, adapter)

        adapter.contactList = contactList
        adapter.notifyDataSetChanged()

        view.backButton.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        view.writeButton.setOnClickListener {
            fragmentManager?.popBackStack()

            val fragment = MonitoringFragment()
            val transaction = fragmentManager?.beginTransaction()

            transaction?.replace(R.id.fragmentContainer, fragment)?.commit()
            fragmentManager?.popBackStack()
        }

        val currentDate = LocalDate.now()
        val dateText = "${currentDate.monthValue}/${currentDate.dayOfMonth}/${currentDate.year}"

        view.date.text = dateText

        return view
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView, adapter: ContactItemListAdapter) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    }
}
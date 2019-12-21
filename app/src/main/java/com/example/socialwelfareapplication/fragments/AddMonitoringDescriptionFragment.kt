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

        adapter = ContactItemListAdapter(R.layout.item_contact_simple)

        setupRecyclerView(view.selectContactRecyclerView, adapter, RecyclerView.HORIZONTAL)

        adapter.contactList = contactList
        adapter.notifyDataSetChanged()

        view.backButton.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }

        view.writeButton.setOnClickListener {
            parentFragmentManager.popBackStack()

            val fragment = MonitoringFragment()
            val transaction = parentFragmentManager.beginTransaction()

            transaction.replace(R.id.fragmentContainer, fragment).commit()
            parentFragmentManager.popBackStack()
        }

        val currentDate = LocalDate.now()
        val dateText = "${currentDate.monthValue}/${currentDate.dayOfMonth}/${currentDate.year}"

        view.date.text = dateText

        return view
    }

}

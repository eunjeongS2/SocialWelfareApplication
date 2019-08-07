package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.MonitoringItemListAdapter
import kotlinx.android.synthetic.main.fragment_monitoring.view.*

class MonitoringFragment : Fragment() {

    private lateinit var adapter: MonitoringItemListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_monitoring, container, false)

        val monitoringList = listOf(
            Monitoring("05/20/2019", "도도", "김한다 어르신 댁", "도시락 배달", "부재중", "집에 안계서서 문앞에 두고 감.", 99),
            Monitoring("05/20/2019", "도도", "강한나 어르신 댁", "서프라이즈 파티", "부재중", "집에 안계서서 문앞에 두고 감.", 199),
            Monitoring("05/20/2019", "짱나", "김고추참치 어르신 댁", "도시락 배달", "부재중", "집에 안계서서 문앞에 두고 감.", 929),
            Monitoring("05/19/2019", "짱나", "도참치 어르신 댁", "서프라이즈 파티", "부재중", "집에 안계서서 문앞에 두고 감.", 499),
            Monitoring("05/19/2019", "참치", "고성준 어르신 댁", "도시락 배달", "부재중", "집에 안계서서 문앞에 두고 감.", 299),
            Monitoring("05/19/2019", "참치", "도레미마켓 어르신 댁", "서프라이즈 파티", "부재중", "집에 안계서서 문앞에 두고 감.", 299)

        )

        setupRecyclerView(view.recyclerView, monitoringList)

        view.calendarImageView.setOnClickListener {
            view.monitoringSearchView.clearFocus()
        }

        view.recyclerView.setOnTouchListener { _, _ ->
            if (view.monitoringSearchView.hasFocus())
                view.monitoringSearchView.clearFocus()

            return@setOnTouchListener false
        }


        return view
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, itemList: List<Monitoring>) {
        adapter = MonitoringItemListAdapter()
        recyclerView.adapter = adapter

        adapter.monitoringList = itemList
        adapter.notifyDataSetChanged()

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }
}
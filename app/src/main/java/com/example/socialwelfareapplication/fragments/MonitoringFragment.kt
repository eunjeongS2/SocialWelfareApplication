package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.MonitoringItemListAdapter
import com.example.socialwelfareapplication.viewmodels.MonitoringViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_monitoring.view.*

class MonitoringFragment : Fragment() {

    private lateinit var viewModel: MonitoringViewModel
    private val disposeBag = CompositeDisposable()
    private val monitoringCalendarFragment = MonitoringCalendarFragment()

    companion object {
        const val REQUEST_CODE = 300
        const val TAG = "MonitoringFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(MonitoringViewModel::class.java)
            viewModel.getData()
        }

        val adapter = MonitoringItemListAdapter()
        view?.recyclerView?.adapter = adapter

        if (viewModel.monitoringList.isNotEmpty()) {
            setupItems(adapter, viewModel.monitoringList)
        }

        view?.recyclerView?.let { setupRecyclerView(it, adapter, RecyclerView.VERTICAL) }

        viewModel.monitoringPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ monitoringList ->
                setupItems(adapter, monitoringList)

                monitoringCalendarFragment.dateList = monitoringList.map { it.date }

            }, { e ->
                Log.d(TAG, "e : ", e)

            })
            .addTo(disposeBag)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitoring, container, false)


        view.calendarImageView.setOnClickListener {
            view.monitoringSearchView.clearFocus()

            view.calendarImageView.calendarIsSelected(!view.calendarImageView.isSelected, monitoringCalendarFragment)

        }

        view.recyclerView.setOnTouchListener { _, _ ->
            if (view.monitoringSearchView.hasFocus())
                view.monitoringSearchView.clearFocus()
            else
                view.calendarImageView.calendarIsSelected(false, monitoringCalendarFragment)

            return@setOnTouchListener false
        }


        val addMonitoringFragment = AddMonitoringSelectContactFragment()

        view.addMonitoringButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            addMonitoringFragment.setTargetFragment(this, REQUEST_CODE)

            transaction.replace(R.id.fragmentContainer, addMonitoringFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

        return view
    }

    private fun ImageView.calendarIsSelected(isSelected: Boolean, fragment: Fragment) {
        this.isSelected = isSelected

        val transaction = parentFragmentManager.beginTransaction()
        fragment.setTargetFragment(this@MonitoringFragment, REQUEST_CODE)

        if (isSelected) {
            transaction.add(R.id.fragmentContainer, fragment)

        } else {
            transaction.remove(fragment)
        }

        transaction.commit()

    }

    private fun setupItems(adapter: MonitoringItemListAdapter, itemList: List<Monitoring>) {
        adapter.monitoringList = itemList
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

}
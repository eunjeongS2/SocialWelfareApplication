package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.viewholders.MonitoringItemViewHolder
import com.example.socialwelfareapplication.R

class MonitoringItemListAdapter : RecyclerView.Adapter<MonitoringItemViewHolder>() {

    var monitoringList: List<Monitoring> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonitoringItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_monitoring, parent, false)
        return MonitoringItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return monitoringList.size
    }

    override fun onBindViewHolder(holder: MonitoringItemViewHolder, position: Int) {
        val item = monitoringList[position]
        holder.bind(item)
    }

}
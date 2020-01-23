package com.eunjeong.socialwelfareapplication.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.models.Monitoring
import com.eunjeong.socialwelfareapplication.viewholders.MonitoringItemViewHolder
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.viewmodels.MonitoringViewModel
import kotlinx.android.synthetic.main.item_monitoring.view.*

class MonitoringItemListAdapter(private val viewModel: MonitoringViewModel) :
    RecyclerView.Adapter<MonitoringItemViewHolder>() {

    var monitoringList: MutableList<Monitoring> = mutableListOf()

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

        val dialogListener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.removeData(item.monitoringKey, item.image) {
                        monitoringList.remove(item)
                        notifyDataSetChanged()
//                      viewModel.getData()
                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {

                }

            }
        }

        holder.itemView.removeMonitoring.setOnClickListener {

            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("활동일지 삭제").setPositiveButton("삭제", dialogListener)
                .setNegativeButton("취소", dialogListener).show()

        }
    }

}
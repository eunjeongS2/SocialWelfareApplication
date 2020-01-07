package com.example.socialwelfareapplication.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Monitoring
import kotlinx.android.synthetic.main.dialog_export_csv.view.*

class ExportCsvDialog : DialogFragment() {

    var monitoringList: List<Monitoring> = emptyList()
    var date: String = ""

    companion object {
        const val TAG = "ExportCsvDialog"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_export_csv, container, false)

        view.cancelButton.setOnClickListener {dismiss()}
        view.dateTextView.text = date

        println(monitoringList)

        return view

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}
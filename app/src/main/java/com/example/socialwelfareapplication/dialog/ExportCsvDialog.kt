package com.example.socialwelfareapplication.dialog

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Monitoring
import kotlinx.android.synthetic.main.dialog_export_csv.view.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class ExportCsvDialog : DialogFragment() {

    var monitoringList: List<Monitoring> = emptyList()
    var date: String = ""

    companion object {
        const val TAG = "ExportCsvDialog"
        const val FILE_NAME = "활동일지.csv"
        const val REQUEST_CODE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_export_csv, container, false)

        view.cancelButton.setOnClickListener { dismiss()}
        view.dateTextView.text = date

        view.sendButton.setOnClickListener {
            if (monitoringList.isNullOrEmpty()) {
                Toast.makeText(context, "해당 구간에 활동일지가 없습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (view.emailEditText.text.isBlank()) {
                Toast.makeText(context, "보내실 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createCSV(monitoringList, view.context, view.emailEditText.text.toString())
        }

        return view

    }

    private fun createCSV(list: List<Monitoring>, context: Context, email: String) {

        try {
            context.getExternalFilesDir(null)?.let {
                if(it.canWrite()) {
                    val bw = BufferedWriter(FileWriter("${it.absoluteFile}/$FILE_NAME", false))

                    val csvList = list.map { monitoring ->
                        "${monitoring.place},${monitoring.date},${monitoring.writer},${monitoring.purpose},${monitoring.state},${monitoring.remark}"
                    }

                    bw.write("방문장소,방문날짜,작성자,방문목적,방문상태,특이사항")
                    bw.newLine()
                    csvList.forEach { data ->
                        bw.write(data)
                        bw.newLine()
                    }

                    bw.flush()
                    bw.close()

                    csvEmailIntent(context, email)

                }
            }

        } catch (e: Exception) {
            Log.w(TAG, "Error writing documents.", e)

        }
    }

    private fun csvEmailIntent(context: Context, email: String) {
        context.getExternalFilesDir(null)?.let {
            val path = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(it.absolutePath, FILE_NAME))
            val intent = Intent(Intent.ACTION_SEND)

            intent.type = "vnd.android.cursor.dir/email"
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_STREAM, path)
            startActivityForResult(intent, REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(context, "전송되었습니다", Toast.LENGTH_SHORT).show()

                dismiss()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}










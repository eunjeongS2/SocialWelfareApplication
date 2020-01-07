package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.checkDate
import com.example.socialwelfareapplication.dialog.ExportCsvDialog
import com.example.socialwelfareapplication.viewmodels.MonitoringViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.android.synthetic.main.fragment_monitoring_calendar.view.*
import java.util.*
import kotlin.collections.HashSet

class MonitoringCalendarFragment(private val viewModel: MonitoringViewModel) : Fragment() {

    var dateList: List<String> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitoring_calendar, container, false)

        var filterList: List<String> = emptyList()

        view.calendar.state().edit()
            .setFirstDayOfWeek(Calendar.MONDAY)
            .setMinimumDate(CalendarDay.from(2019, 0, 1))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .isCacheCalendarPositionEnabled(true)
            .commit()


        val set: HashSet<CalendarDay> = hashSetOf()

        dateList.forEach {
            val date = it.split("/")
            set.add(CalendarDay.from(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1, Integer.parseInt(date[2])))
        }

        view.calendar.addDecorator(
            EventDecorator(
                ResourcesCompat.getColor(view.resources, R.color.colorAccent, null), set
            )
        )

        view.rangeMode.setOnCheckedChangeListener { _, b ->
            filterList = emptyList()

            if (b) {
                view.calendar.selectionMode = SELECTION_MODE_RANGE
            } else {
                view.calendar.selectionMode = SELECTION_MODE_SINGLE
            }
        }

        view.calendar.setOnDateChangedListener { _, date, _ ->
            filterList = emptyList()

            if (view.calendar.selectionMode == SELECTION_MODE_SINGLE) {
                val month = (date.month+1).toString().checkDate()
                val day = date.day.toString().checkDate()

                filterList = listOf( "${date.year}/$month/$day")
            }
        }

        view.calendar.setOnRangeSelectedListener { _, dates ->
            filterList = emptyList()

            if (view.calendar.selectionMode == SELECTION_MODE_RANGE) {
                filterList = dates.map {
                    val month = (it.month+1).toString().checkDate()
                    val day = it.day.toString().checkDate()

                    "${it.year}/$month/$day"
                }

            }

        }

        view.exportFileButton.setOnClickListener {
            val exportFileDialog = ExportCsvDialog()

            if(exportFileDialog.isAdded) {
                return@setOnClickListener
            }

            if(filterList.isNullOrEmpty()) {
                if (view.calendar.selectionMode == SELECTION_MODE_SINGLE) {
                    Toast.makeText(context, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "구간을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            val date = "저장할 구간 : ${filterList.first()} - ${filterList.last()}"


            viewModel.filter(filterList) {
                exportFileDialog.date = date
                exportFileDialog.monitoringList = it
                exportFileDialog.show(parentFragmentManager, ExportCsvDialog.TAG)
            }
        }


        view.applyButton.setOnClickListener {
            viewModel.filter(filterList)
        }

        return view
    }



    class EventDecorator(private val color: Int, private val dates: HashSet<CalendarDay>) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(8f, color))
        }

    }
}



package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.socialwelfareapplication.R
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

class MonitoringCalendarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitoring_calendar, container, false)

        view.calendar.state().edit()
            .setFirstDayOfWeek(Calendar.MONDAY)
            .setMinimumDate(CalendarDay.from(2019, 0, 1))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .isCacheCalendarPositionEnabled(true)
            .commit()


        val set: HashSet<CalendarDay> = hashSetOf()
        set.add(CalendarDay.from(2019, 4, 20))
        set.add(CalendarDay.from(2019, 4, 19))
        set.add(CalendarDay.from(2019, 4, 17))
        set.add(CalendarDay.from(2019, 10, 17))

        view.calendar.addDecorator(
            EventDecorator(
                ResourcesCompat.getColor(view.resources, R.color.colorAccent, null), set
            )
        )

        view.rangeMode.setOnCheckedChangeListener { _, b ->

            if (b) {
                view.calendar.selectionMode = SELECTION_MODE_RANGE
            } else {
                view.calendar.selectionMode = SELECTION_MODE_SINGLE
            }
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

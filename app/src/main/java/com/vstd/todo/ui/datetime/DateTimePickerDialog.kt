package com.vstd.todo.ui.datetime

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vstd.todo.databinding.DialogDateTimePickerBinding
import com.vstd.todo.utilities.*
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class DateTimePickerDialog(private val onDateTimeSubmit: (LocalDateTime) -> Unit) :
    DialogFragment() {

    private lateinit var binding: DialogDateTimePickerBinding
    private var dateTime = DateTimeUtils.now()
    private var isTimeNull = true

    // There must be a LocalDateTime param in order to remember the time user have set in case they click on Quick Option after set the date
    private val quickOptions: Map<String, (LocalDateTime) -> LocalDateTime> = mapOf(
        Constants.TONIGHT to DateTimeUtils.tonight,
        Constants.TOMORROW to DateTimeUtils.tomorrow,
        Constants.NEXT_WEEK to DateTimeUtils.nextWeek,
    )

    //TODO: Fix hardcode strings
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {

        binding = DialogDateTimePickerBinding.inflate(layoutInflater)

        loadArgs()
        updateCalendar()
        updateTime()
        setOnClickListeners()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Pick a date and time")
            .setPositiveButton("SET", onPositiveButtonClicked)
            .setNegativeButton("CANCEL", null)
            .create()
    }


    private fun loadArgs() {
        val oldDateTimeString = arguments?.getString(Constants.DATE_TIME_STRING)
        if (oldDateTimeString != null && oldDateTimeString != "") {
            dateTime = oldDateTimeString.toLocalDateTime()
            isTimeNull = false
        }
    }

    private fun updateCalendar() {
        binding.calendarView.date = dateTime.toLong()
    }

    private fun updateTime() {
        binding.btSetDueTime.text = if (!isTimeNull) dateTime.toTimeString() else "Set due time"
    }

    private fun setOnClickListeners() {
        binding.apply {
            btQuickTomorrow.setOnClickListener { onQuickDateClicked(Constants.TOMORROW) }
            btQuickNextWeek.setOnClickListener { onQuickDateClicked(Constants.NEXT_WEEK) }
            btQuickTonight.setOnClickListener { onQuickDateClicked(Constants.TONIGHT) }
            calendarView.setOnDateChangeListener { _, y, m, d -> onCalendarDateChanged(y, m, d) }
            btSetDueTime.setOnClickListener { onSetDueTimeClicked() }
            btSetRepeat.setOnClickListener { onSetRepeatClicked() }
        }
    }

    private val onSetDueTimeClicked = {
        TimePickerDialog(
            requireContext(),
            onTimeSet,
            dateTime.hour,
            dateTime.minute,
            true
        ).show()
    }

    private val onTimeSet = { _: TimePicker, hour: Int, minute: Int ->
        dateTime = dateTime.setTime(hour, minute)
        isTimeNull = false
        updateTime()
    }
    private val onSetRepeatClicked = {
        // TODO: Not yet implemented
    }

    private val onCalendarDateChanged = { year: Int, month: Int, day: Int ->
        dateTime = dateTime.withYear(year).withMonth(month + 1).withDayOfMonth(day)
    }

    private val onQuickDateClicked = { option: String ->
        if (option == Constants.TONIGHT)
            isTimeNull = false
        val newDateTime = quickOptions[option]?.invoke(dateTime)
        if (newDateTime != null) {
            dateTime = newDateTime
            updateCalendar()
            updateTime()
        }

    }

    private val onPositiveButtonClicked = DialogInterface.OnClickListener { _, _ ->
        onDateTimeSubmit(dateTime)
    }

    companion object {
        const val TAG = "DateTimePickerDialog"
    }
}
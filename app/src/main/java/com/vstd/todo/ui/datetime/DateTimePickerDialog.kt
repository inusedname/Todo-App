package com.vstd.todo.ui.datetime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vstd.todo.R
import com.vstd.todo.databinding.DialogDateTimePickerBinding
import com.vstd.todo.utilities.*
import java.time.LocalDate
import java.time.LocalTime

const val NEXT_WEEK = "NEXT_WEEK"
const val TONIGHT = "TONIGHT"
const val TOMORROW = "TOMORROW"
const val CUSTOM = "CUSTOM"

class DateTimePickerDialog(private val onDateTimeSubmit: (String, String) -> Unit) :
    DialogFragment() {

    private lateinit var binding: DialogDateTimePickerBinding

    private var dateSet = false
    private var date = LocalDate.now()
    private var time: LocalTime? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {

        binding = DialogDateTimePickerBinding.inflate(layoutInflater)

        loadArgs()
        updateCalendar()
        updateTime()
        setOnClickListeners()

        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root)
            .setTitle(getString(R.string.pick_a_date_and_time))
            .setPositiveButton(getString(R.string.set), onPositiveButtonClicked)
            .setNegativeButton(getString(R.string.cancel), null)

        if (dateSet) {
            dialog.setNeutralButton(getString(R.string.clear), onNegativeButtonClicked)
        }

        return dialog.create()
    }


    private fun loadArgs() {
        val oldDate = arguments?.getString(Constants.DATE_STRING)
        if (oldDate != null && oldDate != "null") {
            dateSet = true
            date = oldDate.toLocalDate()
        }

        val oldTime = arguments?.getString(Constants.TIME_STRING)
        if (oldTime != null && oldTime != "null") {
            time = oldTime.toLocalTime()
        }
    }

    private fun updateCalendar() {
        binding.calendarView.date = date.toEpochMillisecond()
    }

    private fun updateTime() {
        val timeForTimePicker = if (time == null) getString(R.string.set_time)
        else time!!.toFriendlyString()

        binding.btSetDueTime.text = timeForTimePicker
    }

    private fun setOnClickListeners() {
        binding.apply {
            btQuickTomorrow.setOnClickListener { onQuickDateClicked(TOMORROW) }
            btQuickNextWeek.setOnClickListener { onQuickDateClicked(NEXT_WEEK) }
            btQuickTonight.setOnClickListener { onQuickDateClicked(TONIGHT) }
            btCustomDate.setOnClickListener { onQuickDateClicked(CUSTOM) }
            calendarView.setOnDateChangeListener { _, y, m, d -> onCalendarDateChanged(y, m, d) }
            btSetDueTime.setOnClickListener { onSetDueTimeClicked() }
            btSetRepeat.setOnClickListener { onSetRepeatClicked() }
        }
    }

    private val onSetDueTimeClicked = {
        val timepicker = TimePickerDialog(
            requireContext(),
            onTimeSet,
            time?.hour ?: LocalTime.now().hour,
            time?.minute ?: LocalTime.now().minute,
            true
        )
        if (time != null)
            timepicker.setButton(
                DialogInterface.BUTTON_NEUTRAL,
                getString(R.string.clear)
            ) { _, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) {
                    time = null
                    updateTime()
                }
            }
        timepicker.show()
    }

    private val onTimeSet = { _: TimePicker, hour: Int, minute: Int ->
        time = LocalTime.of(hour, minute)
        updateTime()
    }
    private val onSetRepeatClicked = {
        requireActivity().snackNotAvailable(binding.root)
    }

    private val onCalendarDateChanged = { year: Int, month: Int, day: Int ->
        date = LocalDate.of(year, month + 1, day)
    }

    private val onQuickDateClicked = { option: String ->
        when (option) {
            NEXT_WEEK -> date = DateTimeUtils.nextWeek()
            TOMORROW -> date = DateTimeUtils.tomorrow()
            TONIGHT -> {
                date = LocalDate.now()
                time = DateTimeUtils.tonight()
            }
            CUSTOM -> showDatePicker()
        }
        updateCalendar()
        updateTime()
    }

    private val onPositiveButtonClicked = DialogInterface.OnClickListener { _, _ ->
        onDateTimeSubmit(date.toString(), time?.toString() ?: "null")
    }

    private val onNegativeButtonClicked = DialogInterface.OnClickListener { _, _ ->
        onDateTimeSubmit("null", "null")
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireActivity(),
            { _, year, month, day ->
                date = LocalDate.of(year, month, day)
                updateCalendar()
            },
            date.year,
            date.monthValue,
            date.dayOfMonth
        ).show()
    }

    companion object {
        const val TAG = "DateTimePickerDialog"
    }
}
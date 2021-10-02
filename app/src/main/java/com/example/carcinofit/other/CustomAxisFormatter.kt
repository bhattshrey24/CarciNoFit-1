package com.example.carcinofit.other

import com.example.carcinofit.database.models.ChartStats
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class CustomAxisFormatter(val calories:List<ChartStats>):IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val date=calories[value.toInt()].relativeDate
        val dateFormat=SimpleDateFormat("dd MMM ''yy", Locale.getDefault())
        return dateFormat.format(date)
    }
}
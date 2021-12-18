package data

import java.time.LocalDate
import java.util.*

data class TimeEntry(val startTime: Calendar, val endTime: Calendar, val breakDuration: Short, val date: LocalDate) {
    override fun toString(): String {
        return "${startTime.get(Calendar.HOUR_OF_DAY)}: ${startTime.get(Calendar.MINUTE)} - ${endTime.get(Calendar.HOUR_OF_DAY)}: ${endTime.get(Calendar.MINUTE)} | $breakDuration $date"
    }
}

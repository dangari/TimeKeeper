package data

import java.time.LocalDate
import java.util.*

data class TimeEntry(val startTime: Time, val endTime: Time, val breakDuration: Short, val date: LocalDate) : Comparable<TimeEntry> {
    override fun toString(): String {
        return "$date $startTime-$endTime: $endTime | $breakDuration"
    }

    override fun compareTo(other: TimeEntry): Int {
        if (date > other.date) return 1
        if (date < other.date) return -1
        if (startTime > other.startTime) return 1
        if (startTime < other.startTime) return -1
        if (endTime > other.endTime) return 1
        if (endTime < other.endTime) return -1
        if (breakDuration > other.breakDuration) return 1
        if (breakDuration < other.breakDuration) return 1

        return 0
    }
}

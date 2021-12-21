package data

import java.time.LocalDate

data class TimeEntry(
    val startTime: Time,
    val endTime: Time,
    val breakDuration: Short,
    val date: LocalDate,
    val type: TimeEntryType = TimeEntryType.WORKING_DAY,
    val description: String = ""
) : Comparable<TimeEntry> {
    override fun toString(): String {
        return "$date $startTime-$endTime: $endTime | $breakDuration"
    }

    constructor(date:LocalDate): this(Time(0,0), Time(0,0), 0, date, TimeEntryType.VACATION)
    constructor(date:LocalDate, description: String): this(Time(0,0), Time(0,0), 0, date, TimeEntryType.VACATION, description)

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

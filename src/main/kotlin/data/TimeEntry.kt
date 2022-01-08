package data

import kotlinx.serialization.Serializable
import serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class TimeEntry(
    val startTime: Time,
    val endTime: Time,
    val breakDuration: Short,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val type: TimeEntryType = TimeEntryType.WORKING_DAY,
    val description: String = "",
    val workingTime: Time =  Time((endTime - startTime).inMinutes() - breakDuration),
    val overTime: Time = if (type == TimeEntryType.WORKING_DAY) workingTime - Config().workingHours else Time(0,0)
) : Comparable<TimeEntry> {

    init {
        require(startTime <= endTime)
    }

    constructor(date:LocalDate): this(Time(0,0), Time(0,0), 0, date, TimeEntryType.VACATION)
    constructor(date:LocalDate, description: String): this(Time(0,0), Time(0,0), 0, date, TimeEntryType.HOLIDAY, description)

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

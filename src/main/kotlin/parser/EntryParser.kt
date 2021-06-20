package parser

import data.TimeEntry
import java.util.*

class EntryParser {
    private val regex = """(?<startTime>((\d)|([01]\d)|(2\d)):([012345]\d))\s*-\s*(?<endTime>((\d)|([01]\d)|(2\d)):([012345]\d))(/(?<breakTime>\d+))?""".toRegex();

    fun getTime(text: String): TimeEntry {
        val result = regex.matchEntire(text) ?: throw java.lang.RuntimeException("Invalid Input")
        val groups = result.groups as? MatchNamedGroupCollection ?: throw RuntimeException("no named groups")
        val startTime = createCalendarFromTime(groups["startTime"]!!.value)
        val endTime = createCalendarFromTime(groups["endTime"]!!.value)
        val breakTime = groups["breakTime"]!!.value.toShort()
        return TimeEntry(startTime, endTime, breakTime)
    }

    private fun createCalendarFromTime(time: String): Calendar {
        val result = """(\d{2}):(\d{2})""".toRegex().matchEntire(time)
        val hours = result!!.groups[1]!!.value.toInt()
        val minutes = result.groups[2]!!.value.toInt()
        return Calendar.Builder().setTimeOfDay(hours, minutes, 0).build()
    }
}

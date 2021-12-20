package utils

import data.DateKey
import data.Time
import data.TimeEntry
import data.TimeEntryType
import tornadofx.SortedFilteredList
import java.io.File
import java.io.UnsupportedEncodingException
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.stream.Collectors

class AtWorkExportReader {
    fun readFileLineByLineUsingForEachLine(fileName: String): HashMap<DateKey, SortedFilteredList<TimeEntry>> {
        val timeEntries = HashMap<DateKey, SortedFilteredList<TimeEntry>>()
        File(fileName).forEachLine {
            val line = it.split(";")
            val dateRegex = """\d{4}-\d{2}-\d{2}""".toRegex() //2020-03-13
            if (line.size > 3) {
                if (line[1] == "Vacation") {
                    val entries = createVacationDays(line)
                    timeEntries.addTimeEntries(entries)
                }
                if (dateRegex.containsMatchIn(line[1])) {
                    val entry = createWorkingDay(line)
                    timeEntries.addTimeEntry(entry)
                }
            }
        }

        return timeEntries
    }


    // 402;2020-03-11 07:20;2020-03-11 16:10;00:45;08:05;"0
    private fun createWorkingDay(line: List<String>): TimeEntry {
        val regex = """(\d{4}-\d{2}-\d{2}) ((\d{2}):(\d{2}))""".toRegex()
        val startTimeText = line[1]
        val endTimeText = line[2]
        val breakDurationText = line[3]

        val startTimeMatch = regex.find(startTimeText)
        val endTimeMatch = regex.find(endTimeText)
        if (startTimeMatch == null || endTimeMatch == null) {
            throw UnsupportedEncodingException("Wrong Format of File")
        }

        val localDate = LocalDate.parse(startTimeMatch.groupValues[1])
        val startTime = createTimeFromMatch(startTimeMatch)
        val endTime = createTimeFromMatch(endTimeMatch)
        val breakDuration = calcBreakDuration(breakDurationText)

        return TimeEntry(startTime, endTime, breakDuration, localDate)
    }

    // Vacation;2021-05-17;2021-05-21;5;"0
    private fun createVacationDays(line: List<String>): List<TimeEntry> {
        val startDate = LocalDate.parse(line[2])
        val endDate = LocalDate.parse(line[3])

        val vacationDays = startDate.datesUntil(endDate).collect(Collectors.toList())
        if (vacationDays.isEmpty()) {
            vacationDays.add(startDate)
        }
        val entries = ArrayList<TimeEntry>()
        val time = Time(0, 0)
        vacationDays.filter{it.dayOfWeek != null && it.dayOfWeek != DayOfWeek.SATURDAY}
            .forEach{entries.add(TimeEntry(time, time, 0, it, TimeEntryType.VACATION))}
        return entries
    }

    private fun createTimeFromMatch(match: MatchResult): Time {
        return Time(match.groupValues[3].toInt(), match.groupValues[4].toInt())
    }

    private fun calcBreakDuration(breakDurationText: String): Short {
        val regex = """(\d{2}):(\d{2})""".toRegex()
        val match = regex.find(breakDurationText) ?: throw UnsupportedEncodingException("Wrong Format of File")

        val hours = match.groupValues[1].toShort()
        val minutes = match.groupValues[2].toShort()

        return (hours * 60 + minutes).toShort()
    }

    private fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addTimeEntry(entry: TimeEntry) {
        val dateKey = DateKey(entry.date.year, entry.date.monthValue)
        if (!this.containsKey(dateKey)) {
            this[dateKey] = SortedFilteredList()
        }

        this[dateKey]?.add(entry)
    }

    private fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addTimeEntries(entries: List<TimeEntry>) {
        entries.forEach { this.addTimeEntry(it) }
    }

}

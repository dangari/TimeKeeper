package utils

import data.Time
import data.TimeEntry
import data.TimeEntryType
import java.io.File
import java.io.UnsupportedEncodingException
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.stream.Collectors

class AtWorkImporter {
    enum class Block{
        WORKING,
        VACATION,
        SICKNESS;

        fun determineBlock(value: String): Block {
            if (value == "Entries") return WORKING
            if (value == "Vacation Days") return VACATION
            if (value == "Sick Days") return SICKNESS
            return this
        }
    }
    fun import(fileName: String): List<TimeEntry> {
        val timeEntries = ArrayList<TimeEntry>()
        val path = System.getProperty("user.dir")
        val file = File("$path/resources/$fileName")
        var block = Block.WORKING
        file.forEachLine {
            val line = it.split(";")
            val dateRegex = """\d{4}-\d{2}-\d{2}""".toRegex() //2020-03-13
            block = block.determineBlock(line[0])
            if (line.size > 3) {
                if ((block == Block.SICKNESS || block == Block.VACATION) && line[1] == "No Name") {
                    val entries = createVacationDays(line, block)
                    timeEntries.addAll(entries)
                }
                if (dateRegex.containsMatchIn(line[1])) {
                    val entry = createWorkingDay(line)
                    timeEntries.add(entry)
                }
            }
        }

        return mergeEntries(timeEntries)
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
    private fun createVacationDays(line: List<String>, block: Block): List<TimeEntry> {
        var startDate = LocalDate.parse(line[2])
        var endDate = LocalDate.parse(line[3])

        if (startDate == endDate && (startDate.dayOfWeek == DayOfWeek.SATURDAY || startDate.dayOfWeek == DayOfWeek.SUNDAY)) {
            return emptyList()
        }
        if (startDate != endDate) {
            startDate = parseStartDate(startDate)
            endDate = parseEndDate(endDate)
        }

        val vacationDays = startDate.datesUntil(endDate).collect(Collectors.toList())
        if (vacationDays.isEmpty()) {
            vacationDays.add(startDate)
        }
        val entries = ArrayList<TimeEntry>()
        val type = if (block == Block.SICKNESS) TimeEntryType.SICKNESS else TimeEntryType.VACATION
        vacationDays.filter{it.dayOfWeek != null && it.dayOfWeek != DayOfWeek.SATURDAY}
            .forEach{entries.add(TimeEntry(it, type))}
        return entries
    }

    private fun createTimeFromMatch(match: MatchResult): Time {
        return Time(match.groupValues[3].toInt(), match.groupValues[4].toInt())
    }

    private fun calcBreakDuration(breakDurationText: String): Short {
        if(breakDurationText.isEmpty()) return 0
        val regex = """(\d{2}):(\d{2})""".toRegex()
        println(breakDurationText)
        val match = regex.find(breakDurationText) ?: throw UnsupportedEncodingException("Wrong Format of File: $breakDurationText")

        val hours = match.groupValues[1].toShort()
        val minutes = match.groupValues[2].toShort()

        return (hours * 60 + minutes).toShort()
    }

    private fun mergeEntries(entries: List<TimeEntry>): List<TimeEntry>{
        val tempSave: HashMap<LocalDate, TimeEntry> = HashMap()
        entries.forEach { entry ->
            if (tempSave.containsKey(entry.date)) {
                val mergedEntry = tempSave[entry.date]!!.merge(entry)
                tempSave[entry.date] = mergedEntry
            }
            else {
                tempSave[entry.date] = entry
            }
        }

        return tempSave.values.toList()
    }

    fun TimeEntry.merge(entry: TimeEntry): TimeEntry {
        val startTime = if (this.startTime > entry.startTime)  entry.startTime else this.startTime
        val endTime = if  (this.endTime > entry.endTime) this.endTime else entry.endTime
        val workingTime = this.workingTime + entry.workingTime
        val breakDuration = endTime - startTime - workingTime

        return TimeEntry(startTime, endTime, breakDuration.inMinutes().toShort(), entry.date, this.type, this.description)

    }

    private fun parseEndDate(date: LocalDate): LocalDate {
        if (date.dayOfWeek == DayOfWeek.SUNDAY) {
            return date.minusDays(2)
        }
        if (date.dayOfWeek == DayOfWeek.SATURDAY) {
            return date.minusDays(1)
        }

        return date;
    }

    private fun parseStartDate(date: LocalDate): LocalDate {
        if (date.dayOfWeek == DayOfWeek.SUNDAY) {
            return date.plusDays(1)
        }
        if (date.dayOfWeek == DayOfWeek.SATURDAY) {
            return date.plusDays(2)
        }

        return date;
    }
}

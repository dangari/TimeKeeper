package utils

import data.DateKey
import data.TimeEntry
import data.TimeEntryType
import javafx.collections.transformation.SortedList
import tornadofx.SortedFilteredList
import tornadofx.toObservable
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar


fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addTimeEntry(entry: TimeEntry) {
    val dateKey = DateKey(entry.date.year, entry.date.monthValue)
    if (!this.containsKey(dateKey)) {
        this[dateKey] = SortedFilteredList()
    }

    this[dateKey]?.add(entry)
}

fun  HashMap<DateKey, SortedFilteredList<TimeEntry>>.addEmptyEntry(dateKey: DateKey) {
    if (this.containsKey(dateKey)) return
    this[dateKey] = SortedFilteredList()
}

fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addTimeEntries(entries: List<TimeEntry>) {
    entries.forEach { this.addTimeEntry(it) }
}

fun  HashMap<DateKey, SortedFilteredList<TimeEntry>>.getAllMonths(): List<Int> {
    return this.keys.map { key -> key.month }.distinct()
}

fun  HashMap<DateKey, SortedFilteredList<TimeEntry>>.getAllYears(): List<Int> {
    return this.keys.map { key -> key.year }.distinct()
}

fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.convertSortedList(): HashMap<DateKey, List<TimeEntry>> {
    val convertedHashMap = HashMap<DateKey, List<TimeEntry>>()

    this.keys.forEach { key -> convertedHashMap[key] = this[key]?.items?.toList()!! }
    return convertedHashMap
}

fun HashMap<DateKey, List<TimeEntry>>.convertList(): HashMap<DateKey, SortedFilteredList<TimeEntry>> {
    val convertedHashMap = HashMap<DateKey, SortedFilteredList<TimeEntry>>()

    this.keys.forEach { key ->
        val list = SortedFilteredList<TimeEntry>()
        list.addAll(this[key]!!)
        convertedHashMap[key] = list
    }
    return convertedHashMap
}

fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addMissingDays() {
    val today = LocalDate.now()
    val startDateKey = this.findOldestDate()
    var startDate = LocalDate.now()
    this[startDateKey]!!.forEach{ entry -> if(entry.date.isBefore(startDate)) startDate = entry.date}
    val allDates = startDate.datesUntil(today).filter{date -> date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY}
    val datesMapped = HashMap<DateKey, MutableList<LocalDate>>()
    allDates.forEach { date ->
        val key = DateKey(date)
        if (!datesMapped.containsKey(key)) {
            datesMapped[key] = mutableListOf()
        }
        val value = datesMapped[key]
        value!!.add(date)
    }
    datesMapped.forEach { (key, value) ->
        val entries = this[key]
        val newList: MutableList<TimeEntry> = entries?.toMutableList() ?: ArrayList()
        value.forEach {date ->
            if (entries != null && !entries.entryWithDateExists(date)) {
                newList.add(TimeEntry(date, TimeEntryType.WORKING_DAY))
            }
        }
        this[key] = SortedFilteredList(newList.toObservable())
    }
}

fun List<TimeEntry>.entryWithDateExists(date: LocalDate): Boolean {
    this.forEach{entry -> if (entry.date == date) return true}
    return false
}

fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.findOldestDate(): DateKey {
    val current = LocalDate.now()
    var oldestDate = DateKey(current.year, current.monthValue)
    this.keys.forEach{date ->
        val value = this[date]
        if (date < oldestDate && value!!.any { entry -> entry.type == TimeEntryType.WORKING_DAY }) oldestDate = date}
    return oldestDate
}

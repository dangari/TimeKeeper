package utils

import data.DateKey
import data.TimeEntry
import javafx.collections.transformation.SortedList
import tornadofx.SortedFilteredList


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

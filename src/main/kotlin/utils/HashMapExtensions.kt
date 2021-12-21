package utils

import data.DateKey
import data.TimeEntry
import tornadofx.SortedFilteredList


fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addTimeEntry(entry: TimeEntry) {
    val dateKey = DateKey(entry.date.year, entry.date.monthValue)
    if (!this.containsKey(dateKey)) {
        this[dateKey] = SortedFilteredList()
    }

    this[dateKey]?.add(entry)
}

fun HashMap<DateKey, SortedFilteredList<TimeEntry>>.addTimeEntries(entries: List<TimeEntry>) {
    entries.forEach { this.addTimeEntry(it) }
}


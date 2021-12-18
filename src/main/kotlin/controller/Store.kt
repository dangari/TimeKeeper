package controller

import data.TimeEntry
import parser.EntryParser
import tornadofx.Controller
import tornadofx.SortedFilteredList
import java.time.LocalDate

class Store : Controller() {
    val timeEntries = SortedFilteredList<TimeEntry> ()

    fun addTimeEntry(entry: String, date: LocalDate) = timeEntries.add(EntryParser().getTime(entry, date))
}

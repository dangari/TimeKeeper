package controller

import data.DateKey
import data.TimeEntry
import parser.EntryParser
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.asObservable
import java.time.LocalDate

class Store : Controller() {
    val timeEntries = mutableMapOf<DateKey, SortedFilteredList<TimeEntry>>().asObservable()
    val months = SortedFilteredList<Int>()
    val years = SortedFilteredList<Int>()

    init {
        val date = LocalDate.now()
        val dateKey = DateKey(date.year, date.month.value)
        timeEntries[dateKey] = SortedFilteredList()
        months.add(date.monthValue)
        years.add(date.year)

        val testString1 = "11:00-15:00/60"
        val testString2 = "12:00-15:00/60"
        val entry1 = EntryParser().getTime(testString1, LocalDate.now())
        val entry2 = EntryParser().getTime(testString2, LocalDate.now().minusMonths(2))
        val key1 = DateKey(2021, 12)
        val key2 = DateKey(2021, 10)
        timeEntries[key1] = SortedFilteredList()
        timeEntries[key1]?.add(entry1)
        timeEntries[key2] = SortedFilteredList()
        timeEntries[key2]?.add(entry2)
        months.add(entry2.date.monthValue)
    }

    fun addTimeEntry(entry: String, date: LocalDate) {
        val dateKey = DateKey(date.year, date.monthValue)
        val timeEntry = EntryParser().getTime(entry, date)
        if (!timeEntries.containsKey(dateKey)) {
            timeEntries[dateKey] = SortedFilteredList()
        }
        timeEntries[dateKey]?.add(timeEntry)


        if (!months.contains(dateKey.month)) {
            months.add(dateKey.month)
        }
        if (!years.contains(dateKey.year)) {
            years.add(dateKey.year)
        }
    }

}

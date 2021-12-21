package controller

import data.DateKey
import data.TimeEntry
import utils.EntryParser
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.asObservable
import utils.AtWorkImporter
import utils.HolidayImporter
import utils.addTimeEntries
import java.time.LocalDate

class Store : Controller() {
    val timeEntries = HashMap<DateKey, SortedFilteredList<TimeEntry>>()
    val months = SortedFilteredList<Int>()
    val years = SortedFilteredList<Int>()

    init {
        val date = LocalDate.now()
        val dateKey = DateKey(date.year, date.month.value)
        timeEntries[dateKey] = SortedFilteredList()
        months.add(date.monthValue)
        years.add(date.year)

        val importedTimeEntries = AtWorkImporter().import("F:\\Projects\\TimeKeeper\\src\\test\\kotlin\\utils\\times.csv")
        val holidays = HolidayImporter().importHolidays("F:\\Projects\\TimeKeeper\\src\\recources\\holidays.json")
        timeEntries.addTimeEntries(holidays)
        timeEntries.addTimeEntries(importedTimeEntries)
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

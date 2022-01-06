package controller

import data.DateKey
import data.TimeEntry
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.asObservable
import tornadofx.sort
import utils.*
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

        months.replaceAll(timeEntries.getAllMonths())
        years.replaceAll(timeEntries.getAllYears())
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

    private fun SortedFilteredList<Int>.replaceAll(newEntries: List<Int>) {
        this.clear()
        this.addAll(newEntries.sorted())
        this.sort()
    }

}

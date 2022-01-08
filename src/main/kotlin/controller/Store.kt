package controller

import data.DateKey
import data.TimeEntry
import tornadofx.Controller
import tornadofx.SortedFilteredList
import tornadofx.sort
import utils.*
import java.time.LocalDate

class Store : Controller() {
    //val timeEntries = HashMap<DateKey, SortedFilteredList<TimeEntry>>()
    val timeEntries = DataHandler().loadEntriesFromFile()
    val months = SortedFilteredList<Int>()
    val years = SortedFilteredList<Int>()

    init {
        val date = LocalDate.now()
        val dateKey = DateKey(date.year, date.month.value)
        //timeEntries[dateKey] = SortedFilteredList()
        months.add(date.monthValue)
        years.add(date.year)

        //val importedTimeEntries = AtWorkImporter().import("F:\\Projects\\TimeKeeper\\resources\\times.csv")
        //val holidays = HolidayImporter().importHolidays("holidays.json")
        //timeEntries.addTimeEntries(holidays)
        //timeEntries.addTimeEntries(importedTimeEntries)

        months.replaceAll(timeEntries.getAllMonths())
        years.replaceAll(timeEntries.getAllYears())
        //DataHandler().saveEntriesToFile(timeEntries)
    }

    fun addTimeEntry(timeEntry: TimeEntry, date: LocalDate) {
        val dateKey = DateKey(date.year, date.monthValue)

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

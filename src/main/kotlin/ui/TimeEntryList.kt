package ui

import controller.Store
import data.DateKey
import data.TimeEntry
import events.AddTimeEntryEvent
import events.SelectedListChangedEvent
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import utils.addEmptyEntry
import utils.getOverTimeOfMonth
import utils.getTotalWorkingHours
import utils.getVacationDays
import java.time.LocalDate
import java.time.Month
import java.time.Year

class TimeEntryList: View() {
    private val store: Store by inject()
    private val selectedMonth = SimpleIntegerProperty(LocalDate.now().month.value)
    private val selectedYear = SimpleIntegerProperty(LocalDate.now().year)
    private var overTime = SimpleStringProperty("")
    private var workingHours = SimpleStringProperty("")
    private var vacationDays = SimpleStringProperty("")

    override val root = vbox {
        hbox {
            combobox(selectedYear, store.years.sortedItems)
            combobox(selectedMonth, store.months.sortedItems)
        }
        vbox {
           label(overTime)
           label(workingHours)
           label(vacationDays)
            subscribe<SelectedListChangedEvent> { event -> updateMonthlyInfo(event.year, event.month) }
            subscribe<AddTimeEntryEvent> { event -> updateMonthlyInfo(event.year, event.month) }
        }
        vbox {
            tableview(store.timeEntries[DateKey(selectedYear.value, selectedMonth.value)]) {
                isEditable = true
                readonlyColumn("Date", TimeEntry::date)
                readonlyColumn("Start Time ", TimeEntry::startTime)
                readonlyColumn("End Time", TimeEntry::endTime)
                readonlyColumn("Break", TimeEntry::breakDuration)
                readonlyColumn("Working Time", TimeEntry::workingTime)
                readonlyColumn("Overtime", TimeEntry::overTime)
                subscribe<SelectedListChangedEvent> { event ->
                    val dateKey = DateKey(event.year, event.month)
                    if (!store.timeEntries.containsKey(dateKey)) {
                        store.timeEntries.addEmptyEntry(dateKey)
                    }
                    items = store.timeEntries[dateKey]
                }
            }
        }
    }

    init {
        updateMonthlyInfo(selectedYear.value, selectedMonth.value)
        selectedYear.onChange {
            fire(SelectedListChangedEvent(it, selectedMonth.value))
        }

        selectedMonth.onChange {
            fire(SelectedListChangedEvent(selectedYear.value, it))
        }
    }

    private fun updateMonthlyInfo(year: Int, month: Int) {
        val dateKey = DateKey(year, month)
        if (!store.timeEntries.containsKey(dateKey)) {
            store.timeEntries.addEmptyEntry(dateKey)
        }
        val entries = store.timeEntries[dateKey]
        overTime.value = entries?.getOverTimeOfMonth().toString()
        vacationDays.value = entries?.getVacationDays().toString()
        workingHours.value = entries?.getTotalWorkingHours().toString()
    }
}

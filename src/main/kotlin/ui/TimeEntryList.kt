package ui

import controller.Store
import data.DateKey
import data.TimeEntry
import events.AddTimeEntryEvent
import events.SelectedListChangedEvent
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import utils.*
import java.time.LocalDate

class TimeEntryList: View() {
    private val store: Store by inject()
    private val selectedMonth = SimpleIntegerProperty(LocalDate.now().month.value)
    private val selectedYear = SimpleIntegerProperty(LocalDate.now().year)
    private var monthCalculatedTime = SimpleStringProperty("")
    private var totalCalculatedTime = SimpleStringProperty("")
    private var yearCalculatedTime = SimpleStringProperty("")
    private val timeCalculator = TimeCalculator()

    override val root = vbox {
        hbox {
            combobox(selectedYear, store.years.sortedItems)
            combobox(selectedMonth, store.months.sortedItems)
        }
        vbox {
           hbox {
               label("Month: ")
               label(monthCalculatedTime)
           }
            hbox {
                label("Year: ")
                label(yearCalculatedTime)
            }
            hbox {
                label("Total: ")
                label(totalCalculatedTime)
            }
            subscribe<SelectedListChangedEvent> { event -> updateMonthlyInfo(event.year, event.month) }
            subscribe<AddTimeEntryEvent> { event -> updateMonthlyInfo(event.year, event.month) }
        }
        vbox {
            tableview(store.timeEntries[DateKey(selectedYear.value, selectedMonth.value)]?.sortedItems) {
                isEditable = true
                readonlyColumn("Date", TimeEntry::date)
                readonlyColumn("Start Time ", TimeEntry::startTime)
                readonlyColumn("End Time", TimeEntry::endTime)
                readonlyColumn("Break", TimeEntry::breakDuration)
                readonlyColumn("Working Time", TimeEntry::workingTime)
                readonlyColumn("Overtime", TimeEntry::overTime)
                readonlyColumn("Type", TimeEntry::type)
                readonlyColumn("Description", TimeEntry::description)
                subscribe<SelectedListChangedEvent> { event ->
                    val dateKey = DateKey(event.year, event.month)
                    if (!store.timeEntries.containsKey(dateKey)) {
                        store.timeEntries.addEmptyEntry(dateKey)
                    }
                    items = store.timeEntries[dateKey]?.sortedItems
                }
            }
        }
    }

    init {
        timeCalculator.init(store.timeEntries)
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
        monthCalculatedTime.value = timeCalculator.getMonthlyCalculatedTimes(dateKey, entries!!).toString()
        yearCalculatedTime.value = timeCalculator.yearTimeCalculation(year).toString()
        totalCalculatedTime.value = timeCalculator.totalTimeCalculation().toString()
    }

}

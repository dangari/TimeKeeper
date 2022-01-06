package ui

import controller.Store
import data.DateKey
import data.TimeEntry
import events.SelectedListChangedEvent
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*
import utils.addEmptyEntry
import java.time.LocalDate

class TimeEntryList: View() {
    private val store: Store by inject()
    private val selectedMonth = SimpleIntegerProperty(LocalDate.now().month.value)
    private val selectedYear = SimpleIntegerProperty(LocalDate.now().year)
    private var selectedTimeEntries = SortedFilteredList<TimeEntry>()

    override val root = vbox {
        hbox {
            combobox(selectedYear, store.years.sortedItems)
            combobox(selectedMonth, store.months.sortedItems)
        }
        tableview(store.timeEntries[DateKey(LocalDate.now().year, LocalDate.now().monthValue)]) {
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

    init {
        selectedTimeEntries = store.timeEntries[DateKey(LocalDate.now().year, LocalDate.now().monthValue)]!!
        selectedYear.onChange {
            fire(SelectedListChangedEvent(it, selectedMonth.value))
        }

        selectedMonth.onChange {
            fire(SelectedListChangedEvent(selectedYear.value, it))
        }
    }
}

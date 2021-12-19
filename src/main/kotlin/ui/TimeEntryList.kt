package ui

import controller.Store
import data.DateKey
import data.TimeEntry
import events.SelectedListChangedEvent
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*
import java.time.LocalDate

class TimeEntryList: View() {
    private val store: Store by inject()
    private val selectedMonth = SimpleIntegerProperty(store.months.first())
    private val selectedYear = SimpleIntegerProperty(store.years.first())
    private var selectedTimeEntries = SortedFilteredList<TimeEntry>()

    override val root = vbox {
        hbox {
            combobox(selectedYear, store.years)
            combobox(selectedMonth, store.months)
        }
        tableview(store.timeEntries[DateKey(LocalDate.now().year, LocalDate.now().monthValue)]) {
            isEditable = true
            readonlyColumn("Date", TimeEntry::date)
            readonlyColumn("Start Time ", TimeEntry::startTime)
            readonlyColumn("End Time", TimeEntry::endTime)
            readonlyColumn("Break", TimeEntry::breakDuration)
            subscribe<SelectedListChangedEvent> { event ->
                val dateKey = DateKey(event.year, event.month)
                if (store.timeEntries.containsKey(dateKey)) {
                    items = store.timeEntries[dateKey]
                }
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

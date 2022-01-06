package ui

import controller.Store
import events.AddTimeEntryEvent
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import utils.EntryParser
import java.time.LocalDate


class TimeInput: View() {
    private val store: Store by inject()
    private val date = SimpleObjectProperty<LocalDate>()

    override val root = vbox {
        label("Time Entry")
        hbox {
            textfield {
                promptText = "Enter Time"
                action {
                    val timeEntry = EntryParser().getTime(text, date.get())
                    store.addTimeEntry(timeEntry, date.get())
                    fire(AddTimeEntryEvent(date.get().year, date.get().monthValue, timeEntry))
                    clear()
                }
            }
            datepicker(date) {
                value = LocalDate.now()
            }}
    }
}

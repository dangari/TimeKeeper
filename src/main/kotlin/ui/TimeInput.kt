package ui

import controller.Store
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
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
                    store.addTimeEntry(text, date.get())
                    clear()
                }
            }
            datepicker(date) {
                value = LocalDate.now()
            }}
    }
}

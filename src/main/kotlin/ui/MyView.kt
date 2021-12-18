package ui

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.View
import tornadofx.borderpane

class MyView : View("My View") {
    override val root = borderpane()

    init {
        title = "TimeKeeper"

        with(root) {
            top(TimeInput::class)
            center(TimeEntryList::class)
        }
    }
}

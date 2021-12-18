package ui

import controller.Store
import data.TimeEntry
import tornadofx.ListCellFragment
import tornadofx.hbox
import tornadofx.label

class TimeEntryFragment: ListCellFragment<TimeEntry>() {
    val store: Store by inject()

    override val root = hbox {
        label (itemProperty.asString())
    }
}

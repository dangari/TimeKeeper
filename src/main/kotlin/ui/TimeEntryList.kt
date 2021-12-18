package ui

import controller.Store
import tornadofx.View
import tornadofx.listview

class TimeEntryList: View() {
    private val store: Store by inject()

    override val root = listview(store.timeEntries) {
        isEditable = true
        cellFragment(TimeEntryFragment::class)
    }
}

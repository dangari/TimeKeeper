package ui

import tornadofx.View
import tornadofx.borderpane
import tornadofx.hbox
import tornadofx.label

class MyView : View("My View") {
    override val root = borderpane()

    init {
        title = "TimeKeeper"

        with(root) {
            top(TimeInput::class)
            center(TimeEntryList::class)
            bottom = label("Create by Akadia")
        }
    }
}

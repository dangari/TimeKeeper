package events

import tornadofx.FXEvent

class SelectedListChangedEvent(val year: Int, val month: Int): FXEvent()

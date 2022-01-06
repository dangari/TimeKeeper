package events

import data.TimeEntry
import tornadofx.FXEvent

class AddTimeEntryEvent(val year: Int, val month: Int, timeEntry: TimeEntry) :FXEvent()

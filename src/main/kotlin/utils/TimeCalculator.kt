package utils

import data.Time
import data.TimeEntry
import data.TimeEntryType
import tornadofx.SortedFilteredList


fun SortedFilteredList<TimeEntry>.getOverTimeOfMonth(): Time {
    return this.map{entry -> entry.overTime}.reduce { totalOvertime, time -> totalOvertime + time }
}

fun SortedFilteredList<TimeEntry>.getTotalWorkingHours(): Time {
    return this.map{entry -> entry.workingTime}.reduce { totalOvertime, time -> totalOvertime + time }
}

fun SortedFilteredList<TimeEntry>.getVacationDays(): Int {
    return this.filter { entry -> entry.type == TimeEntryType.VACATION }.size
}


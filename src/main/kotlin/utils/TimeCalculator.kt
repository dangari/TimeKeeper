package utils

import data.*
import tornadofx.SortedFilteredList
import java.time.LocalDate


data class CalculatedTime(val overtime: Time, val workingHours: Time, val vacationDays : Int) {
    override fun toString(): String {
        return "Overtime: $overtime, Working Hours: $workingHours, Vacation days: $vacationDays"
    }

    constructor(): this(Time(0,0), Time(0,0), 0)

    operator fun minus(other: CalculatedTime): CalculatedTime {
        return CalculatedTime(
            this.overtime - other.overtime,
            this.workingHours - other.workingHours,
            this.vacationDays - other.vacationDays
        )
    }
    operator fun plus(other: CalculatedTime): CalculatedTime {
        return CalculatedTime(
            this.overtime + other.overtime,
            this.workingHours + other.workingHours,
            this.vacationDays + other.vacationDays
        )
    }
}

class TimeCalculator {
    private val calculatedTimes = HashMap<DateKey, CalculatedTime>()
    val vacationDaysTotal = Config().vacation.keys.reduce{sum: Int, key -> sum + Config().vacation[key]!!}


    fun init(timeEntries: HashMap<DateKey, SortedFilteredList<TimeEntry>>): TimeCalculator {
        timeEntries.forEach{monthlyEntry ->
            getMonthlyCalculatedTimes(monthlyEntry.key, monthlyEntry.value) }

        return this
    }

    fun getMonthlyCalculatedTimes(dateKey: DateKey, entries: SortedFilteredList<TimeEntry>): CalculatedTime {
        if (entries.size == 0) {
            calculatedTimes[dateKey] = CalculatedTime()
            return CalculatedTime()
        }
        val overTimeMonth = entries.getOverTimeOfMonth()
        val vacationDaysOfMonth = entries.getVacationDays()
        val workingHoursMonth = entries.getTotalWorkingHours()
        val calculatedTime = CalculatedTime(overTimeMonth, workingHoursMonth, vacationDaysOfMonth)
        calculatedTimes[dateKey] = calculatedTime
        return calculatedTime
    }

    fun yearTimeCalculation(year: Int): CalculatedTime {
        var totalCalculatedTime = CalculatedTime()
        calculatedTimes.forEach{ monthlyEntry ->
            if(monthlyEntry.key.year != year) {
                return@forEach
            }
            totalCalculatedTime += monthlyEntry.value
        }

        return totalCalculatedTime
    }

    fun totalTimeCalculation(): CalculatedTime {
        val currentYear = LocalDate.now().year
        val currentMonth = LocalDate.now().monthValue
        var totalCalculatedTime = CalculatedTime()
        calculatedTimes.forEach{ monthlyEntry ->
            if(monthlyEntry.key.year > currentYear || monthlyEntry.key.month > currentMonth && monthlyEntry.key.year == currentYear) {
                return@forEach
            }
            totalCalculatedTime += monthlyEntry.value
        }

        return totalCalculatedTime
    }

}

private fun SortedFilteredList<TimeEntry>.getOverTimeOfMonth(): Time {
    return this.map{entry -> entry.overTime}.reduce { totalOvertime, time -> totalOvertime + time }
}

private fun SortedFilteredList<TimeEntry>.getTotalWorkingHours(): Time {
    return this.map{entry -> entry.workingTime}.reduce { totalOvertime, time -> totalOvertime + time }
}

private fun SortedFilteredList<TimeEntry>.getVacationDays(): Int {
    return this.filter { entry -> entry.type == TimeEntryType.VACATION }.size
}

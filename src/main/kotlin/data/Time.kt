package data

data class Time(val hour: Int, val minutes: Int): Comparable<Time> {

    init {
        require(hour in 0 .. 23)
        require(minutes in 0 .. 59)
    }

    override fun toString(): String {
        return "${hour.timeFormat()}:${hour.timeFormat()}"
    }

    override fun compareTo(other: Time): Int {
        if (hour > other.hour) return 1
        if (hour < other.hour) return -1
        if (minutes > other.minutes) return 1
        if (minutes < other.minutes) return -1

        return 0
    }

    private fun Int.timeFormat(): String {
        return if (this <= 9) "0$this" else this.toString()
    }
}

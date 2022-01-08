package data

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class Time(val hour: Int, val minutes: Int, val isOverTime: Boolean = false): Comparable<Time> {

    init {
        if (!isOverTime) {
            require(hour in 0..23)
            require(minutes in 0..59)
        }
        else {
            require(minutes in 0..59 || minutes in -59 .. -1)
            require(minutes >= 0 && hour >= 0 || minutes <= 0 && hour <= 0)
        }
    }

    constructor(minutes: Int, isOverTime: Boolean = false): this(minutes/60, minutes - 60 * (minutes/60), isOverTime)

    operator fun plus(other: Time) = Time(this.inMinutes() + other.inMinutes(), true)
    operator fun minus(other: Time) = Time(this.inMinutes() - other.inMinutes(), true)

    fun inMinutes(): Int {
        return this.minutes + this.hour * 60
    }

    override fun toString(): String {
        return (if(hour < 0 || minutes < 0) "-" else "") + "${abs(hour).timeFormat()}:${abs(minutes).timeFormat()}"

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

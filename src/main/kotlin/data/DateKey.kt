package data

import kotlinx.serialization.Serializable
import serializer.DateKeySerializer

@Serializable(with = DateKeySerializer::class)
data class DateKey(val year: Int, val month: Int) : Comparable<DateKey> {

    constructor(value: Int): this(value/100, value.mod(100))

    override fun compareTo(other: DateKey): Int {
        if (year > other.year) return 1
        if (year < other.year) return -1
        if (month > other.month) return 1
        if (month < other.month) return -1

        return 0
    }

    fun toInt(): Int {
        return year*100 + month
    }
}

package data

data class DateKey(val year: Int, val month: Int) : Comparable<DateKey> {
    override fun compareTo(other: DateKey): Int {
        if (year > other.year) return 1
        if (year < other.year) return -1
        if (month > other.month) return 1
        if (month < other.month) return -1

        return 0
    }
}

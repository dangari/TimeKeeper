package parser

import data.Time
import data.TimeEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

internal class EntryParserTest {

    private val localDate: LocalDate = LocalDate.now()

    @Test
    fun testTimeEntryParser() {
        val testString = "11:00-15:00/60"
        val startTime = Time(11, 0)
        val endTime = Time(15, 0)
        assertEquals(TimeEntry(startTime, endTime, 60, localDate), EntryParser().getTime(testString, localDate))
    }

    @Test
    fun testTimeEntryParserWithoutBreak() {
        val testString = "11:00-15:00"
        val startTime = Time(11, 0)
        val endTime = Time(15, 0)
        assertEquals(TimeEntry(startTime, endTime, 0, localDate), EntryParser().getTime(testString, localDate))
    }

    @Test
    fun testInvalidInput() {
        val testString = "asdasd"
        assertThrows<RuntimeException> { EntryParser().getTime(testString, localDate) }
    }
}

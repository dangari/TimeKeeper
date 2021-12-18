package parser

import data.TimeEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import java.util.*

internal class EntryParserTest {

    @Test
    fun testTimeEntryParser() {
        val testString = "11:00-15:00/60"
        val startTime = Calendar.Builder().setTimeOfDay(11,0,0).build();
        val endTime = Calendar.Builder().setTimeOfDay(15,0,0).build()
        assertEquals(TimeEntry(startTime, endTime, 60), EntryParser().getTime(testString))
    }

    @Test
    fun testTimeEntryParserWithoutBreak() {
        val testString = "11:00-15:00"
        val startTime = Calendar.Builder().setTimeOfDay(11,0,0).build();
        val endTime = Calendar.Builder().setTimeOfDay(15,0,0).build()
        assertEquals(TimeEntry(startTime, endTime, 0), EntryParser().getTime(testString))
    }

    @Test
    fun testInvalidInput() {
        val testString = "asdasd"
        assertThrows<RuntimeException> { EntryParser().getTime(testString) }
    }
}

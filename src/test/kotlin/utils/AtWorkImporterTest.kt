package utils

import org.junit.jupiter.api.Test

internal class AtWorkImporterTest {

    @Test
    fun testFileReading() {
        AtWorkImporter().import("F:\\Projects\\TimeKeeper\\src\\test\\kotlin\\utils\\times.csv")
    }

    @Test
    fun testFileImporter() {
        HolidayImporter().importHolidays("F:\\Projects\\TimeKeeper\\src\\recources\\holidays.json")
    }
}

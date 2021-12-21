package utils

import data.HolidayApiModel
import data.TimeEntry
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.time.LocalDate


class HolidayImporter {
    private val jsonParser = Json {
        ignoreUnknownKeys = true
    }

    fun importHolidays(fileName: String): List<TimeEntry> {
        val inputStream: InputStream = File(fileName).inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        Json.configuration.ignoreUnknownKeys
        val holidaysFromApi = jsonParser.decodeFromString<HolidayApiModel>(inputString)
        val holidays = ArrayList<TimeEntry>()

        holidaysFromApi.feiertage.forEach { holiday -> holidays.add(TimeEntry(LocalDate.parse(holiday.date), holiday.fname)) }

        return holidays
    }
}

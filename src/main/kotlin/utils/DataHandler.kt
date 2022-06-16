package utils

import data.DateKey
import data.HolidayApiModel
import data.LocalSave
import data.TimeEntry
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import tornadofx.SortedFilteredList
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.InputStream


class DataHandler {
    private val baseDir = System.getProperty("user.dir")
    private val saveFile = "$baseDir/resources//save.json"

    fun saveEntriesToFile(timeEntries: HashMap<DateKey, SortedFilteredList<TimeEntry>>) {
        val dataToSave = timeEntries.convertSortedList()
        val localSave = LocalSave(dataToSave)
        val string = Json.encodeToString(localSave)

        File(saveFile).writeText(string)
    }

    fun loadEntriesFromFile(): HashMap<DateKey, SortedFilteredList<TimeEntry>> {
        val saveFile = File(saveFile)
        val inputStream: InputStream = saveFile.inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        if (inputString.isBlank()) return HashMap()
        val localSave = Json.decodeFromString<LocalSave>(inputString)

        return localSave.timeEntries.convertList()
    }
}

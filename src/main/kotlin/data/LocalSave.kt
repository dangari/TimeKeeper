package data

import kotlinx.serialization.Serializable

@Serializable
data class LocalSave(
    val timeEntries: HashMap<DateKey, List<TimeEntry>>,
    val version: String = "1.0"
)

package data

import kotlinx.serialization.Serializable

@Serializable
class HolidaysModel(val date: String, val fname: String)

@Serializable
class HolidayApiModel(val feiertage: List<HolidaysModel>)

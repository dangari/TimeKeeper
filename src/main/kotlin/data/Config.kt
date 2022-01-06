package data

data class Config(
    val workingHours: Time = Time(8,0),
    val vacation: HashMap<Int, Int> = hashMapOf(2020 to 24, 2021 to 30, 2022 to 30))

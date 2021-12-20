package utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AtWorkExportReaderTest {

    @Test
    fun testFileReading() {
        AtWorkExportReader().readFileLineByLineUsingForEachLine("F:\\Projects\\TimeKeeper\\src\\test\\kotlin\\utils\\times.csv")
    }
}

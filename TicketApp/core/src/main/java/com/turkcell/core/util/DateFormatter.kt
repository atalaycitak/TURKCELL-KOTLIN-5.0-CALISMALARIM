package com.turkcell.core.util

private val turkishMonthsShort = arrayOf(
    "Oca","Sub","Mar","Nis","May","Haz","Tem","Agu","Eyl","Eki","Kas","Ara"
)

object DateFormatter {
    fun format(isoDate: String): String {
        return try {
            val datePart = isoDate.substringBefore("T")
            val parts = datePart.split("-")
            val year = parts[0]
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            val hour = isoDate.substringAfter("T").substringBefore(":").toIntOrNull() ?: 0
            val minute = isoDate.substringAfter(":").substringBefore(":").toIntOrNull() ?: 0
            "$day ${turkishMonthsShort[month - 1]} $year, %02d:%02d".format(hour, minute)
        } catch (e: Exception) {
            isoDate
        }
    }
}
package market

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.TimeZone
import java.util.Calendar

/**
* @param dateString "dd-mm-yyyy"
*/
fun dateToUnixTimestamp(dateString: String): Long {
    val l = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    return l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
}

fun timeStampToDate(timeStamp: Long): String {
    val date = Date(timeStamp*1000)
    val cal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()))
    cal.time = date
    return dateToString(cal)
}

fun subtractMonthsFromCurrentDate(months: Int): String {
    val referenceDate = Date()
    val c = Calendar.getInstance()
    c.time = referenceDate
    c[Calendar.DAY_OF_MONTH] = 1
    c.add(Calendar.MONTH, -months)
    return dateToString(c)
}

fun dateToString(cal: Calendar): String {
    return "${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.YEAR)}"
}
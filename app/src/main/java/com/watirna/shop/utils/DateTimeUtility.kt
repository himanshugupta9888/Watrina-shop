package com.watirna.shop.utils

import com.watirna.shop.application.AppController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtility {

    init {
        AppController.appComponent.inject(this)
    }

    fun toLocalTime(time: Long, to: TimeZone): Long {
        return convertTime(time, Constants.utcTZ, to)
    }

    fun toUTC(time: Long, from: TimeZone): Long {
        return convertTime(time, from, Constants.utcTZ)
    }

    fun convertTime(time: Long, from: TimeZone, to: TimeZone): Long {
        return time + getTimeZoneOffset(time, from, to)
    }

    private fun getTimeZoneOffset(time: Long, from: TimeZone, to: TimeZone): Long {
        val fromOffset = from.getOffset(time)
        var toOffset = to.getOffset(time)
        val diff: Int

        if (fromOffset >= 0) {
            toOffset = if (toOffset > 0) {
                toOffset.times(-1)
            } else {
                Math.abs(toOffset)
            }
            diff = (fromOffset.plus(toOffset)).times(-1)
        } else {
            if (toOffset <= 0) {
                toOffset = (Math.abs(toOffset)).times(-1)
            }
            diff = (Math.abs(fromOffset)).plus(toOffset)
        }
        return diff.toLong()
    }

    fun getCalendar(year: Int, month: Int, day: Int, hour: Int, minutes: Int): Calendar {
        val cal = Calendar.getInstance()
        if (year > 0) {
            cal.set(Calendar.YEAR, year)
        }
        if (month > 0) {
            cal.set(Calendar.MONTH, month)
        }
        if (day > 0) {
            cal.set(Calendar.DAY_OF_MONTH, day)
        }
        if (hour >= 0) {
            cal.set(Calendar.HOUR_OF_DAY, hour)
        }
        if (minutes >= 0) {
            cal.set(Calendar.MINUTE, minutes)
        }
        return cal
    }

    fun convertUtcStringToDate(stringDate: String): Date? {
        var date: Date? = null
        try {
            date = Constants.utcFormat.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    fun convertStringToDate(stringDate: String): Date? {
        var date: Date? = null
        try {
            date = Constants.utcFormat.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    fun getDateFormatFromString(stringDate: String): Date? {
        var date: Date? = null
        try {
            date = Constants.dateFormat.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    fun toLocalDate(utcDateString: String): Date {
        val calendar = Calendar.getInstance()
        calendar.time = convertUtcStringToDate(utcDateString)
        val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
        calendar.timeInMillis = sLocalTime
        return calendar.time
    }

    fun toUtcString(localDate: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = localDate
        val utcTime = toUTC(calendar.timeInMillis, calendar.timeZone)
        calendar.timeInMillis = utcTime
        return getDateTimeString(calendar.time)
    }

    fun getDateTimeString(date: Date): String {
        return Constants.dateTimeFormat.format(date)
    }

    fun getDateTimeString(utcDateString: String): String {
        var date: Date? = null
        var dateString = utcDateString
        try {
            date = Constants.utcFormat.parse(utcDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateString = Constants.dateTimeFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getUTCTime(time: Date): String {
        try {
            val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val timeZone = TimeZone.getTimeZone("UTC")
            val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            simpleDateFormat.timeZone = timeZone

            return simpleDateFormat.format(time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getDateString(localDateString: String): String {
        var date: Date? = null
        var dateString = localDateString
        try {
            date = Constants.utcFormat.parse(localDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateString = Constants.myPostDateFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getUtcDateString(localDateString: String): String {
        var date: Date? = null
        var dateString = localDateString
        try {
            date = Constants.dateFormat.parse(localDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val utcTime = toUTC(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = utcTime

            dateString = Constants.dateFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getLocalTimeString(localDateString: String): String {
        var date: Date? = null
        var localTimeString = localDateString
        try {
            date = Constants.dateTimeFormat.parse(localDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            localTimeString = Constants.timeFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return localTimeString
    }

    fun getUtcTimeString(localDateString: String): String {
        var date: Date? = null
        var utcTimeString = localDateString
        try {
            date = Constants.dateTimeFormat.parse(localDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val utcTime = toUTC(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = utcTime

            utcTimeString = Constants.dateTimeFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return utcTimeString
    }

    fun getConfirmPageDateTime(utcDateString: String): String {
        var date: Date? = null
        var dateString = utcDateString
        try {
            date = Constants.dateTimeFormat.parse(utcDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateString = Constants.dateTimeFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getConfirmPageDate(utcDateString: String): String {
        var date: Date? = null
        var dateString = utcDateString
        try {
            date = Constants.dateFormat.parse(utcDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateString = Constants.dateFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getEnlargeViewTitle(utcDateString: String): String {
        var date: Date? = null
        var month = utcDateString
        try {
            date = Constants.utcFormat.parse(utcDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            month = Constants.dayViewFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return month
    }

    fun getEnlargeViewSubTitle(utcDateString: String): String {
        var date: Date? = null
        var month = utcDateString
        try {
            date = Constants.utcFormat.parse(utcDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            month = Constants.timeFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return month
    }

    fun getDayViewTitle(date: Date): String {
        var dateString = ""
        try {
            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateString = Constants.dayViewFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getExpireDate(utcDateString: String): String {
        var date: Date? = null
        var dateString = utcDateString
        try {
            date = Constants.utcFormat.parse(utcDateString)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateString = Constants.dateFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString
    }

    fun getBabyDiaryViewDate(utcDateString: String): String {
        var date: Date? = null
        var dateStr = utcDateString
        try {
            date = Constants.utcFormat.parse(utcDateString)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val sLocalTime = toLocalTime(calendar.timeInMillis, calendar.timeZone)
            calendar.timeInMillis = sLocalTime

            dateStr = Constants.dateTimeDiaryFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateStr
    }

    fun isValidDate(date: String): Boolean {
        try {
            val df = Constants.utcFormat
            df.isLenient = false
            df.parse(date)
            return true
        } catch (e: ParseException) {
        }
        return false

    }

    fun getDateFromMilliSec(milliSeconds: Long, dateFormat: SimpleDateFormat): String {
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return dateFormat.format(calendar.time)
    }


    fun getCurrentTimeChat(): String {
        try {
            val timeZone = TimeZone.getTimeZone("UTC")
            val simpleDateFormat = SimpleDateFormat(Constants.UDC_TIME, Locale.ENGLISH)
            simpleDateFormat.timeZone = timeZone
            return simpleDateFormat.format(Date())
        } catch (e: Exception) {
            // Log.e(Constants.LOGGER_TAG, Constants.LOGGER_ERROR, e);
            return ""
        }

    }
}

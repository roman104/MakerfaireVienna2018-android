/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.mind.synergy.util

import android.support.annotation.UiThread
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Martin Albedinsky
 */
class DateUtils private constructor() {

    companion object {

        internal const val TAG = "DateUtils"

        const val NO_FORMAT = ""
    }

    @Suppress("unused") object Pattern {

        // DATE ------------------------------------------------------------------------------------

        const val DATE = "dd.MM.yyyy"
        const val DATE_ISO = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"
        const val DATE_DESCRIPTIVE = "dd. MMMM yyyy"
        const val DATE_TIME = "dd.MM.yyyy HH:mm"

        // TIME ------------------------------------------------------------------------------------

        const val TIME = "HH:mm"
        const val TIME_DETAILED = "HH:mm:ss"
    }

    class Formatter private constructor() {

        companion object {

            private val UTC_TIME_ZONE = TimeZone.getTimeZone("UTC")
            private val FORMATS = HashMap<String, SimpleDateFormat>(2)

            fun createThreadSafeFormat(pattern: String): ThreadLocal<SimpleDateFormat> {
                return object : ThreadLocal<SimpleDateFormat>() {

                    override fun initialValue() = createFormat(pattern)
                }
            }

            fun createFormat(pattern: String): SimpleDateFormat {
                return SimpleDateFormat(pattern, DatePolices.LOCALE).apply { timeZone = DatePolices.TIME_ZONE }
            }

            private fun obtainFormat(pattern: String): SimpleDateFormat {
                var format: SimpleDateFormat? = null
                synchronized(FORMATS) {
                    if (FORMATS[pattern] != null) {
                        format = FORMATS[pattern]!!
                    } else {
                        format = createFormat(pattern)
                        FORMATS[pattern] = format!!
                    }
                }
                return format!!
            }

            fun formatElapsedTime(timeInMillis: Long): String {
                var formatted = ""
                var hours = 0L
                var minutes = 0L
                var seconds: Long

                if (timeInMillis >= 1000 * 60 * 60) {
                    hours = timeInMillis / (1000 * 60 * 60)
                    formatted = "${hours}h"
                }
                if (timeInMillis >= 1000 * 60) {
                    minutes = timeInMillis / (1000 * 60)
                    minutes -= hours * 60
                    formatted += if (hours > 0) " ${minutes}m" else "${minutes}m"
                }
                if (timeInMillis >= 1000) {
                    seconds = timeInMillis / 1000
                    seconds -= hours * 60 * 60 + minutes * 60
                    formatted += if (hours > 0 || minutes > 0) " ${seconds}s" else "${seconds}s"
                }

                return formatted
            }

            @UiThread fun format(milliseconds: Long, pattern: String) = format(milliseconds, obtainFormat(pattern))

            fun format(milliseconds: Long, format: SimpleDateFormat): String {
                if (milliseconds == 0L) {
                    return NO_FORMAT
                }
                return try {
                    format.format(milliseconds)
                } catch (e: ArrayIndexOutOfBoundsException) {
                    Logging.w(TAG, "Failed to format time($milliseconds) using format($format)!", e)
                    NO_FORMAT
                }
            }

            @UiThread fun parse(data: String?, pattern: String) = parse(data, obtainFormat(pattern))

            @UiThread fun parse(data: String?, pattern: String, timeZone: String?) = parse(data, obtainFormat(pattern), timeZone)

            fun parse(data: String?, format: SimpleDateFormat, timeZone: String?): Long {
                if (data == null || data.isEmpty()) {
                    return DatePolices.NO_TIME
                }
                // Keep format's original time zone so we can restore it after parsing is done.
                val formatTimeZone = format.timeZone
                format.timeZone = if (timeZone == null) UTC_TIME_ZONE else TimeZone.getTimeZone(timeZone)
                val milliseconds = parse(data, format)
                // Restore format's original time zone.
                format.timeZone = formatTimeZone
                return milliseconds
            }

            fun parse(data: String?, format: SimpleDateFormat): Long {
                if (data == null || data.isEmpty()) {
                    return DatePolices.NO_TIME
                }
                return try {
                    format.parse(data).time
                } catch (e: ParseException) {
                    Logging.w(TAG, "Failed to parse data($data) using format($format)!", e)
                    DatePolices.NO_TIME
                }

            }
        }
    }
}
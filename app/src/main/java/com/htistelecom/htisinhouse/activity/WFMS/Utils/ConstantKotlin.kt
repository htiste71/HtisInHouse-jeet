package com.htistelecom.htisinhouse.activity.WFMS.Utils

import com.htistelecom.htisinhouse.activity.WFMS.activity.MainActivityNavigation
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ConstantKotlin {
    companion object
    {
        val FOO = "foo"


        fun getCurrentDateTime(): Pair<String, String> {
            val sdfDate = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            val sdfTime = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)

            val currentDate = sdfDate.format(Date())
            val currentTime = sdfTime.format(Date())

            return Pair(currentTime, currentDate)
        }

        fun getCurrentTime24Hrs(): String {
            val sdfTime = SimpleDateFormat("HH:mm", Locale.ENGLISH)

            val currentTime = sdfTime.format(Date())

            return currentTime
        }

        fun backHomeTrue() {
            MainActivityNavigation.isBackHome = true
        }
    }
}
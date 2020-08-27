package com.htistelecom.htisinhouse.utilities

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ConstantKotlin {
    companion object {
        val FOO = "foo"


        fun getCurrentDateTime(): Pair<String,String> {
            val sdfDate = SimpleDateFormat("dd MMM yyyy")
            val sdfTime = SimpleDateFormat("hh:mm aa")

            val currentDate = sdfDate.format(Date())
            val currentTime = sdfTime.format(Date())

            return Pair(currentTime,currentDate)
        }
        fun getCurrentTime24Hrs(): String {
            val sdfTime = SimpleDateFormat("HH:mm")

            val currentTime = sdfTime.format(Date())

            return currentTime
        }
    }
}
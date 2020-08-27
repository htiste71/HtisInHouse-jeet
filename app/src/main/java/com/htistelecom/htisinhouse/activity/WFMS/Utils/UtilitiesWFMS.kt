package com.htistelecom.htisinhouse.activity.WFMS.Utils

import android.content.Context
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UtilitiesWFMS {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun leaveDate(leaveFromTo: String?): Pair<String, String> {
            val originalFormat: DateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
            val mFromDate=leaveFromTo!!.split("-")[0]
            val mToDate=leaveFromTo!!.split("-")[1]

            val dateFrom: Date = originalFormat.parse(mFromDate)
            val mStrFromDate: String = targetFormat.format(dateFrom)

            val dateTo: Date = originalFormat.parse(mToDate)
            val mStrToDate: String = targetFormat.format(dateTo)
            return Pair(mStrFromDate,mStrToDate)
        }
        fun dateToString(date: Date):String {

            val targetFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")

            val mDate: String = targetFormat.format(date)
            return mDate
        }
    }
}
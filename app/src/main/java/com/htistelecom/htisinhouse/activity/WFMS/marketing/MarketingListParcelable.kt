package com.htistelecom.htisinhouse.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MarketingListParcelable(

        private var fvCompanyName: String,
        private val fdCustReachTime: String,
        private val fdCustOutTime: String,
        private val fvEmployeeName: String,
        val PositionName: String,
        private val EntityName: String,
        private val fvWebLink: String,
        private val BusinessNature: String,
        private val fvCompanyOwner: String,
        private val fvRemarks: String,
        private val fiAnnualTurnoverID: String,
        private val fvCompanyOwnerContact: String,

        private val fvLeadGenerated: String,
        private val fvEmailID: String,
        private val StateName: String,
        private val CityName: String,
        private val fvAddress: String,
        private val fvPhone: String,
        private val fvZipCode: String,
        private val fvEmplyeeId: String,
        private val TaskDate: String? = null


) : Parcelable
package com.htistelecom.htisinhouse.activity.WFMS.add_details.repository

import com.htistelecom.htisinhouse.activity.WFMS.add_details.network.APIHelper

class Repository(private val apiHelper: APIHelper) {
    suspend fun getCountriesList() = apiHelper.getCountry()
}
package com.htistelecom.htisinhouse.activity.WFMS.add_details.network

class APIHelper(private val apiClient:APIClient) {

    suspend fun getCountry()=apiClient.countryList()
}
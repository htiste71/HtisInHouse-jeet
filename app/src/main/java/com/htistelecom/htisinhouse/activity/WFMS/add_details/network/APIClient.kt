package com.htistelecom.htisinhouse.activity.WFMS.add_details.network

import com.htistelecom.htisinhouse.activity.WFMS.add_details.models.CountryModel
import retrofit2.Response
import retrofit2.http.GET

interface APIClient {

    @GET("CountryList")
    suspend fun countryList(): Response<ArrayList<CountryModel>>


}
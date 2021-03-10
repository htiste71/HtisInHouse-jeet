package com.htistelecom.htisinhouse.utilities

import com.htistelecom.htisinhouse.activity.WFMS.AddTask.Companion.projectArray
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

class CountriesClass {


    companion object {
        var countryList = ArrayList<TwoParameterModel>()
        lateinit var countryArray: Array<String?>

        var stateList = ArrayList<TwoParameterModel>()
        lateinit var stateArray: Array<String?>

        var cityList = ArrayList<TwoParameterModel>()
        lateinit var cityArray: Array<String?>

        fun commonMethod(response: Any?, TYPE: Int) {
            if (TYPE == ConstantsWFMS.COUNTRY_LIST_WFMS) {
                countryArray = emptyArray()
                countryList.clear()
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {
                    val jsonArray = json.getJSONArray("Output")
                    for (i in 0 until jsonArray.length()) {
                        val jsonInner = jsonArray.getJSONObject(i)
                        val model = TwoParameterModel()
                        model.id = jsonInner.getString("CountryId")
                        model.name = jsonInner.getString("CountryName")
                        countryList.add(model)
                    }

                }


                countryArray = arrayOfNulls<String>(countryList.size)
                for (i in countryList.indices) {
                    countryArray[i] = countryList.get(i).name
                }


            } else if (TYPE == ConstantsWFMS.STATE_LIST_WFMS) {
                stateArray = emptyArray()
                stateList.clear()
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {
                    val jsonArray = json.getJSONArray("Output")
                    for (i in 0 until jsonArray.length()) {
                        val jsonInner = jsonArray.getJSONObject(i)
                        val model = TwoParameterModel()
                        model.id = jsonInner.getString("StateId")
                        model.name = jsonInner.getString("StateName")
                        stateList.add(model)
                    }

                }

                stateArray = arrayOfNulls<String>(stateList.size)
                for (i in stateList.indices) {
                    stateArray[i] = stateList.get(i).name
                }

            } else if (TYPE == ConstantsWFMS.CITY_LIST_WFMS) {
                cityArray = emptyArray()
                cityList.clear()
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {
                    val jsonArray = json.getJSONArray("Output")
                    for (i in 0 until jsonArray.length()) {
                        val jsonInner = jsonArray.getJSONObject(i)
                        val model = TwoParameterModel()
                        model.id = jsonInner.getString("CityId")
                        model.name = jsonInner.getString("CityName")
                        cityList.add(model)
                    }

                }
                cityArray = arrayOfNulls<String>(cityList.size)
                for (i in cityList.indices) {
                    cityArray[i] = cityList.get(i).name
                }
            }

        }
    }


}
package com.htistelecom.htisinhouse.activity.WFMS

import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import org.json.JSONArray
import retrofit2.Response

class AddTask {
    companion object {
        var projectList = ArrayList<TwoParameterModel>()
        lateinit var projectArray: Array<String?>

         var siteList = ArrayList<TwoParameterModel>()
        lateinit var siteArray: Array<String?>

         var activityList = ArrayList<TwoParameterModel>()
        lateinit var activityArray: Array<String?>

        fun commonMethod(response: Any?, TYPE: Int) {
            if (TYPE == PROJECT_LIST_WFMS) {
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())


                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)

                        val model = TwoParameterModel()
                        model.id = item.getString("ProjectId")
                        model.name = item.getString("ProjectName")
                        projectList.add(model)
                    }
                    projectArray = arrayOfNulls<String>(projectList.size)
                    for (i in projectList.indices) {
                        projectArray[i] = projectList.get(i).name
                    }


            } else if (TYPE == SITE_LIST_WFMS) {
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("SiteId")
                    model.name = item.getString("SiteName")
                    siteList.add(model)
                }

                siteArray = arrayOfNulls<String>(siteList.size)
                for (i in siteList.indices) {
                    siteArray[i] = siteList.get(i).name
                }

            } else if (TYPE == ACTIVITY_LIST_WFMS) {
                val jsonArray = JSONArray((response as Response<*>).body()!!.toString())

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)

                    val model = TwoParameterModel()
                    model.id = item.getString("ActivityId")
                    model.name = item.getString("ActivityName")
                    activityList.add(model)
                }
                activityArray = arrayOfNulls<String>(activityList.size)
                for (i in activityList.indices) {
                    activityArray[i] = activityList.get(i).name
                }
            }

        }
    }


}
package com.htistelecom.htisinhouse.activity.WFMS.add_project

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.CountriesClass
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_add_project.*
import org.json.JSONObject
import retrofit2.Response

class AddProjectFragment : Fragment(), MyInterface, View.OnClickListener, FragmentLifecycle {

    private var hasBeenVisibleOnce = false

    private var mCountryId: String = ""
    private var mStateId: String = ""
    private var mCityId: String = ""
    private var mProjectName = ""
    private lateinit var tinyDB: TinyDB

    lateinit var listPopupWindow: ListPopupWindow


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_project, null)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        clickListener()
        // hitAPI(ConstantsWFMS.COUNTRY_LIST_WFMS, "")


        btnSubmitAddProjectFragment.setOnClickListener {

            mProjectName = etAddProjectFragment.text.toString()
            if (mProjectName.equals("")) {
                UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errEnterProject))
            } else if (mCountryId.equals("")) {
                UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectCountry))

            } else if (mStateId.equals("")) {
                UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectState))

            } else if (mCityId.equals("")) {
                UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectCity))

            } else {
                val jsonObject = JSONObject()
                jsonObject.put("ProjectId", "0")
                jsonObject.put("ProjectName", mProjectName)
                jsonObject.put("CustomerId", "0")
                jsonObject.put("StateId", mStateId)
                jsonObject.put("CityId", mCityId)

                hitAPI(SUBMIT_PROJECT_WFMS, jsonObject.toString())


            }

        }


    }

    private fun clickListener() {
        rlCityAddProjectFragment.setOnClickListener(this)
        rlStateAddProjectFragment.setOnClickListener(this)
        rlCountryAddProjectFragment.setOnClickListener(this)
    }

    private fun hitAPI(TYPE: Int, params: String) {
        if (TYPE == COUNTRY_LIST_WFMS) {
            ApiData.getGetData(ConstantsWFMS.COUNTRY_LIST_WFMS, this, activity)

        } else if (TYPE == STATE_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.STATE_LIST_WFMS, this, activity)

        } else if (TYPE == CITY_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.CITY_LIST_WFMS, this, activity)

        } else if (TYPE == SUBMIT_PROJECT_WFMS) {
            ApiData.getData(params, ConstantsWFMS.SUBMIT_PROJECT_WFMS, this, activity)

        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if ((response as Response<*>).code() == 401 || (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {
            if (TYPE == SUBMIT_PROJECT_WFMS) {
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {
                    Utilities.showToast(activity, json.getString("Message"))
                    clearCredential()
                }
                else
                {
                    Utilities.showToast(activity, json.getString("Message"))

                }

            } else

                CountriesClass.commonMethod(response, TYPE)

        }

    }

    private fun clearCredential() {
        etAddProjectFragment.setText("")
        tvCountryAddProjectFragment.text=""
        mCountryId=""
        tvStateAddProjectFragment.text=""
        mStateId=""
        tvCityAddProjectFragment.text=""
        mCityId=""
    }

    fun showDropdown(array: Array<String?>, spinnerData: SpinnerData, textView: Ubuntu, width: Int) {
        listPopupWindow = ListPopupWindow(activity!!)
        listPopupWindow!!.setAdapter(ArrayAdapter<Any?>(
                activity!!,
                R.layout.row_profile_spinner, array))
        listPopupWindow!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_white_background_no_radius_border))
        listPopupWindow!!.anchorView = textView
        listPopupWindow!!.width = width
        listPopupWindow!!.height = 700
        listPopupWindow!!.isModal = true
        listPopupWindow!!.setOnItemClickListener { parent, view, position, id ->

            if (textView.id == R.id.tvCountryAddProjectFragment) {
                spinnerData.getData(CountriesClass.countryList.get(position).id, CountriesClass.countryList.get(position).name)
            } else if (textView.id == R.id.tvStateAddProjectFragment) {
                spinnerData.getData(CountriesClass.stateList.get(position).id, CountriesClass.stateList.get(position).name)

            } else if (textView.id == R.id.tvCityAddProjectFragment) {
                spinnerData.getData(CountriesClass.cityList.get(position).id, CountriesClass.cityList.get(position).name)
            }
            textView.text = array!![position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlCountryAddProjectFragment -> {
                showDropdown(CountriesClass.countryArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mCountryId = mId
                        mStateId = ""
                        mCityId = ""
                        tvStateAddProjectFragment.text = ""
                        tvCityAddProjectFragment.text = ""
                        val json = JSONObject()
                        json.put("CountryId", mCountryId)
                        hitAPI(STATE_LIST_WFMS, json.toString())

                    }

                }, tvCountryAddProjectFragment, 700)
            }
            R.id.rlStateAddProjectFragment -> {
                showDropdown(CountriesClass.stateArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mStateId = mId

                        mCityId = ""
                        tvCityAddProjectFragment.text = ""
                        val json = JSONObject()
                        json.put("StateId", mStateId)
                        hitAPI(CITY_LIST_WFMS, json.toString())

                    }

                }, tvStateAddProjectFragment, 700)
            }
            R.id.rlCityAddProjectFragment -> {
                showDropdown(CountriesClass.cityArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mCityId = mId


                    }

                }, tvCityAddProjectFragment, 700)
            }

        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser)
            Handler().postDelayed({
                if (activity != null) {
                    hitAPI(COUNTRY_LIST_WFMS, "")
                }
            }, 200)
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onPauseFragment() {
        TODO("Not yet implemented")
    }

    override fun onResumeFragment() {
        TODO("Not yet implemented")
    }
}

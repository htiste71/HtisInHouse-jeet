package com.htistelecom.htisinhouse.activity.WFMS.add_project


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.AddTask
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.PROJECT_LIST_WFMS
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.ConstantKotlin
import com.htistelecom.htisinhouse.utilities.CountriesClass
import com.htistelecom.htisinhouse.utilities.Utilities
import android.widget.ListPopupWindow
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.config.TinyDB
import kotlinx.android.synthetic.main.fragment_add_site.*
import org.json.JSONObject
import retrofit2.Response

class AddSiteFragment : Fragment(), MyInterface, View.OnClickListener, FragmentLifecycle {
    private var mSiteId = ""
    private var mAddress: String = ""
    var mLatitude = ""
    var mLongitude = ""
    private lateinit var tinyDB: TinyDB
    lateinit var listPopupWindow: ListPopupWindow
    private var mCountryId: String = ""
    private var mStateId: String = ""
    private var mCityId: String = ""
    private var mProjectId = ""
    var mSiteName = ""

    private var hasBeenVisibleOnce = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser)
            Handler().postDelayed({
                if (activity != null) {
                    hitAPI(PROJECT_LIST_WFMS, "")
                }
            }, 200)
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_site, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tinyDB = TinyDB(activity)
        //  hitAPI(PROJECT_LIST_WFMS, "")

        rlProjectNameAddSiteFragment.setOnClickListener(this)
        rlCityAddSiteFragment.setOnClickListener(this)
        rlStateAddSiteFragment.setOnClickListener(this)
        rlCountryAddSiteFragment.setOnClickListener(this)
        btnSubmitAddSiteFragment.setOnClickListener(this)

    }

    fun hitAPI(TYPE: Int, params: String) {
        if (TYPE == PROJECT_LIST_WFMS) {

        } else {
            Utilities.dismissDialog()
        }
        if (TYPE == ConstantsWFMS.PROJECT_LIST_WFMS) {
            ApiData.getGetDataSiteAdd(ConstantsWFMS.PROJECT_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.COUNTRY_LIST_WFMS) {
            ApiData.getGetDataSiteAdd(ConstantsWFMS.COUNTRY_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.STATE_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.STATE_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.CITY_LIST_WFMS) {
            ApiData.getData(params, ConstantsWFMS.CITY_LIST_WFMS, this, activity)

        } else if (TYPE == ConstantsWFMS.SUBMIT_SITE_WFMS) {
            ApiData.getData(params, ConstantsWFMS.SUBMIT_SITE_WFMS, this, activity)

        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {

        if (TYPE != PROJECT_LIST_WFMS) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
        }
        if ((response as Response<*>).code() == 401 || (response as Response<*>).code() == 403) {
            if (Utilities.isShowing())
                Utilities.dismissDialog()
            ConstantKotlin.logout(activity!!, tinyDB)
        } else {
            if (TYPE == ConstantsWFMS.PROJECT_LIST_WFMS) {
                AddTask.commonMethod(response, ConstantsWFMS.PROJECT_LIST_WFMS)
                hitAPI(ConstantsWFMS.COUNTRY_LIST_WFMS, "")


            } else if (TYPE == ConstantsWFMS.COUNTRY_LIST_WFMS) {
                CountriesClass.commonMethod(response, TYPE)

            } else if (TYPE == ConstantsWFMS.STATE_LIST_WFMS) {
                CountriesClass.commonMethod(response, TYPE)

            } else if (TYPE == ConstantsWFMS.CITY_LIST_WFMS) {
                CountriesClass.commonMethod(response, TYPE)

            } else if (TYPE == ConstantsWFMS.SUBMIT_SITE_WFMS) {
                val json = JSONObject((response as Response<*>).body()!!.toString())
                if (json.getString("Status").equals("Success")) {
                    Utilities.showToast(activity, json.getString("Message"))
                    clearCredential()
                } else {
                    Utilities.showToast(activity, json.getString("Message"))

                }
            }
        }


    }

    private fun clearCredential() {
        mProjectId = ""
        tvProjectNameAddSiteFragment.text = ""

        etSiteIdAddSiteFragment.setText("")
        mSiteId = ""
        etSiteNameAddSiteFragment.setText("")
        mSiteName = ""
        etAddressAddSiteFragment.setText("")
        mAddress = ""
        tvCountryAddSiteFragment.text = ""
        mCountryId = ""
        tvStateAddSiteFragment.text = ""
        mStateId = ""
        tvCityAddSiteFragment.text = ""
        mCityId = ""
        etLatitudeAddSiteFragment.setText("")
        mLatitude = ""
        etLongitudeAddSiteFragment.setText("")
        mLongitude = ""
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
            if (textView.id == R.id.tvProjectNameAddSiteFragment) {
                spinnerData.getData(AddTask.projectList.get(position).id, AddTask.projectList.get(position).name)
            } else if (textView.id == R.id.tvCountryAddSiteFragment) {
                spinnerData.getData(CountriesClass.countryList.get(position).id, CountriesClass.countryList.get(position).name)
            } else if (textView.id == R.id.tvStateAddSiteFragment) {
                spinnerData.getData(CountriesClass.stateList.get(position).id, CountriesClass.stateList.get(position).name)

            } else if (textView.id == R.id.tvCityAddSiteFragment) {
                spinnerData.getData(CountriesClass.cityList.get(position).id, CountriesClass.cityList.get(position).name)
            }
            textView.text = array!![position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.rlProjectNameAddSiteFragment -> {
                showDropdown(AddTask.projectArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mProjectId = mId
//                        mStateId = ""
//                        mCityId = ""
//                        tvStateAddSiteFragment.text = ""
//                        tvCityAddSiteFragment.text = ""
//                        val json = JSONObject()
//                        json.put("CountryId", mCountryId)
                        hitAPI(ConstantsWFMS.COUNTRY_LIST_WFMS, "")

                    }

                }, tvProjectNameAddSiteFragment, 700)
            }
            R.id.rlCountryAddSiteFragment -> {
                showDropdown(CountriesClass.countryArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mCountryId = mId
                        mStateId = ""
                        mCityId = ""
                        tvStateAddSiteFragment.text = ""
                        tvCityAddSiteFragment.text = ""
                        val json = JSONObject()
                        json.put("CountryId", mCountryId)
                        hitAPI(ConstantsWFMS.STATE_LIST_WFMS, json.toString())

                    }

                }, tvCountryAddSiteFragment, 700)
            }
            R.id.rlStateAddSiteFragment -> {
                showDropdown(CountriesClass.stateArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mStateId = mId

                        mCityId = ""
                        tvCityAddSiteFragment.text = ""
                        val json = JSONObject()
                        json.put("StateId", mStateId)
                        hitAPI(ConstantsWFMS.CITY_LIST_WFMS, json.toString())

                    }

                }, tvStateAddSiteFragment, 700)
            }
            R.id.rlCityAddSiteFragment -> {
                showDropdown(CountriesClass.cityArray, object : SpinnerData {
                    override fun getData(mId: String, mName: String) {

                        mCityId = mId


                    }

                }, tvCityAddSiteFragment, 700)
            }
            R.id.btnSubmitAddSiteFragment -> {

                mSiteName = etSiteNameAddSiteFragment.text.toString()
                mAddress = etAddressAddSiteFragment.text.toString()
                mSiteId = etSiteIdAddSiteFragment.text.toString()
                mLatitude = etLatitudeAddSiteFragment.text.toString()
                mLongitude = etLongitudeAddSiteFragment.text.toString()
                if (mProjectId.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectProject))
                } else if (mSiteName.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errEnterSite))
                } else if (mSiteId.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errEnterSiteId))
                } else if (mAddress.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errEnterAddress))
                } else if (mCountryId.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectCountry))

                } else if (mStateId.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectState))

                } else if (mCityId.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errSelectCity))

                } else if (mLatitude.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.erEnterLatitude))

                } else if (mLongitude.equals("")) {
                    UtilitiesWFMS.showToast(activity!!, activity!!.getString(R.string.errEnterLongitude))

                } else {
                    val jsonObject = JSONObject()
                    jsonObject.put("ProjectId", mProjectId)
                    jsonObject.put("SiteId", "0")
                    jsonObject.put("SiteName", mSiteName)

                    jsonObject.put("SAPID", mProjectId)
                    jsonObject.put("Address", mAddress)
                    jsonObject.put("PinCode", "")
                    jsonObject.put("StateId", mStateId)
                    jsonObject.put("CityId", mCityId)
                    jsonObject.put("Latitude", mLatitude)
                    jsonObject.put("Longitude", mLongitude)

                    hitAPI(ConstantsWFMS.SUBMIT_SITE_WFMS, jsonObject.toString())


                }


            }

        }
    }

    override fun onPauseFragment() {
        TODO("Not yet implemented")
    }

    override fun onResumeFragment() {
        TODO("Not yet implemented")
    }
}


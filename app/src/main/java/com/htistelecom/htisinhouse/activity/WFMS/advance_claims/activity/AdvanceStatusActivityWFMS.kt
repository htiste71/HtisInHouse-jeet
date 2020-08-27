package com.htistelecom.htisinhouse.activity.WFMS.advance_claims.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListPopupWindow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.advance_claims.adapters.AdvanceStatusAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.advance_claims.models.AdvanceClaimListModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.activity_advance_status_wfms.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class AdvanceStatusActivityWFMS : Activity(), View.OnClickListener, MyInterface {

    private var advanceClaimList = ArrayList<AdvanceClaimListModel>()
    lateinit var dialog: Dialog
    var FOR_TYPE = 0
    val FOR_PENDING = 1
    val FOR_APPROVED = 2
    val FOR_REJECTED = 3
    lateinit var tinyDB: TinyDB
    lateinit var adapter: AdvanceStatusAdapterWFMS

    //    var sendToList = ArrayList<SendToModel>()
//    lateinit var sendToArray: Array<String?>
    private lateinit var listPopupWindow: ListPopupWindow
    // var mSendToId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advance_status_wfms)
        initViews()
        listeners()
        ivBack.setOnClickListener { view -> finish() }
        ivDrawer.setOnClickListener { view ->

            dialogAdvanceClaim()
        }


    }


    private fun initViews() {
        tinyDB = TinyDB(this)
        tv_title.text = "Advance Status"
        ivDrawer.visibility = View.VISIBLE
        recyclerViewWFMS.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    private fun listeners() {
        tvPendingAdvanceClaimWFMS.setOnClickListener(this)
        tvApprovedAdvanceClaimWFMS.setOnClickListener(this)
        tvRejectedAdvanceClaimWFMS.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()
        FOR_TYPE = FOR_PENDING
        changeTextColor()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvPendingAdvanceClaimWFMS -> {
                FOR_TYPE = FOR_PENDING
                changeTextColor()

            }
            R.id.tvApprovedAdvanceClaimWFMS -> {
                FOR_TYPE = FOR_APPROVED

                changeTextColor()


            }
            R.id.tvRejectedAdvanceClaimWFMS -> {
                FOR_TYPE = FOR_REJECTED

                changeTextColor()


            }
        }
    }

    fun changeTextColor() {
        val json = JSONObject()
        json.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
        if(FOR_TYPE==FOR_PENDING)
        {
            tvPendingAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorOrange))
            tvApprovedAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvRejectedAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorDarkBlue))
            json.put("RequestStatusId", 1)
        }
        else if(FOR_TYPE==FOR_APPROVED)
        {
            tvPendingAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvApprovedAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorOrange))
            tvRejectedAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorDarkBlue))
            json.put("RequestStatusId", 2)

        }
        else
        {
            tvPendingAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvApprovedAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorDarkBlue))
            tvRejectedAdvanceClaimWFMS.setTextColor(resources.getColor(R.color.colorOrange))
            json.put("RequestStatusId", 3)

        }




        hitAPI(json.toString(), ConstantsWFMS.SHOW_ADVANCE_DETAIL_WFMS)
    }

    fun dialogAdvanceClaim() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_advance_claim_wfms)
        dialog.setCancelable(false)
        dialog.show()
        val btnSubmitAdvanceClaim = dialog.findViewById(R.id.btnSubmitAdvanceClaim) as Button
        val btnCancelAdvanceClaim = dialog.findViewById(R.id.btnCancelAdvanceClaim) as Button
        //   val rlSelectDateRequestAdvance = dialog.findViewById(R.id.rlSelectDateRequestAdvance) as RelativeLayout
        //val rlRequestToSpinnerAdvanceClaim = dialog.findViewById(R.id.rlRequestToSpinnerAdvanceClaim) as RelativeLayout
        val tvSelectDateRequestAdvance = dialog.findViewById(R.id.tvSelectDateRequestAdvance) as Ubuntu
        val etAmountAdvanceClaim = dialog.findViewById(R.id.etAmountAdvanceClaim) as UbuntuEditText
        val etReasonAdvanceClaim = dialog.findViewById(R.id.etReasonAdvanceClaim) as UbuntuEditText


        var mDateAdvanceClaim = ""
        // var mRequestToAdvanceClaim = ""
        var mAmountAdvanceClaim = ""
        var mReasonAdvanceClaim = ""



        tvSelectDateRequestAdvance.text = UtilitiesWFMS.dateToString(Calendar.getInstance().time)

//        rlSelectDateRequestAdvance.setOnClickListener { view ->
//            val datePickerDialog = DatePickerDialog(this,
//                    { view, year1, monthOfYear, dayOfMonth ->
//
//                        var mDate = dayOfMonth.toString() + "-" + Utilities.getMonthFormat(monthOfYear + 1) + "-" + year1
//                        tvSelectDateRequestAdvance.text = mDate
//
//
//                    }, year, month, day)
//
//            datePickerDialog.datePicker.minDate = calc.timeInMillis
//            datePickerDialog.show()
//
//        }


//        rlRequestToSpinnerAdvanceClaim.setOnClickListener { view ->
//
//
//            showDropdown(sendToArray, object : SpinnerData {
//                override fun getData(mId: String, mName: String) {
//                    mSendToId = mId
//                }
//            }, tvRequestToAdvanceClaim, 700)
//        }


        btnSubmitAdvanceClaim.setOnClickListener { view ->
            mDateAdvanceClaim = tvSelectDateRequestAdvance.text.toString()
            // mRequestToAdvanceClaim = tvRequestToAdvanceClaim.text.toString()
            mAmountAdvanceClaim = etAmountAdvanceClaim.text.toString()
            mReasonAdvanceClaim = etReasonAdvanceClaim.text.toString()
//            if (mRequestToAdvanceClaim.equals("")) {
//                Utilities.showToast(this, resources.getString(R.string.errRequestTo))
//            }
            if (mAmountAdvanceClaim.equals("")) {
                Utilities.showToast(this, resources.getString(R.string.errAmount))
            } else if (mReasonAdvanceClaim.equals("")) {
                Utilities.showToast(this, resources.getString(R.string.errReason))
            } else {
                val jsonObject = JSONObject()
                jsonObject.put("RequestId", "0")
                jsonObject.put("RequestDate", mDateAdvanceClaim)
                jsonObject.put("Remarks", mReasonAdvanceClaim)
                jsonObject.put("Amount", mAmountAdvanceClaim)
                // jsonObject.put("RequestTo", mSendToId)

                jsonObject.put("EmpId", tinyDB.getString(ConstantsWFMS.TINYDB_EMP_ID))
                hitAPI(jsonObject.toString(), ConstantsWFMS.REQUEST_ADVANCE_WFMS)
            }

        }
        btnCancelAdvanceClaim.setOnClickListener { view -> dialog.dismiss() }
    }

    private fun hitAPI(params: String, TYPE: Int) {
        if (TYPE == REQUEST_ADVANCE_WFMS) {
            ApiData.getData(params, REQUEST_ADVANCE_WFMS, this, this)

        } else if (TYPE == ConstantsWFMS.SHOW_ADVANCE_DETAIL_WFMS) {
            ApiData.getData(params, SHOW_ADVANCE_DETAIL_WFMS, this, this)
        }

//        else if (TYPE == DOCUMENT_SEND_TO_WFMS) {
//            ApiData.getGetData(ConstantsWFMS.DOCUMENT_SEND_TO_WFMS, this, this)
//
//        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utilities.dismissDialog()
        if (TYPE == REQUEST_ADVANCE_WFMS) {
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                Utilities.showToast(this, jsonObject.getString("Message"))
                dialog.dismiss()
                changeTextColor()
            }

        } else if (TYPE == SHOW_ADVANCE_DETAIL_WFMS) {
            advanceClaimList.clear()
            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
            if (jsonObject.getString("Status").equals("Success")) {
                recyclerViewWFMS.visibility = View.VISIBLE
                tvNoRecordAdvanceStatusActivityWFMS.visibility = View.GONE
                advanceClaimList = Gson().fromJson<java.util.ArrayList<AdvanceClaimListModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<AdvanceClaimListModel>>() {

                }.type)

                if (FOR_TYPE == FOR_PENDING) {
                    recyclerViewWFMS.adapter = AdvanceStatusAdapterWFMS(this, 0, advanceClaimList)
                } else if (FOR_TYPE == FOR_APPROVED) {
                    recyclerViewWFMS.adapter = AdvanceStatusAdapterWFMS(this, 1, advanceClaimList)

                } else {
                    recyclerViewWFMS.adapter = AdvanceStatusAdapterWFMS(this, 2, advanceClaimList)

                }


            } else {
                recyclerViewWFMS.visibility = View.GONE
                tvNoRecordAdvanceStatusActivityWFMS.visibility = View.VISIBLE
            }
        }
//        else if (TYPE == ConstantsWFMS.DOCUMENT_SEND_TO_WFMS) {
//            val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
//            if (jsonObject.getString("Status").equals("Success")) {
//                sendToList = Gson().fromJson<java.util.ArrayList<SendToModel>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<List<DocumentTypeModel>>() {}.type);
//                sendToArray = arrayOfNulls<String>(sendToList.size)
//                for (i in sendToList.indices) {
//                    //Storing names to string array
//                    sendToArray!![i] = sendToList.get(i).deptName
//                }
//            }
//        }
    }

    fun showDropdown(array: Array<String?>, spinnerData: SpinnerData, textView: Ubuntu, width: Int) {
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow!!.setAdapter(ArrayAdapter<Any?>(
                this,
                R.layout.row_profile_spinner, array))
        listPopupWindow!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_white_background_no_radius_border))
        listPopupWindow!!.anchorView = textView
        listPopupWindow!!.width = width
        listPopupWindow!!.height = 700
        listPopupWindow!!.isModal = true
        listPopupWindow!!.setOnItemClickListener { parent, view, position, id ->

//            if (textView.id == R.id.tvRequestToAdvanceClaim) {
//                spinnerData.getData(sendToList.get(position).deptID, sendToList.get(position).deptName)
//
//            }


            textView.text = array!![position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
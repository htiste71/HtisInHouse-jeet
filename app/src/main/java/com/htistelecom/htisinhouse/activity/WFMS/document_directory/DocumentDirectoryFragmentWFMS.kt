package com.htistelecom.htisinhouse.activity.WFMS.document_directory


import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.Utils.UtilitiesWFMS
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.adapters.DocumentDirectoryCommonAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.models.DocumentListModelWFMS
import com.htistelecom.htisinhouse.activity.WFMS.fragments.BaseFragmentCamera
import com.htistelecom.htisinhouse.activity.WFMS.models.TwoParameterModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.font.Ubuntu
import com.htistelecom.htisinhouse.font.UbuntuEditText
import com.htistelecom.htisinhouse.interfaces.SpinnerData
import com.htistelecom.htisinhouse.retrofit.MyInterface
import com.htistelecom.htisinhouse.utilities.DateUtils
import com.htistelecom.htisinhouse.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_document_directory.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import org.json.JSONObject
import retrofit2.Response
import java.io.File

import kotlin.collections.ArrayList


class DocumentDirectoryFragmentWFMS : BaseFragmentCamera(), View.OnClickListener, MyInterface {


    lateinit var dialog: Dialog
    private var imgReqBody: MultipartBody.Part? = null
    private var img1ReqBody: MultipartBody.Part? = null

    private var ivUploadImg1UploadDoc: ImageView? = null
    private var ivUploadImgUploadDoc: ImageView? = null

    private var ivAddHeader: ImageView? = null
    private var IMGVIEW: Int = -1


    lateinit var adapter: DocumentDirectoryCommonAdapterWFMS
    lateinit var dialogRequestDoc: Dialog
    private var mEmpIdToSend: String = ""
    private lateinit var listPopupWindow: ListPopupWindow

    private var documentList = ArrayList<DocumentListModelWFMS>()
    var documentTypeList = ArrayList<TwoParameterModel>()
    lateinit var documentTypeArray: Array<String?>
    var departmentList = ArrayList<TwoParameterModel>()
    lateinit var departmentArray: Array<String?>

    lateinit var tinyDB: TinyDB


    var mCurrentDate = ""
    var DOC_TYPE = 0
    var REQUESTED_DOC = 1
    var UPLOADED_DOC = 2
    var DOWNLOAD_DOC = 3
    var imagesAl = java.util.ArrayList<MultipartBody.Part>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_document_directory, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun initViews() {
        tinyDB = TinyDB(activity)
        tinyDB = TinyDB(activity)
        ivAddHeader = activity!!.findViewById(R.id.ivAddHeader) as ImageView

        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        mCurrentDate = DateUtils.currentDate();

        ivAddHeader!!.visibility = VISIBLE
        hideShowSpinner(false, resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorDarkBlue))
        DOC_TYPE = REQUESTED_DOC
        hitAPI(DOCUMENTS_WFMS, "")


    }

    private fun listeners() {
        tvRequestedDocument.setOnClickListener(this)
        tvDefaultDocument.setOnClickListener(this)
        tvUploadedDocument.setOnClickListener(this)
        //  tvDocumentTypeSpinner.setOnClickListener(this)
        ivAddHeader!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvRequestedDocument -> {

                hideShowSpinner(false, resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorDarkBlue))


                DOC_TYPE = REQUESTED_DOC
                hitAPI(DOCUMENTS_WFMS, "")

                ivAddHeader!!.visibility = View.VISIBLE


            }
            R.id.tvDefaultDocument -> {
                hideShowSpinner(true, resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorDarkBlue))


                DOC_TYPE = DOWNLOAD_DOC
                hitAPI(DOCUMENTS_WFMS, "")


                ivAddHeader!!.visibility = View.GONE


            }
            R.id.tvUploadedDocument -> {
                hideShowSpinner(false, resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorOrange))

                DOC_TYPE = UPLOADED_DOC
                hitAPI(DOCUMENTS_WFMS, "")


                ivAddHeader!!.visibility = View.GONE


            }


            R.id.ivAddHeader -> {
                if (DOC_TYPE == REQUESTED_DOC) {
                    dialogRequestDocument()
                }

            }
        }

    }


    fun dialogUploadDoc(docRequestId: String,docName:String) {


         dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_upload_document_wfms)
        dialog.setCancelable(false)
        dialog.show()
        val btnSubmitUploadDoc = dialog.findViewById(R.id.btnSubmitUploadDoc) as Button
        val btnCancelUploadDoc = dialog.findViewById(R.id.btnCancelUploadDoc) as Button
        val rlSelectDateUploadDoc = dialog.findViewById(R.id.rlSelectDateUploadDoc) as RelativeLayout
        val tvSelectDateUploadDoc = dialog.findViewById(R.id.tvSelectDateUploadDoc) as Ubuntu

     //   val rlDocumentTypeSpinnerUploadDoc = dialog.findViewById(R.id.rlDocumentTypeSpinnerUploadDoc) as RelativeLayout
        val tvDocumentTypeUploadDoc = dialog.findViewById(R.id.tvDocumentTypeUploadDoc) as Ubuntu

     //   val rlSendToSpinnerUploadDoc = dialog.findViewById(R.id.rlSendToSpinnerUploadDoc) as RelativeLayout
        val tvSendToUploadDoc = dialog.findViewById(R.id.tvSendToUploadDoc) as Ubuntu

        val etRemarksUploadDoc = dialog.findViewById(R.id.etRemarksUploadDoc) as UbuntuEditText
        ivUploadImgUploadDoc = dialog.findViewById(R.id.ivUploadImgUploadDoc) as ImageView
        ivUploadImg1UploadDoc = dialog.findViewById(R.id.ivUploadImg1UploadDoc) as ImageView

        var mSendTo = ""
        var mDocumentType = ""
        tvSelectDateUploadDoc.text = mCurrentDate
        tvDocumentTypeUploadDoc.text=docName


        btnCancelUploadDoc.setOnClickListener { view -> dialog.dismiss() }

//        rlSendToSpinnerUploadDoc.setOnClickListener { view ->
//
//
//            showDropdown(departmentArray, object : SpinnerData {
//                override fun getData(mId: String, mName: String) {
//                }
//            }, tvSendToUploadDoc, 700)
//        }
        ivUploadImgUploadDoc!!.setOnClickListener { view ->

            IMGVIEW = 0
            checkPermissions("com.htistelecom.htisinhouse.activity.NewKnjrkhana.document_directory.DocumentDirectoryFragmentWFMS", activity!!)


        }

        ivUploadImg1UploadDoc!!.setOnClickListener { view ->
            IMGVIEW = 1
            checkPermissions("com.htistelecom.htisinhouse.activity.NewKnjrkhana.document_directory.DocumentDirectoryFragmentWFMS", activity!!)
        }


//        rlDocumentTypeSpinnerUploadDoc.setOnClickListener { view ->
//            showDropdown(documentTypeArray, object : SpinnerData {
//                override fun getData(mId: String, mName: String) {
//                    mDocumentType=mId
//                }
//            }, tvDocumentTypeUploadDoc, 700)
//        }
        btnSubmitUploadDoc.setOnClickListener { view ->
            val mRemarks = etRemarksUploadDoc.text.toString()
            if(imagesAl.size==0)
            {
                UtilitiesWFMS.showToast(activity!!,resources.getString(R.string.errImages))
            }
            else {
                var parts: Array<MultipartBody.Part?>? = null
                if (imagesAl.size > 0) {
                    parts = arrayOfNulls(imagesAl.size)
                    imagesAl.toArray(parts)
                }
                var obj = JSONObject()
                obj.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
                obj.put("RequestId", docRequestId)
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = obj.toString().toRequestBody(mediaType)
                ApiData.forImageDataArray(parts, requestBody, UPLOAD_DOCUMENT_WFMS, this, activity)
            }


        }


    }

    fun dialogRequestDocument() {

        var mDocTypeID = ""
        var mDeptId = ""
        dialogRequestDoc = Dialog(activity)
        dialogRequestDoc.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRequestDoc.setContentView(R.layout.dialog_request_document_wfms)
        dialogRequestDoc.setCancelable(false)
        dialogRequestDoc.show()

            hitAPI(DOCUMENT_TYPE_WFMS, "")



        val btnSubmitRequestedDoc = dialogRequestDoc.findViewById(R.id.btnSubmitRequestedDoc) as Button
        val btnCancelRequestedDoc = dialogRequestDoc.findViewById(R.id.btnCancelRequestedDoc) as Button
        val rlSelectDateSpinnerRequestedDoc = dialogRequestDoc.findViewById(R.id.rlSelectDateSpinnerRequestedDoc) as RelativeLayout
        val tvSelectDateRequestedDoc = dialogRequestDoc.findViewById(R.id.tvSelectDateRequestedDoc) as Ubuntu

        val rlDocumentTypeSpinnerRequestedDoc = dialogRequestDoc.findViewById(R.id.rlDocumentTypeSpinnerRequestedDoc) as RelativeLayout
        val tvDocumentTypeRequestedDoc = dialogRequestDoc.findViewById(R.id.tvDocumentTypeRequestedDoc) as Ubuntu

        val rlRequestToSpinnerRequestedDoc = dialogRequestDoc.findViewById(R.id.rlRequestToSpinnerRequestedDoc) as RelativeLayout
        val tvDocumentRequestToRequestedDoc = dialogRequestDoc.findViewById(R.id.tvDocumentRequestToRequestedDoc) as Ubuntu

        val etReasonRequestedDoc = dialogRequestDoc.findViewById(R.id.etReasonRequestedDoc) as UbuntuEditText

        tvSelectDateRequestedDoc.text = mCurrentDate
        rlRequestToSpinnerRequestedDoc.setOnClickListener { view ->
            showDropdown(departmentArray, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mDeptId = mId
                }
            }, tvDocumentRequestToRequestedDoc, 700)
        }

        rlDocumentTypeSpinnerRequestedDoc.setOnClickListener { view ->
            showDropdown(documentTypeArray, object : SpinnerData {
                override fun getData(mId: String, mName: String) {
                    mDocTypeID = mId
                }
            }, tvDocumentTypeRequestedDoc, 700)
        }

        btnCancelRequestedDoc.setOnClickListener { view ->
            dialogRequestDoc.dismiss()
        }

        btnSubmitRequestedDoc.setOnClickListener { view ->

            var mMessage = etReasonRequestedDoc.text.toString()
            var obj = JSONObject()
            obj.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
            obj.put("DocTypeId", mDocTypeID)
            obj.put("EmpRemarks", mMessage)
            obj.put("ReqToEmp", mDeptId)



            hitAPI(REQUEST_DOCUMENT_WFMS, obj.toString())


        }


    }

    fun showDropdown(array: Array<String?>, spinnerData: SpinnerData, textView: Ubuntu, width: Int) {
        listPopupWindow = ListPopupWindow(activity)
        listPopupWindow!!.setAdapter(ArrayAdapter<Any?>(
                activity,
                R.layout.row_profile_spinner, array))
        listPopupWindow!!.setBackgroundDrawable(resources.getDrawable(R.drawable.rect_white_background_no_radius_border))
        listPopupWindow!!.anchorView = textView
        listPopupWindow!!.width = width
        listPopupWindow!!.height = 700
        listPopupWindow!!.isModal = true
        listPopupWindow!!.setOnItemClickListener { parent, view, position, id ->

            if (textView.id == R.id.tvDocumentTypeRequestedDoc || textView.id == R.id.tvSendToUploadDoc) {
                spinnerData.getData(documentTypeList.get(position).id, documentTypeList.get(position).name)
            } else if (textView.id == R.id.tvDocumentRequestToRequestedDoc || textView.id==R.id.tvDocumentTypeUploadDoc) {
                spinnerData.getData(departmentList.get(position).id, departmentList.get(position).name)
                // mEmpIdToSend = alSendTo.get(position).empID

            }



            textView.text = array!![position]
            listPopupWindow!!.dismiss()
        }
        listPopupWindow!!.show()
    }

    fun hideShowSpinner(isShow: Boolean, color1: Int, color2: Int, color3: Int) {
//        if (isShow)
//            rlDocumentSpinner.visibility = View.VISIBLE
//        else
//            rlDocumentSpinner.visibility = View.GONE

        tvRequestedDocument.setTextColor(color1)
        tvDefaultDocument.setTextColor(color2)
        tvUploadedDocument.setTextColor(color3)

    }


    private fun hitAPI(type: Int, params: String) {
        when (type) {
            ConstantsWFMS.DOCUMENTS_WFMS -> {

                val json = JSONObject()
                json.put("EmpId", tinyDB.getString(TINYDB_EMP_ID))
                if (DOC_TYPE == REQUESTED_DOC) {

                    json.put("RequestType", "1")


                } else if (DOC_TYPE == UPLOADED_DOC) {
                    json.put("RequestType", "2")

                } else {
                    json.put("RequestType", "3")

                }
                //  json.put("Search", "")

                ApiData.getData(json.toString(), DOCUMENTS_WFMS, this, activity)
            }


            DOCUMENT_TYPE_WFMS -> {
                ApiData.getGetData(DOCUMENT_TYPE_WFMS, this, activity)

            }
            DEPARTMENT_LIST_WFMS -> {
                ApiData.getGetData(DEPARTMENT_LIST_WFMS, this, activity)

            }
            REQUEST_DOCUMENT_WFMS -> {
                ApiData.getData(params, REQUEST_DOCUMENT_WFMS, this, activity)

            }
            UPLOAD_DOCUMENT_WFMS -> {
                ApiData.getData(params, UPLOAD_DOCUMENT_WFMS, this, activity)

            }

        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {

        if(TYPE!= DOCUMENT_TYPE_WFMS)
        Utilities.dismissDialog()
        when (TYPE) {

            DOCUMENTS_WFMS -> {

                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    documentList.clear()
                    documentList = Gson().fromJson<java.util.ArrayList<DocumentListModelWFMS>>(jsonObject.getJSONArray("Output").toString(), object : TypeToken<ArrayList<DocumentListModelWFMS>>() {}.type);


                    if (documentList.size > 0) {


                        tvNoRecordDocument.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        if (DOC_TYPE == REQUESTED_DOC) {


                            adapter = DocumentDirectoryCommonAdapterWFMS(activity!!, documentList, 0,this@DocumentDirectoryFragmentWFMS)
                            recyclerView.adapter = adapter


                        } else if (DOC_TYPE == UPLOADED_DOC) {
                            adapter = DocumentDirectoryCommonAdapterWFMS(activity!!, documentList, 2, this)
                            recyclerView.adapter = adapter
                        } else {
                            adapter = DocumentDirectoryCommonAdapterWFMS(activity!!, documentList, 1, this)
                            recyclerView.adapter = adapter
                        }
                    }


                } else {
                    tvNoRecordDocument.visibility = View.VISIBLE
                    recyclerView.visibility = GONE
                }

            }

            DOCUMENT_TYPE_WFMS -> {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    val jsonArray = jsonObject.getJSONArray("Output")
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)

                        val model = TwoParameterModel()
                        model.id = item.getString("DocumentTypeId")
                        model.name = item.getString("DocumentTypeName")
                        documentTypeList.add(model)
                    }
                }
                documentTypeArray = arrayOfNulls<String>(documentTypeList.size)
                for (i in documentTypeList.indices) {
                    documentTypeArray!![i] = documentTypeList.get(i).name
                }
                hitAPI(DEPARTMENT_LIST_WFMS, "")
            }
            DEPARTMENT_LIST_WFMS -> {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    val jsonArray = jsonObject.getJSONArray("Output")
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)

                        val model = TwoParameterModel()
                        model.id = item.getString("DepartmentId")
                        model.name = item.getString("DepartmentName")
                        departmentList.add(model)
                    }
                }
                departmentArray = arrayOfNulls<String>(departmentList.size)
                for (i in departmentList.indices) {
                    departmentArray!![i] = departmentList.get(i).name
                }
            }
            REQUEST_DOCUMENT_WFMS -> {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    dialogRequestDoc.dismiss()
                    hideShowSpinner(false, resources.getColor(R.color.colorOrange), resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorDarkBlue))

                    DOC_TYPE = REQUESTED_DOC
                    hitAPI(DOCUMENTS_WFMS, "")
                }
            }
            UPLOAD_DOCUMENT_WFMS -> {
                val jsonObject = JSONObject((response as Response<*>).body()!!.toString())
                if (jsonObject.getString("Status").equals("Success")) {
                    Utilities.showToast(activity, jsonObject.getString("Message"))
                    hideShowSpinner(false, resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorDarkBlue), resources.getColor(R.color.colorOrange))

                    DOC_TYPE = UPLOADED_DOC
                    hitAPI(DOCUMENTS_WFMS, "")
                }
                dialog.dismiss()


            }


        }
    }

    fun imagePath(file: File) {
        if (IMGVIEW == 0) {

            Glide.with(activity!!).load(file).into(ivUploadImgUploadDoc!!)
            val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
            imgReqBody = MultipartBody.Part.createFormData("file", file.name, mFile)
            imagesAl.add(imgReqBody!!)
        } else if (IMGVIEW == 1) {
            Glide.with(activity!!).load(file).into(ivUploadImg1UploadDoc!!);
            val mFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
             img1ReqBody = MultipartBody.Part.createFormData("file", file.name, mFile)
            imagesAl.add(img1ReqBody!!)
        }

    }

}


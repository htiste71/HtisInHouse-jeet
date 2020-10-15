package com.htistelecom.htisinhouse.activity.WFMS.document_directory


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.ApiData
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS.*
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ImageFileUtils.TAG
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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_document_directory.*
import kotlinx.android.synthetic.main.fragment_profile_wfms.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DocumentDirectoryFragmentWFMS : Fragment(), View.OnClickListener, MyInterface {


    lateinit var manager: DownloadManager
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

    lateinit var ctx: Context
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_document_directory, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        listeners()


    }

    private fun initViews() {
        progressDialog = ProgressDialog(activity);

        manager = activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

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


    fun dialogUploadDoc(docRequestId: String, docName: String) {


        dialog = Dialog(activity!!)
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
        tvDocumentTypeUploadDoc.text = docName


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
            checkPermissions()


        }

        ivUploadImg1UploadDoc!!.setOnClickListener { view ->
            IMGVIEW = 1
            checkPermissions()
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
            if (imagesAl.size == 0) {
                UtilitiesWFMS.showToast(activity!!, resources.getString(R.string.errImages))
            } else {
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
        dialogRequestDoc = Dialog(activity!!)
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

            if (textView.id == R.id.tvDocumentTypeRequestedDoc || textView.id == R.id.tvSendToUploadDoc) {
                spinnerData.getData(documentTypeList.get(position).id, documentTypeList.get(position).name)
            } else if (textView.id == R.id.tvDocumentRequestToRequestedDoc || textView.id == R.id.tvDocumentTypeUploadDoc) {
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

        if (TYPE != DOCUMENT_TYPE_WFMS)
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


                            adapter = DocumentDirectoryCommonAdapterWFMS(activity!!, documentList, 0, this@DocumentDirectoryFragmentWFMS)
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
                } else {
                    UtilitiesWFMS.showToast(activity!!, jsonObject.getString("Message"))
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

    fun downloadPDF(docUrl: String, docName: String) {
        val docArray = docName.split(",")
      //  DownloadPDF("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf", manager, progressDialog, activity).execute()

        for (str in docArray) {
            DownloadPDF(docUrl + str, manager, progressDialog, activity).execute()

        }
    }

    fun downloadImage(docUrl: String, docName: String) {
        val docArray = docName.split(",")
        for (str in docArray) {
            Downloading(docUrl + str, manager, progressDialog, activity).execute()

        }
    }

    class Downloading(url: String, manager: DownloadManager, progressDialog: ProgressDialog, activity: FragmentActivity?) : AsyncTask<Void, Void, Void>() {
        private var mImagePathString=""
        lateinit var imgPath: File
        var mUrl = url
        var mManager: DownloadManager = manager
        var progressDialog = progressDialog
        var activity = activity


        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        override fun doInBackground(vararg p0: Void): Void? {
            val mydir = File(Environment.getExternalStorageDirectory().toString() + "/MyDocuments")
            if (!mydir.exists()) {
                mydir.mkdirs()
            }
            val downloadUri: Uri = Uri.parse(mUrl)
            val request: DownloadManager.Request = DownloadManager.Request(downloadUri)
         //   val dateFormat = SimpleDateFormat("mmddyyyyhhmmss")
            imgPath = File(Environment.getExternalStorageDirectory().toString() + "/" + "MyDocuments")
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir("/MyDocuments", "$imgPath.jpg")
            mImagePathString=imgPath.absolutePath

            mManager.enqueue(request)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            UtilitiesWFMS.showToast(activity!!, "Download finish")
           // val uri = Uri.fromFile(File(mImagePathString))


            val intent = Intent(Intent.ACTION_VIEW)
            if(mUrl.contains("png",true))
            {
                intent.setDataAndType(Uri.parse(mUrl), "image/png");

            }
            else if(mUrl.contains("jpeg",true))
            {
                intent.setDataAndType(Uri.parse(mUrl), "image/jpeg");

            }
            else if(mUrl.contains("jpg",true))
            {
                intent.setDataAndType(Uri.parse(mUrl), "image/jpg");

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity!!.startActivity(intent);

            progressDialog.dismiss()
        }


    }

    private class DownloadPDF(url: String, manager: DownloadManager, mDialog: ProgressDialog, activity: FragmentActivity?) : AsyncTask<Void?, Void?, Void?>() {
        var mUrl = url
        var mManager = manager
        var progressDialog = mDialog
        var mActivity = activity
        var apkStorage: File? = null
        var outputFile: File? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(mActivity)
            progressDialog.setMessage("Downloading...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun onPostExecute(result: Void?) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss()

                    Toast.makeText(mActivity, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mUrl))
                    mActivity!!.startActivity(browserIntent)
//                    val path = Uri.fromFile(outputFile)
//                    var pdfOpenintent = Intent(Intent.ACTION_VIEW)
//                  //  pdfOpenintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    pdfOpenintent.setDataAndType(path, "application/pdf")
//
//                    try {
//                        mActivity!!.startActivity(pdfOpenintent)
//
//                    } catch (e: ActivityNotFoundException) {
//                        UtilitiesWFMS.showToast(mActivity!!,"No App found to open PDF")
//                    }
                } else {

                }
            } catch (e: Exception) {
                e.printStackTrace()


            }
            super.onPostExecute(result)
        }


        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val url = URL(mUrl) //Create Download URl
                val c: HttpURLConnection = url.openConnection() as HttpURLConnection //Open Url Connection
                c.setRequestMethod("GET") //Set Request Method to "GET" since we are grtting data
                c.connect() //connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() !== HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            .toString() + " " + c.getResponseMessage())
                }


                //Get File if SD card is present

                apkStorage = File(Environment.getExternalStorageDirectory().toString() + "/" + "MyDocuments")
                val mFile = mUrl.substring(mUrl.lastIndexOf('/'), mUrl.length);
                //If File is not present create directory
                if (!apkStorage!!.exists()) {
                    apkStorage!!.mkdir()
                    Log.e(TAG, "Directory Created.")
                }
                outputFile = File(apkStorage, mFile) //Create Output file in Main File

                //Create New File if not present
                if (!outputFile!!.exists()) {
                    outputFile!!.createNewFile()
                    Log.e(TAG, "File Created")
                }
                val fos = FileOutputStream(outputFile) //Get OutputStream for NewFile Location
                val `is`: InputStream = c.getInputStream() //Get InputStream for connection
                val buffer = ByteArray(1024) //Set buffer type
                var len1 = 0 //init length
                while (`is`.read(buffer).also({ len1 = it }) != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }

                //Close all connection after doing task
                fos.close()
                `is`.close()
            } catch (e: Exception) {

                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e(TAG, "Download Error Exception " + e.message)
            }
            return null
        }
    }



    public fun checkPermissions() {
        if(Build.VERSION.SDK_INT<=23)
        {
            if(ContextCompat.checkSelfPermission(activity!!,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity!!,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),1);
                return;
            } else {
                onSelectImageClick()

            }
        }
        else{
            onSelectImageClick()

        }



//        Dexter.with(activity)
//                .withPermissions(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//
//                ).withListener(object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                        if (report.areAllPermissionsGranted()) {
//                            onSelectImageClick()
//                        }
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
//                        token.continuePermissionRequest()
//                    }
//                }).check()


    }



    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size > 0) {
            val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (locationAccepted) {
                CropImage.startPickImageActivity(activity!!)

                //startLocationClass()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),1);


                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                var s = result.getUri()
                val file=File(File(result.uri.path).absolutePath)
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






                //sendImage(File(File(result.uri.path).absolutePath))
                //  fragmentImage.setImageURI(s)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

    fun callMethod(imageUri: Uri) {
        startCropImageActivity(imageUri)

    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(activity!!, this)
    }



    fun onSelectImageClick() {
        CropImage.startPickImageActivity(activity!!)
    }




}


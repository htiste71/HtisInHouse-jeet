package com.htistelecom.htisinhouse.activity.WFMS.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.FileModel
import com.htistelecom.htisinhouse.config.TinyDB
import com.htistelecom.htisinhouse.retrofit.ApiClient
import com.htistelecom.htisinhouse.retrofit.ApiInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadDataServerService : Service() {
lateinit var db:TinyDB
    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        hitAPI()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        db= TinyDB(this)

        // hitAPI()
    }

    fun hitAPI() {
        val dateObj = Date()
        val dFormat = SimpleDateFormat("dd-MMM-yyyy")
        val cDate = dFormat.parse(dFormat.format(dateObj))
        var alFile = Gson().fromJson<ArrayList<FileModel>>(db.getString(ConstantsWFMS.TINYDB_MYFILE), object : TypeToken<ArrayList<FileModel>>() {}.type)
      // db.putString(ConstantsWFMS.TINYDB_MYFILE,"")
        if (alFile != null) {
            if (alFile.size > 0) {
                for ((index, value) in alFile.withIndex()) {

                    val model = value
                    val fDate = dFormat.parse(model.fileDate)
                    if (!cDate.equals(fDate))
                    {


                        val file = File(model.filepath)
                        val mFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
                        val json = JSONObject()
                        json.put("EmpId", value.id)
                        json.put("LatLongDate", value.fileDate)


                        val data = RequestBody.create("text/plain".toMediaTypeOrNull(), json.toString())
                        val apiService = ApiClient.getClient(db).create(ApiInterface::class.java)
                        val call = apiService.methodUploadLogFileWFMS(fileToUpload, data)

                        call.enqueue(object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                //int statusCode = response.code();
                                try {
                                    Log.e("res", response.toString())


                                    val jsonObject = Gson().toJsonTree(response.body()).asJsonObject
                                    if (jsonObject.get("Status").asString.equals("Success", ignoreCase = true)) {
                                        val file = File(model.filepath);
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                        alFile.removeAt(0)
                                        val mydb = TinyDB(this@UploadDataServerService)

                                        mydb.putString(ConstantsWFMS.TINYDB_MYFILE, Gson().toJson(alFile))
                                      //  TinyDB(this@UploadDataServerService).putBoolean("isUploaded", true)
                                    } else {

                                    }


                                } catch (ex: Exception) {
                                    Log.e("res", ex.message)

                                }

                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {
                                // Log error here since request failed
                                Log.e("", "error =$t")

                            }
                        })
                    }

                }
            } else {

            }
        }
    }


  }









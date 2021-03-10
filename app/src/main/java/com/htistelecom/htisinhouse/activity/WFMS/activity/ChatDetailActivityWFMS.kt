package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.MyApplication
import com.htistelecom.htisinhouse.activity.WFMS.Utils.ConstantsWFMS
import com.htistelecom.htisinhouse.activity.WFMS.adapters.ChatDetailAdapterWFMS
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import com.htistelecom.htisinhouse.config.TinyDB
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat_detail_wfms.*
import kotlinx.android.synthetic.main.toolbar_chat_detail.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject

class ChatDetailActivityWFMS : Activity() {

    lateinit var db: TinyDB
    lateinit var adapter: ChatDetailAdapterWFMS
    var mOtherId = ""
    var myId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail_wfms)
        initViews()
        initIntentData()







        ivBackChatDetailWFMS.setOnClickListener { view -> finish() }

        ivSendChatDetailActivityWFMS.setOnClickListener { v ->


            MyApplication.mSocket!!.on("send_message", Emitter.Listener {

                args ->
                val data = args[0] as JSONObject


//                runOnUiThread(Runnable {
                val message = etTextChatDetailActivityWFMS.text.toString()
                val jsonObject = JSONObject()
                jsonObject.put("sender", db.getString(ConstantsWFMS.TINYDB_EMP_ID))
                jsonObject.put("receiver", mOtherId)

                jsonObject.put("message", message)
                adapter.addNewMessage(myId, mOtherId, message, true)
                MyApplication.mSocket!!.send("new_message", jsonObject.toString())
            })
//            })

//                    private val onSendNewMessage = Emitter.Listener { args ->
//                runOnUiThread(Runnable {
//                    val message = etTextChatDetailActivityWFMS.text.toString()
//                    val jsonObject = JSONObject()
//                    jsonObject.put("sender", db.getString(ConstantsWFMS.TINYDB_EMP_ID))
//                    jsonObject.put("receiver", mOtherId)
//
//                    jsonObject.put("message", message)
//                    adapter.addNewMessage(myId, mOtherId, message, true)
//                    MyApplication.mSocket!!.send("new_message", jsonObject.toString())
//                })
//            })


        }

    }

    private fun initViews() {
        db = TinyDB(this)

        MyApplication.mSocket!!.on("new_message", onReceiveNewMessage)



        myId = db.getString(ConstantsWFMS.TINYDB_EMP_ID)

        rvChatDetailActivityWFMS.layoutManager = LinearLayoutManager(this, VERTICAL, false)
    }

    private fun initIntentData() {
        val teamModel = intent.getSerializableExtra("model") as? MyTeamModel
        tvTitleChatDetailWFMS.text = teamModel!!.empName
        mOtherId = teamModel.empId
        if (teamModel.empImg.equals("")) {
            Glide.with(this).load(R.drawable.icon_man).into(ivProfilePicChatDetailWFMS);

        } else {
            Glide.with(this).load(teamModel.empImgPath + teamModel.empImg).into(ivProfilePicChatDetailWFMS);


        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //    private val onSendNewMessage = Emitter.Listener { args ->
//        runOnUiThread(Runnable {
//            val message = etTextChatDetailActivityWFMS.text.toString()
//            val jsonObject = JSONObject()
//            jsonObject.put("sender", db.getString(ConstantsWFMS.TINYDB_EMP_ID))
//            jsonObject.put("receiver", mOtherId)
//
//            jsonObject.put("message", message)
//            adapter.addNewMessage(myId, mOtherId, message, true)
//            MyApplication.mSocket!!.send("new_message", jsonObject.toString())
//        })
//    }
    private val onReceiveNewMessage = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val message = etTextChatDetailActivityWFMS.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("sender", db.getString(ConstantsWFMS.TINYDB_EMP_ID))
            jsonObject.put("receiver", mOtherId)

            jsonObject.put("message", message)
            adapter.addNewMessage(myId, mOtherId, message, false)
            MyApplication.mSocket!!. send("new_message", jsonObject.toString())
        })
    }

}
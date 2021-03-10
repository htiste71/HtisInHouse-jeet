package com.htistelecom.htisinhouse.activity.WFMS.Utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

import java.net.URISyntaxException

object SingletonClass {
    var mSocket: Socket?=null

    fun connect() {


        try {
            mSocket = IO.socket("http://chatserver.htistelecom.in")
            mSocket!!.on(Socket.EVENT_CONNECT, object : Emitter.Listener {
             override   fun call(vararg args: Any?) {
                    Log.e("Coonect", "Connected")
                }
            })

            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, object : Emitter.Listener {
               override fun call(vararg args: Any?) {
                    Log.e("Coonect Error", "Connected Error")
                }
            })
        } catch (e: URISyntaxException) {
            Log.e("jags", e.message)
        }
    }

}
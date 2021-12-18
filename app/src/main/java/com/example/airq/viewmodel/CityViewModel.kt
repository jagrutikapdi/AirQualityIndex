package com.example.airq.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airq.Status
import com.example.airq.model.Data
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit


class CityViewModel() : ViewModel() {

    private val mClient: OkHttpClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
    var activity: Activity? = null
    var webSocket: WebSocket? = null
    lateinit var listener: MyWebSocket
    private val logTag = javaClass.simpleName
    private var mApiResponse: MutableLiveData<List<Data>>? = null
    var mStatus = MutableLiveData<Status>()
    val request: Request

    init {
        mApiResponse = MutableLiveData()
        listener = MyWebSocket(this)
        request = Request.Builder().url("ws://city-ws.herokuapp.com/").build()
    }


    val status: LiveData<Status>
        get() = mStatus
    val response: LiveData<List<Data>>
        get() {
            return mApiResponse!!
        }

    fun setActivityContext(activity: Activity) {
        this.activity = activity
    }

    fun startSocket() {
        try {
            mStatus.value = Status.FETCHING
            webSocket = mClient.newWebSocket(request, listener)

        } catch (e: Exception) {
            mStatus.value = Status.ERROR
            webSocket?.close(1000, "Socket Closed !!")
            e.printStackTrace()
        }
    }

    class MyWebSocket(internal var cityViewModel: CityViewModel) : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {

        }


        override fun onMessage(webSocket: WebSocket, message: String) {
            Log.d(cityViewModel.logTag, message)
            cityViewModel.activity?.runOnUiThread(Runnable {
                val gson = GsonBuilder().create()
                val cityList: List<Data> = gson.fromJson(
                    message,
                    object : TypeToken<List<Data?>?>() {}.getType()
                ) as List<Data>
                cityViewModel.mApiResponse?.value = cityList
                cityViewModel.mStatus.value = Status.DONE
            })
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.i(cityViewModel.logTag, "Receive Bytes : " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            Log.i(cityViewModel.logTag, "Closing Socket : $code / $reason")
        }

        override fun onFailure(
            webSocket: WebSocket,
            throwable: Throwable,
            response: okhttp3.Response
        ) {
            Log.e(cityViewModel.logTag, "Error : " + throwable.message)
            cityViewModel.mStatus.value = Status.ERROR
        }
    }
}
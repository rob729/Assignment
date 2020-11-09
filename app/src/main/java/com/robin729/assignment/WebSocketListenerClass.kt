package com.robin729.assignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.robin729.assignment.model.Result
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListenerClass() : WebSocketListener() {

    private val NORMAL_CLOSURE_STATUS = 1000
    private val _transactionData =  MutableLiveData<Result>()
    private val _connectionState = MutableLiveData<String>()

    val transactionData: LiveData<Result>
        get() = _transactionData

    val connectionState: LiveData<String>
        get() = _connectionState

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        webSocket.send("{\"op\":\"unconfirmed_sub\"}")
        _connectionState.postValue("Connected")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val result = Gson().fromJson(text, Result::class.java)
        _transactionData.postValue(result)
    }


    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        _connectionState.postValue("Disconnected")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.e("TAG", "connection failure ${t.localizedMessage}")
    }
}
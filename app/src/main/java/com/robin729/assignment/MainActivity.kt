package com.robin729.assignment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.robin729.assignment.databinding.ActivityMainBinding
import com.robin729.assignment.model.Result
import com.robin729.assignment.network.ConvRatioAqi
import com.robin729.assignment.util.StoreSession
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    private val webSocketListenerClass: WebSocketListenerClass = WebSocketListenerClass()

    private val convRatio: Float by lazy {
        StoreSession.readFloat("CONV_RATIO")
    }

    private val transactionDetailsListAdapter: TransactionDetailsListAdapter by lazy{
        TransactionDetailsListAdapter()
    }

    private lateinit var activityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        activityMainBinding =  DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        activityMainBinding.listRV.adapter = transactionDetailsListAdapter

        activityMainBinding.clearButton.setOnClickListener {
            transactionDetailsListAdapter.transactionList.clear()
            transactionDetailsListAdapter.notifyDataSetChanged()
        }

        GlobalScope.launch{
            getConvRatio()
        }
        startSocket()

        webSocketListenerClass.transactionData.observe(this, { result ->
            var totalAmount = 0.00
            result.transDetails.out.forEach {
                totalAmount += it.value
            }
            totalAmount /= 10.00.pow(8.00)
            totalAmount /= convRatio

            if (totalAmount > 100) {
                if (transactionDetailsListAdapter.transactionList.size == 5) {
                    transactionDetailsListAdapter.transactionList.removeAt(0)
                }
                transactionDetailsListAdapter.transactionList.add(result)
                transactionDetailsListAdapter.notifyDataSetChanged()
            }
        })

        webSocketListenerClass.connectionState.observe(this, {
            activityMainBinding.statusTxt.text = "Conenction Status: $it"
        })
    }

    private fun startSocket(){
        val request: Request = Request.Builder().url("wss://ws.blockchain.info/inv").build()
        val webSocket = client.newWebSocket(request, webSocketListenerClass)
        client.dispatcher().executorService().shutdown()
        webSocket.send("\"op\":\"unconfirmed_sub\"")

    }


    private suspend fun getConvRatio() = withContext(Dispatchers.IO){
        return@withContext try {
            val response = ConvRatioAqi.retrofitService.getConvRatio()
            if(response.isSuccessful){
                StoreSession.writeFloat("CONV_RATIO", response.body()?: 0.0000645f)
            } else {
                Log.e("TAG", "error in fetching conversion ratio")
            }
        } catch (e: Exception){
            Log.e("TAG", "error in fetching conversion ratio")
        }
    }
}
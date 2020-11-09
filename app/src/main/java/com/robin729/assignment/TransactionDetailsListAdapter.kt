package com.robin729.assignment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.robin729.assignment.model.Result
import com.robin729.assignment.util.StoreSession
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

class TransactionDetailsListAdapter :
    RecyclerView.Adapter<TransactionDetailsListAdapter.ViewHolder>() {


    var transactionList = ArrayList<Result>()
    private val dateFormat = SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss")
    private val timeZone: TimeZone by lazy {
        Calendar.getInstance().timeZone
    }
    private val convRatio: Float by lazy {
        StoreSession.readFloat("CONV_RATIO")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionDetailsListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TransactionDetailsListAdapter.ViewHolder, position: Int) {
        val item = transactionList[position]
        val timeValueTxt = holder.itemView.findViewById<TextView>(R.id.timeValueTxt)
        val amountValueTxt = holder.itemView.findViewById<TextView>(R.id.amountValueTxt)
        val hashValueTxt = holder.itemView.findViewById<TextView>(R.id.hashValueTxt)
        dateFormat.timeZone = timeZone
        timeValueTxt.text = dateFormat.format(Date(item.transDetails.time*1000))
        hashValueTxt.text = item.transDetails.hash
        var totalAmount: Double = 0.00
        item.transDetails.out.forEach {
            totalAmount += it.value
        }
        totalAmount /= 10.00.pow(8.00)
        totalAmount /= convRatio

        amountValueTxt.text = "$ ${totalAmount.toBigDecimal().setScale(3, RoundingMode.UP)}"

    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    }

    override fun getItemCount(): Int  = transactionList.size

}
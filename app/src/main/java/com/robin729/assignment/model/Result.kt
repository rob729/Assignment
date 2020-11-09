package com.robin729.assignment.model

import com.google.gson.annotations.SerializedName

data class Result(val op: String, @SerializedName("x")val transDetails: TransDetails){
    data class TransDetails(val time: Long, val hash: String, val out: List<SpentDetails>){
        data class SpentDetails(val value: Long, val spent: Boolean)
    }
}
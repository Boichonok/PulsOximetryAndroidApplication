package com.example.alex.bbsaturation.BBSaturation_DataBase

import android.util.Log

/**
 * Created by alex on 03.11.17.
 */
class mojoBBSaturation {

    private var TAG: String = "StreamManager"

    private var hr: Double = 0.0
    private var spo2: Double = 0.0
    private var id : Int = 0

    public fun setId(id : Int){
        this.id = id
    }

    public fun getId(): Int {
        return this.id
    }

    public fun getHr(): Double {
        Log.d(TAG,"GetHr: " + this.hr)
        return this.hr
    }

    public fun setHr(hr : Double){
        this.hr = hr
        Log.d(TAG,"setHR: " + this.hr)
    }

    public fun getSpO2(): Double {
        Log.d(TAG,"GETspo2 : " + this.spo2)
        return this.spo2
    }

    public fun setSpO2(spo2: Double) {
        this.spo2 = spo2
        Log.d(TAG,"setSpo2: " + this.spo2)

    }
}
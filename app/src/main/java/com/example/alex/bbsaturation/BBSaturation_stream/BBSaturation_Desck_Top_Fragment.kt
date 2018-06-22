package com.example.alex.bbsaturation.BBSaturation_stream

import android.annotation.TargetApi
import android.app.Application
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.alex.bbsaturation.BBSaturation_DataBase.BBSaturation_DataBase
import com.example.alex.bbsaturation.BBSaturation_DataBase.mojoBBSaturation
import com.example.alex.bbsaturation.R

/**
 * Created by alex on 04.11.17.
 */
class BBSaturation_Desck_Top_Fragment : Fragment() {

    private var TAG: String = "StreamManager"
    private var hrText: TextView? = null
    private var spo2Text: TextView? = null
    private var mojo: mojoBBSaturation? = null
    private var bbsaturationDataBaseObj: BBSaturation_DataBase? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater?.inflate(R.layout.desk_top_fragment, container, false)!!

        hrText = view.findViewById<TextView>(R.id.hrText)
        spo2Text = view.findViewById<TextView>(R.id.spo2Text)
        if (view.context != null) {
            bbsaturationDataBaseObj = BBSaturation_DataBase.getInstance(view.context!!)
            if (!bbsaturationDataBaseObj!!.getAllData(view.context).isEmpty()){
                mojo = bbsaturationDataBaseObj!!.getAllData(view.context).last()
                hrText!!.setText(String.format("%.1f", mojo!!.getHr()))
                spo2Text!!.setText(String.format("%.1f", mojo!!.getSpO2()))

            }

        }
        return view
    }



}
package com.example.alex.bbsaturation.BBSaturation_DataBase

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by alex on 03.11.17.
 */
class BBSaturation_DataBase : SQLiteOpenHelper {


    private var TAG: String = "StreamManager"
    private val DB_NAME : String = "BBSaturation"
    private var DB_VERSION = 1
    private val tableName : String = "bbSaturationParams"
    private val pol_hr : String = "HR"
    private val pol_SpO2 : String = "SpO2"
    private val pol_id : String = "id"

    private constructor(context : Context) : super(context,"BBSaturation",null,1)

    public fun addRow(context: Context,HR: String, SpO2: String){
        val contenValues:ContentValues = ContentValues()
        contenValues.put("HR",HR)
        contenValues.put("SpO2",SpO2)
        val id =  getInstance(context).writableDatabase!!.insert(tableName,null,contenValues)
        Log.d(TAG,"results Adding in to DATA_BASE: " + id + " is true")
    }

    public fun getAllData(context: Context):List<mojoBBSaturation> {
        val mojoList: ArrayList<mojoBBSaturation> = ArrayList()

        val c : Cursor = getInstance(context).writableDatabase!!.query(tableName,null,null,null,null,null,null)

        if (c.moveToFirst()) {
            val idColIndex = c.getColumnIndex(pol_id)
            val hrColIndex = c.getColumnIndex(pol_hr)
            val spo2ColIndex = c.getColumnIndex(pol_SpO2)
            val mojo = mojoBBSaturation()
            do {
                mojo.setHr(c.getString(hrColIndex).toDouble())
                mojo.setSpO2(c.getString(spo2ColIndex).toDouble())
                mojo.setId(c.getInt(idColIndex))
                mojoList.add(mojo)
            } while (c.moveToNext())
            c.close()
        }
        return mojoList
    }
    companion object {
        private var mInstanceate : BBSaturation_DataBase? = null

        @JvmStatic
        fun getInstance(context: Context) : BBSaturation_DataBase {
            if (mInstanceate == null) {
                mInstanceate = BBSaturation_DataBase(context.applicationContext)
            }
            return mInstanceate!!
        }


    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val res = p0!!.execSQL("create table "+ tableName +" (" +
        pol_id + " integer primary key autoincrement," +
        pol_hr + " TEXT," +
        pol_SpO2 + " TEXT" + ");")
        Log.d(TAG,"OnCreateDB" + res)
    }
}
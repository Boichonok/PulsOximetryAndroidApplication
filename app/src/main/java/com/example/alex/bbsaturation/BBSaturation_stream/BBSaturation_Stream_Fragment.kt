package com.example.alex.bbsaturation.BBSaturation_stream

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.alex.bbsaturation.BBSaturation_DataBase.BBSaturation_DataBase

import com.example.alex.bbsaturation.R
import com.example.bbsaturationlibrary.PulsOximetryCalculation
import com.example.bbsaturationlibrary.StreamManager
import com.example.bbsaturationlibrary.StreamManagerInterface
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.bbsaturtion_stream_fragment.*

/**
 * Created by alex on 02.11.17.
 */
class BBSaturation_Stream_Fragment : Fragment(), StreamManagerInterface,CompoundButton.OnCheckedChangeListener{


    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

           when (p0!!.id) {
               R.id.chooseLineType -> {
                   if (p1) {
                           /*var countPoint : Int = 0
                           var dataPoint  = Array<DataPoint>(pulsOximetryObject!!.picksList.size,{countPoint -> DataPoint(0.0,0.0) })
                            for (i in pulsOximetryObject!!.picksTime) {
                                if(pulsOximetryObject!!.picksList.containsKey(i)) {
                                    dataPoint[countPoint] = DataPoint(countPoint.toDouble(), pulsOximetryObject!!.picksList.getValue(i))
                                }
                                countPoint++
                            }
                            pickPointIr = PointsGraphSeries(dataPoint)
                            graphView!!.addSeries(pickPointIr)
                            */
                       graphView!!.addSeries(pickPointIr)
                       graphView!!.removeSeries(redSeries)
                       graphView!!.removeSeries(irSeries)

                   } else {
                       graphView!!.removeSeries(pickPointIr)
                       graphView!!.addSeries(irSeries)
                       graphView!!.addSeries(redSeries)
                   }
               }
           }

    }

    private var TAG: String = "StreamManager"
    private var streamManager : StreamManager? = null
    private var pulsOximetryObject: PulsOximetryCalculation? = null
    private var graphView : GraphView? = null
    private var countReceiveData1 : Double? = 0.0
    private var irSeries : LineGraphSeries<DataPoint>? = null
    private var redSeries : LineGraphSeries<DataPoint>? = null

   // private var secondLineOrderIR: LineGraphSeries<DataPoint>? = null
   // private var secondLineOrderRED: LineGraphSeries<DataPoint>? = null
    private var pickPointIr: PointsGraphSeries<DataPoint>? = null

    private var results : TextView? = null
    private var startStream: Button? = null
    private var chooseLineType: Switch? = null

    private val hrList : ArrayList<Double>? = ArrayList()
    private val spo2List : ArrayList<Double>? = ArrayList()

    private var _context: Context? = null
    private fun initGraph(v : View?) {
        this.graphView = v!!.findViewById<GraphView>(R.id.graph)
        this.graphView!!.gridLabelRenderer.setLabelFormatter(object : DefaultLabelFormatter(){
            override fun formatLabel(value: Double, isValueX: Boolean): String {
               var _value = value
                if (isValueX) {
                    return String.format("%.1f",(_value * (1.0 / 250)))
                } else {
                    if (_value < 0) {
                        _value = Math.abs(_value)
                        if (_value > 1) {
                            return "-" + String.format("%.1f", Math.log(_value))
                        } else {
                            return "-" + super.formatLabel(_value, isValueX)
                        }
                    } else {
                        if (_value > 1) {
                            return String.format("%.1f", Math.log(_value))
                        } else {
                            return super.formatLabel(_value, isValueX)
                        }
                    }
                }
            }
        })
        this.graphView!!.viewport.setMaxX(5000.0);
        this.graphView!!.viewport.setYAxisBoundsManual(true)
        this.graphView!!.viewport.setMinY(0.0)
        this.graphView!!.viewport.setMaxY(1000000.0)
        this.graphView!!.viewport.setScrollable(true)
        this.graphView!!.viewport.setScalable(true)
        this.graphView!!.gridLabelRenderer.setVerticalLabelsVisible(false)
        this.graphView!!.gridLabelRenderer.setHorizontalLabelsVisible(false)

        irSeries = LineGraphSeries<DataPoint>()
        redSeries = LineGraphSeries<DataPoint>()
       // secondLineOrderRED = LineGraphSeries<DataPoint>()
       // secondLineOrderIR = LineGraphSeries<DataPoint>()
        pickPointIr = PointsGraphSeries<DataPoint>()

        redSeries!!.setColor(Color.RED)
        redSeries!!.setDrawBackground(true)
        redSeries!!.setBackgroundColor(Color.argb(64,204,46,46))

        irSeries!!.setColor(Color.BLACK)
        irSeries!!.setDrawBackground(true)
        irSeries!!.setBackgroundColor(Color.argb(161,0,0,0))

       // secondLineOrderIR!!.color = Color.argb(255,7,99,91)
       // secondLineOrderIR!!.isDrawBackground = true
       // secondLineOrderIR!!.backgroundColor = Color.argb(83,7,99,91)
      //  secondLineOrderRED!!.color = Color.argb(255,191,10,10)
      //  secondLineOrderRED!!.isDrawBackground = true
      //  secondLineOrderRED!!.backgroundColor = Color.argb(83,7,99,91)
        pickPointIr!!.color = Color.argb(255,7,99,91)


        graphView!!.removeAllSeries()
        graphView!!.addSeries(irSeries)
        graphView!!.addSeries(redSeries)
       // graphView!!.addSeries(secondLineOrderIR)
        //graphView!!.addSeries(secondLineOrderRED)
        graphView!!.addSeries(pickPointIr)
    }


    private fun cleanGraph() {
        redSeries = LineGraphSeries<DataPoint>()
        irSeries = LineGraphSeries<DataPoint> ()
       // secondLineOrderIR = LineGraphSeries<DataPoint>()
       // secondLineOrderRED = LineGraphSeries<DataPoint>()
        pickPointIr = PointsGraphSeries<DataPoint>()

        redSeries!!.setColor(Color.argb(255, 191, 10, 10))
        redSeries!!.setDrawBackground(true)
        redSeries!!.setBackgroundColor(Color.argb(83, 209, 10, 10))
        irSeries!!.setColor(Color.argb(255, 7, 99, 91))
        irSeries!!.setDrawBackground(true)
        irSeries!!.setBackgroundColor(Color.argb(83, 7, 99, 91))

       // secondLineOrderIR!!.color = Color.argb(255,7,99,91)
       // secondLineOrderIR!!.isDrawBackground = true
       // secondLineOrderIR!!.backgroundColor = Color.argb(83,7,99,91)
       // secondLineOrderRED!!.color = Color.argb(255,191,10,10)
       // secondLineOrderRED!!.isDrawBackground = true
       // secondLineOrderRED!!.backgroundColor = Color.argb(83,7,99,91)
        pickPointIr!!.color = Color.argb(255,7,99,91)

        graphView!!.removeAllSeries()
        graphView!!.addSeries(irSeries)
        graphView!!.addSeries(redSeries)
      // graphView!!.addSeries(secondLineOrderIR)
      //  graphView!!.addSeries(secondLineOrderRED)
        graphView!!.addSeries(pickPointIr)

        graphView!!.scrollTo(0, 0)
        graphView!!.getViewport().setXAxisBoundsManual(true)
        graphView!!.getViewport().setYAxisBoundsManual(true)
        graphView!!.getViewport().setMinX(0.0)
        graphView!!.getViewport().setMaxX(5000.0)
        graphView!!.getViewport().setMinY(-2000.0)
        graphView!!.getViewport().setMaxY(5000.0)
        graphView!!.getViewport().setScrollable(true)
        graphView!!.getViewport().setScalable(true)

        graphView!!.getGridLabelRenderer().setVerticalLabelsVisible(false)
        graphView!!.getGridLabelRenderer().setHorizontalLabelsVisible(false)
        countReceiveData1 = 0.0;
    }



    //3
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater?.inflate(R.layout.bbsaturtion_stream_fragment, container, false)!!
        this._context = view.context
        startStream = view.findViewById<Button>(R.id.start_stream)
        this.results = view.findViewById<TextView>(R.id.results)
        this.chooseLineType = view.findViewById<Switch>(R.id.chooseLineType)
        this.chooseLineType!!.setOnCheckedChangeListener(this)
        this.chooseLineType!!.isEnabled = false
        startStream!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                streamManager!!.connectingTo("BadBoys")
            }

        })
        activity.runOnUiThread(Runnable {
            initGraph(view)
        })
        return view
    }



    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.streamManager = StreamManager(activity,this,false)

    }
    override fun onResume() {
        super.onResume()

    }




    override fun ppgSignals(p0: Double?, p1: Double?, p2: PulsOximetryCalculation?) {
        p2!!.addSample(p0!!, (this.countReceiveData1!! * 20.06).toInt())
        p2.addSampleSpO2(p0,p1!!,(this.countReceiveData1!! * 20.06).toInt() )
        results!!.setText("hr: " + String.format("%.0f",p2.heartRate) + " SpO2: " + String.format("%.0f",p2.spO2) + " %")

        hrList!!.add(p2.heartRate)
        spo2List!!.add(p2.spO2)

        this.irSeries!!.appendData(DataPoint(countReceiveData1!!,p0),true,50000)
        this.redSeries!!.appendData(DataPoint(countReceiveData1!!,p1),true,50000)
        //this.pickPointIr!!.appendData(DataPoint(countReceiveData1!!,p2.picksList.get((countReceiveData1!! * 10.0).toInt())!!),true,5000)
        if(p2.picksTime.contains(((countReceiveData1!!-1) * 20.06).toInt())) {
            this.pickPointIr!!.appendData(DataPoint(countReceiveData1!! - 1, p2!!.picksList.getValue(((countReceiveData1!!-1) * 20.06).toInt())), true, 50000)
        }
        countReceiveData1 = countReceiveData1!! + 1.0

        Log.d(TAG,"countReceiveData: " + countReceiveData1)

    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun stopStream(p0: String?, p1: PulsOximetryCalculation?) {
        Log.d(TAG,"STOP_STREAM: " + p0)
        streamManager!!.disconnectDevice()
        this.pulsOximetryObject = p1!!
        for ( interval in p1.rrIntervals) {
            Log.d(TAG,"Interval: " + interval)
        }
        calculateMeasure()
        activity.runOnUiThread(Runnable {
             startStream!!.isEnabled = true
             chooseLineType!!.isEnabled = true
        })
    }



    private fun calculateMeasure() {
        var averageHr = 0.0
        var averageSpO2 = 0.0
        var sumHr = 0.0
        var sumSpO2 = 0.0
        for (i in hrList!!.indices) {
            sumHr = sumHr + hrList[i]
        }
        for (i in spo2List!!.indices) {
            sumSpO2 = sumSpO2 + spo2List[i]
        }
        averageHr = sumHr / hrList.size
        averageSpO2 = sumSpO2 / spo2List.size
        BBSaturation_DataBase.getInstance(this._context!!).addRow(this._context!!,averageHr.toString(),averageSpO2.toString())

    }

    override fun cleanGraphicsAndStartSendingCommand() {
        cleanGraph()
        activity.runOnUiThread(Runnable {
            startStream!!.isEnabled = false
            this.chooseLineType!!.isEnabled = false
            this.streamManager!!.sendCommand(this.streamManager!!.comands["StartStream"])}
        )


    }

    override fun Errors(p0: String?) {
        activity.runOnUiThread(Runnable {
            results!!.setText("Errors: " + p0)
        })
    }

}
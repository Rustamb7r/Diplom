package etu.diplom

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import etu.diplom.databinding.ActivitySecondGraphBinding
import kotlin.math.abs

class SecondGraph : AppCompatActivity() {

    private var binding: ActivitySecondGraphBinding? = null
    private var listX: ArrayList<Double> = ArrayList()
    private var listY: ArrayList<Double> = ArrayList()
    private var listApprox: ArrayList<Double> = ArrayList()
    private var listApproxX: ArrayList<Double> = ArrayList()
    private var listNegativeY: ArrayList<Double> = ArrayList()
    private var listPositiveY: ArrayList<Double> = ArrayList()
    private var listNegativeX: ArrayList<Double> = ArrayList()
    private var listPositiveX: ArrayList<Double> = ArrayList()
    private var listNegativeXApprox: ArrayList<Double> = ArrayList()
    private var listNegativeYApprox: ArrayList<Double> = ArrayList()
    private var listPositiveXApprox: ArrayList<Double> = ArrayList()
    private var listPositiveYApprox: ArrayList<Double> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondGraphBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val point = 1
        val n = 5

        val listOX = converter(intent.getStringArrayListExtra("LIST_X_SECOND")!!)
        val listOY = converter(intent.getStringArrayListExtra("LIST_Y_SECOND")!!)
        Log.d("MySmartLog", "listOX: $listOX")
        Log.d("MySmartLog", "listOY: $listOY")

        var k = 0
        while (k < listOX.size - 1) {
            if (listOX[k + 1] - listOX[k] == 1.0) {
                listX.add(listOX[k])
                listY.add(listOY[k])
            } else if (listOX[k + 1] - listOX[k] != 1.0) {
                listX.add(listOX[k])
                listY.add(listOY[k])
                listX.add((listOX[k + 1] + listOX[k]) / 2)
                listY.add((listOY[k + 1] + listOY[k]) / 2)

//      TODO Неудачный вариант
//                listX.add(listOX[k])
//                listY.add(listOY[k])
//                var difference = listOX[k + 1] - listOX[k]
//                while (difference != 1.0) {
//                    listX.add(++listOX[k])
//                    listY.add(listOY[k] + ((listOY[k + 1] - listOY[k]) / difference))
//                    difference--
//                }
            }
            k++
        }

        binding?.btnList1?.setOnClickListener {
            var i = point
            listApproxX.clear()
            while (i < listX.size - n) {
                listApproxX.add(listX[i])
                i++
            }
            listApprox = getFirstApprox(listX, listY, n, point)
            binding?.layoutGraphOne?.removeAllSeries()
            binding?.layoutGraphOne?.addSeries(LineGraphSeries(showGraph(listApproxX, listApprox)))
            Log.d("MySmartLog", "listApproxX size: ${listApproxX.size}")
            Log.d("MySmartLog", "listApprox size: ${listApprox.size}")
        }

        binding?.btnList2?.setOnClickListener {
            var i = 0
            listApproxX.clear()
            while (i < listX.size - 1) {
                listApproxX.add(listX[i])
                i++
            }
            listApprox = getSecondApprox(listX, listY)
            binding?.layoutGraphOne?.removeAllSeries()
            binding?.layoutGraphOne?.addSeries(LineGraphSeries(showGraph(listApproxX, listApprox)))
            Log.d("MySmartLog", "listApproxX size: ${listApproxX.size}")
            Log.d("MySmartLog", "listApprox size: ${listApprox.size}")
        }

        Log.d("MySmartLog", "listX: $listX")
        Log.d("MySmartLog", "listY: $listY")

//        Разделение по нулю
        binding?.btnListOne?.setOnClickListener {
            clickOne()
        }

//        Разделение траектории на две прямые
        binding?.btnListTwo?.setOnClickListener {
            clickTwo(point, listX, listY)
        }
    }

    private fun clickOne() {
        listNegativeY.clear()
        listNegativeX.clear()
        listPositiveY.clear()
        listPositiveX.clear()
        var i = 0
        while (i < listApprox.size) {
            if (listApprox[i] < 0) {
                listNegativeY.add(listApprox[i])
                listNegativeX.add(listApproxX[i])
            }
            if (listApprox[i] > 0) {
                listPositiveY.add(listApprox[i])
                listPositiveX.add(listApproxX[i])
            }
            i++
        }
//            Log.d("MySmartLog", "listNegativeY: $listNegativeY")
//            Log.d("MySmartLog", "listNegativeX: $listNegativeX")
//            Log.d("MySmartLog", "listPositiveY: $listPositiveY")
//            Log.d("MySmartLog", "listPositiveX: $listPositiveX")
//        Log.d("MySmartLog", "listNegativeX size: ${listNegativeX.size}")
//        Log.d("MySmartLog", "listNegativeY size: ${listNegativeY.size}")
//        Log.d("MySmartLog", "listPositiveX size: ${listPositiveX.size}")
//        Log.d("MySmartLog", "listPositiveY size: ${listPositiveY.size}")

        binding?.layoutGraphTwo?.removeAllSeries()
        binding?.layoutGraphTwo?.addSeries(
            LineGraphSeries(
                showGraph(
                    listNegativeX,
                    listNegativeY
                )
            )
        )
        binding?.layoutGraphThree?.removeAllSeries()
        binding?.layoutGraphThree?.addSeries(
            LineGraphSeries(
                showGraph(
                    listPositiveX,
                    listPositiveY
                )
            )
        )
    }

    private fun clickTwo(point: Int, listX: ArrayList<Double>, listY: ArrayList<Double>) {
        listNegativeXApprox.clear()
        listNegativeYApprox.clear()
        listPositiveXApprox.clear()
        listPositiveYApprox.clear()

        val splitLeft = 60
        val splitRight = 50
        if (listNegativeX.size != 0 && listNegativeY.size != 0) {
            var i = 0
            while (i < listNegativeX.size - splitLeft) {
                listNegativeXApprox.add(listNegativeX[i])
                listNegativeYApprox.add(listY[i + point])
//                    listNegativeYApprox.add(listNegativeY[i])
                i++
            }
//                Log.d("MySmartLog", "listNegativeXApprox size: ${listNegativeXApprox.size}")
//                Log.d("MySmartLog", "listNegativeYApprox size: ${listNegativeYApprox.size}")
        } else Toast.makeText(this, "Списки пустые", Toast.LENGTH_SHORT).show()

        if (listPositiveX.size != 0 && listPositiveY.size != 0) {
            var i = splitRight
            while (i < listPositiveX.size) {
                listPositiveXApprox.add(listPositiveX[i])
                listPositiveYApprox.add(listY[i + point + splitLeft + listNegativeYApprox.size])
//                    listPositiveYApprox.add(listPositiveY[i])
                i++
            }
//                Log.d("MySmartLog", "listPositiveXApprox size: ${listPositiveXApprox.size}")
//                Log.d("MySmartLog", "listPositiveYApprox size: ${listPositiveYApprox.size}")
        } else Toast.makeText(this, "Списки пустые", Toast.LENGTH_SHORT).show()

        binding?.layoutGraphFive?.removeAllSeries()
        binding?.layoutGraphFive?.addSeries(
            LineGraphSeries(
                showGraph(
                    listNegativeXApprox,
                    listNegativeYApprox
                )
            )
        )
        binding?.layoutGraphFive?.addSeries(
            LineGraphSeries(
                showGraph(
                    listPositiveXApprox,
                    listPositiveYApprox
                )
            )
        )
        binding?.layoutGraphFour?.removeAllSeries()
        binding?.layoutGraphFour?.addSeries(LineGraphSeries(showGraph(listX, listY)))
    }

    private fun converter(
        list: ArrayList<String>
    ): ArrayList<Double> {
        val newList: ArrayList<Double> = ArrayList()
        newList.clear()
        var i = 0
        while (i < list.size) {
            newList.add(list[i].toDouble())
            i++
        }
        return newList
    }

    private fun showGraph(
        list1: ArrayList<Double>,
        list2: ArrayList<Double>
    ): Array<DataPoint> {
        val dataPoint = ArrayList<DataPoint>()
        dataPoint.clear()
        var i = 0

        while (i < list2.size) {
            dataPoint.add(DataPoint(list1[i], list2[i]))
            i++
        }
        return dataPoint.toTypedArray()
    }

    private fun getFirstApprox(
        listX: ArrayList<Double>,
        listY: ArrayList<Double>,
        n: Int,
        point: Int
    ): ArrayList<Double> {
        val newApproxList: ArrayList<Double> = ArrayList()
        var j = point

        while (j < listX.size - n) {
            var count = j
            var sumx = 0.0
            var sumy = 0.0
            var sumx2 = 0.0
            var sumxy = 0.0

            var i = 0
            while (i < n) {
                sumx += listX[count]
                sumy += listY[count]
                sumx2 += listX[count] * listX[count]
                sumxy += listX[count] * listY[count]
                i++
                count++
            }
            val a = (n * sumxy - (sumx * sumy)) / ((n * sumx) - (sumx * sumx))
            newApproxList.add(a)
            j++
        }
        return newApproxList
    }

    private fun getSecondApprox(
        listX: ArrayList<Double>,
        listY: ArrayList<Double>
    ): ArrayList<Double> {
        val newApproxList: ArrayList<Double> = ArrayList()

        var i = 0
        var dy: Double
        var dx: Double
        while (i < listX.size - 1) {
            dy = listY[i] - listY[i + 1]
            dx = listX[i] - listX[i + 1]
            val a = abs(dy / dx)
            newApproxList.add(a)
            i++
        }
        return newApproxList
    }
}
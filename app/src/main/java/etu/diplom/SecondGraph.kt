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
import java.util.*
import kotlin.collections.ArrayList
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
        val n = 40

//        TODO Получение координат от Хасбика
        val baseListOX = converter(intent.getStringArrayListExtra("LIST_X_SECOND")!!)
        val baseListOY = converter(intent.getStringArrayListExtra("LIST_Y_SECOND")!!)
//        Log.d("MySmartLog", "listOX: $listOX")
//        Log.d("MySmartLog", "listOY: $listOY")

//        TODO Заполнение пробелов в списках координат
        var k = 0
        while (k < baseListOX.size - 1) {
            if (baseListOX[k + 1] - baseListOX[k] == 1.0) {
                listX.add(baseListOX[k])
                listY.add(baseListOY[k])
            } else if (baseListOX[k + 1] - baseListOX[k] != 1.0) {
                listX.add(baseListOX[k])
                listY.add(baseListOY[k])
                listX.add((baseListOX[k + 1] + baseListOX[k]) / 2)
                listY.add((baseListOY[k + 1] + baseListOY[k]) / 2)

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
//        Log.d("MySmartLog", "listX: $listX")
//        Log.d("MySmartLog", "listY: $listY")

//        TODO Первый способ апроксимации
        binding?.btnList1?.setOnClickListener {
            var i = point
            listApproxX.clear()
            while (i < listX.size - n) {
                listApproxX.add(listX[i])
                i++
            }
            val startOne = System.currentTimeMillis()
            listApprox = getFirstApprox(listX, listY, n, point)
            val timeOne = System.currentTimeMillis() - startOne
            Log.d("MySmartLog", "timeOne: $timeOne")

            binding?.layoutGraphOne?.removeAllSeries()
            binding?.layoutGraphOne?.gridLabelRenderer?.horizontalAxisTitle = "x"
            binding?.layoutGraphOne?.gridLabelRenderer?.verticalAxisTitle = "a"

            binding?.layoutGraphOne?.viewport?.setMinX(0.0)
            binding?.layoutGraphOne?.viewport?.setMaxX(Collections.max(listApproxX))
            binding?.layoutGraphOne?.viewport?.setMinY(Collections.min(listApprox))
            binding?.layoutGraphOne?.viewport?.setMaxY(Collections.max(listApprox))
            binding?.layoutGraphOne?.viewport?.isXAxisBoundsManual = true
            binding?.layoutGraphOne?.viewport?.isYAxisBoundsManual = true

            binding?.layoutGraphOne?.addSeries(LineGraphSeries(showGraph(listApproxX, listApprox)))
//            Log.d("MySmartLog", "listApproxX size: ${listApproxX.size}")
//            Log.d("MySmartLog", "listApprox size: ${listApprox.size}")
        }

//        TODO Тупорылый способ апроксимации Федорова
        binding?.btnList2?.setOnClickListener {
            var i = 0
            listApproxX.clear()
            while (i < listX.size - 1) {
                listApproxX.add(listX[i])
                i++
            }

            val startTwo = System.currentTimeMillis()
            listApprox = getSecondApprox(listX, listY, n)
            val timeTwo = System.currentTimeMillis() - startTwo
            Log.d("MySmartLog", "timeTwo: $timeTwo")

            binding?.layoutGraphOne?.removeAllSeries()
            binding?.layoutGraphOne?.gridLabelRenderer?.horizontalAxisTitle = "x"
            binding?.layoutGraphOne?.gridLabelRenderer?.verticalAxisTitle = "a"

            binding?.layoutGraphOne?.viewport?.setMinX(0.0)
            binding?.layoutGraphOne?.viewport?.setMaxX(Collections.max(listApproxX))
            binding?.layoutGraphOne?.viewport?.setMinY(Collections.min(listApprox))
            binding?.layoutGraphOne?.viewport?.setMaxY(Collections.max(listApprox))
            binding?.layoutGraphOne?.viewport?.isXAxisBoundsManual = true
            binding?.layoutGraphOne?.viewport?.isYAxisBoundsManual = true

            binding?.layoutGraphOne?.addSeries(LineGraphSeries(showGraph(listApproxX, listApprox)))
//            Log.d("MySmartLog", "listApproxX size: ${listApproxX.size}")
//            Log.d("MySmartLog", "listApprox size: ${listApprox.size}")
        }

//        TODO Получение апроксимированного графика
        binding?.btnListOne?.setOnClickListener {
            clickOne()
        }

//        TODO Получение двух апроксимированных графиков по нулю
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

//        TODO Разделение по нулю
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
//        Log.d("MySmartLog", "listNegativeY: $listNegativeY")
//        Log.d("MySmartLog", "listNegativeX: $listNegativeX")
//        Log.d("MySmartLog", "listPositiveY: $listPositiveY")
//        Log.d("MySmartLog", "listPositiveX: $listPositiveX")
//        Log.d("MySmartLog", "listNegativeX size: ${listNegativeX.size}")
//        Log.d("MySmartLog", "listNegativeY size: ${listNegativeY.size}")
//        Log.d("MySmartLog", "listPositiveX size: ${listPositiveX.size}")
//        Log.d("MySmartLog", "listPositiveY size: ${listPositiveY.size}")

        binding?.layoutGraphTwo?.removeAllSeries()

//        TODO Раскоментить если будут нормальные координаты
//        binding?.layoutGraphTwo?.viewport?.setMinX(0.0)
//        binding?.layoutGraphTwo?.viewport?.setMaxX(Collections.max(listNegativeX))
//        binding?.layoutGraphTwo?.viewport?.setMinY(Collections.min(listNegativeY))
//        binding?.layoutGraphTwo?.viewport?.setMaxY(Collections.max(listNegativeY))
//        binding?.layoutGraphTwo?.viewport?.isXAxisBoundsManual = true
//        binding?.layoutGraphTwo?.viewport?.isYAxisBoundsManual = true
        binding?.layoutGraphTwo?.addSeries(LineGraphSeries(showGraph(listNegativeX, listNegativeY)))

//        TODO Раскоментить если будут нормальные координаты
        binding?.layoutGraphThree?.removeAllSeries()
//        binding?.layoutGraphThree?.viewport?.setMinX(0.0)
//        binding?.layoutGraphThree?.viewport?.setMaxX(Collections.max(listPositiveX))
//        binding?.layoutGraphThree?.viewport?.setMinY(Collections.min(listPositiveY))
//        binding?.layoutGraphThree?.viewport?.setMaxY(Collections.max(listPositiveY))
//        binding?.layoutGraphThree?.viewport?.isXAxisBoundsManual = true
//        binding?.layoutGraphThree?.viewport?.isYAxisBoundsManual = true
        binding?.layoutGraphThree?.addSeries(LineGraphSeries(showGraph(listPositiveX, listPositiveY)))
    }

    private fun clickTwo(pointNew: Int, listX: ArrayList<Double>, listY: ArrayList<Double>) {
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
                listNegativeYApprox.add(listY[i + pointNew])
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
                listPositiveYApprox.add(listY[i + pointNew + splitLeft + listNegativeYApprox.size])
//                    listPositiveYApprox.add(listPositiveY[i])
                i++
            }
//                Log.d("MySmartLog", "listPositiveXApprox size: ${listPositiveXApprox.size}")
//                Log.d("MySmartLog", "listPositiveYApprox size: ${listPositiveYApprox.size}")
        } else Toast.makeText(this, "Списки пустые", Toast.LENGTH_SHORT).show()

        binding?.layoutGraphFour?.removeAllSeries()
        binding?.layoutGraphFour?.gridLabelRenderer?.horizontalAxisTitle = "x"
        binding?.layoutGraphFour?.gridLabelRenderer?.verticalAxisTitle = "a"
        binding?.layoutGraphFour?.addSeries(LineGraphSeries(showGraph(listX, listY)))

        binding?.layoutGraphFive?.removeAllSeries()
        binding?.layoutGraphFive?.gridLabelRenderer?.horizontalAxisTitle = "x"
        binding?.layoutGraphFive?.gridLabelRenderer?.verticalAxisTitle = "y"
        binding?.layoutGraphFive?.addSeries(LineGraphSeries(showGraph(listNegativeXApprox, listNegativeYApprox)))
        binding?.layoutGraphFive?.addSeries(LineGraphSeries(showGraph(listPositiveXApprox, listPositiveYApprox)))
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
        listY: ArrayList<Double>,
        n: Int
    ): ArrayList<Double> {
        val newApproxList: ArrayList<Double> = ArrayList()

        var i = n / 2 + 1
        var dy: Double
        var dx: Double
        while (i < listX.size - n / 2 - 1) {
            dy = listY[i - n / 2] - listY[i + n / 2]
            dx = listX[i - n / 2] - listX[i + n / 2]
            val a = abs(dy / dx)
            newApproxList.add(a)
            i++
        }
        return newApproxList
    }
}
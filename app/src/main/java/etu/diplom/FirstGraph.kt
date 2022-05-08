package etu.diplom

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import etu.diplom.databinding.ActivityFirstGraphBinding

class FirstGraph : AppCompatActivity() {

    private var binding: ActivityFirstGraphBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstGraphBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val listX = intent.getStringArrayListExtra("LIST_X_FIRST")
        val listY = intent.getStringArrayListExtra("LIST_Y_FIRST")

        if (listX != null && listY != null) {
            show(listX, listY)
        }
    }

    private fun show(pointsX: ArrayList<String>, pointsY: ArrayList<String>) {
        val dx: Float = (pointsX[0].toFloat().toInt() / 10 * 10).toFloat()
        val dy: Float = (pointsY[1].toFloat().toInt() / 10 * 10).toFloat()

        val p = Path()
        var i = 0
        while (i < pointsX.size) {
            p.lineTo(pointsX[i].toFloat() - dx, pointsY[i].toFloat() - dy)
            i++
        }

        val d = ShapeDrawable(PathShape(p, 200F, 200F))
        d.intrinsicHeight = 100
        d.intrinsicWidth = 100
        d.paint.color = Color.RED
        d.paint.style = Paint.Style.STROKE

        binding?.firstGraph?.setImageDrawable(d)
    }
}
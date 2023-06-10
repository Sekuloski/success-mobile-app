package mk.sekuloski.success.utils

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomPieChartRenderer(chart: PieChart?, animator: ChartAnimator?, viewPortHandler: ViewPortHandler?, colors: HashMap<String, Int>) : PieChartRenderer(chart, animator, viewPortHandler) {

    private var mHasLabelData = false
    private var mHasValueData = false
    private var mEntryLabelCanvas: Canvas? = null
    private var mValueCanvas: Canvas? = null
    private var mEntryLabel: String = ""
    private var mValueText: String = ""
    private var mEntryLabelX = 0f
    private var mColors: HashMap<String, Int> = colors
    private var mValueX = 0f
    private var mEntryLabelY = 0f
    private var mValueY = 0f
    private var mValueColor = 0

    override fun drawEntryLabel(c: Canvas?, label: String, x: Float, y: Float) {
        //instead of calling super save the label data temporary
        //super.drawEntryLabel(c, label, x, y)
        mHasLabelData = true
        //save all entry label information temporary
        mEntryLabelCanvas = c
        mEntryLabel = label
        mEntryLabelX = x
        mEntryLabelY = y
        //and check if we have both label and value data temporary to draw them
        checkToDrawLabelValue()
    }

    override fun drawValue(c: Canvas?, valueText: String, x: Float, y: Float, color: Int) {
        //instead of calling super save the value data temporary
        //super.drawValue(c, valueText, x, y, color)
        mHasValueData = true
        //save all value information temporary
        mValueCanvas = c
        mValueText = valueText
        mValueX = x
        mValueY = y
        mValueColor = color
        //and check if we have both label and value data temporary to draw them
        checkToDrawLabelValue()
    }

    private fun checkToDrawLabelValue() {
        if (mHasLabelData && mHasValueData) {
            drawLabelAndValue()
            mHasLabelData = false
            mHasValueData = false
        }
    }

    private fun drawLabelAndValue() {
        //to show label on top of the value just swap the mEntryLabelY with mValueY
        drawEntryLabelData(mEntryLabelCanvas, mEntryLabel, mEntryLabelX, mValueY)
        drawValueData(mValueCanvas, mValueText, mValueX, mEntryLabelY, mValueColor)
    }

    //This is the same code used in super.drawEntryLabel(c, label, x, y) with any other customization you want in mEntryLabelsPaint
    @SuppressLint("ResourceAsColor")
    private fun drawEntryLabelData(c: Canvas?, label: String, x: Float, y: Float) {
        val mEntryLabelsPaint: Paint = paintEntryLabels
        mEntryLabelsPaint.color = mColors[label] ?: Color.BLACK
        mEntryLabelsPaint.typeface = Typeface.DEFAULT_BOLD
        mEntryLabelsPaint.textAlign = Paint.Align.CENTER
        c?.drawText(label, x, y, mEntryLabelsPaint)
    }

    //This is the same code used in super.drawValue(c, valueText, x, y, color) with any other customization you want in mValuePaint
    private fun drawValueData(c: Canvas?, valueText: String, x: Float, y: Float, color: Int) {
        mValuePaint.color = color
        mValuePaint.textAlign = Paint.Align.CENTER
        c?.drawText(valueText, x, y, mValuePaint)
    }
}
package com.blighter.algoprog.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.RectF
import android.text.style.ReplacementSpan
import kotlin.math.roundToInt

//CustomClass for roundSpan used for achievements
class RoundedBackgroundSpan(color: Int) : ReplacementSpan() {
    private var backgroundColor = 0
    private val cornerRadius = 16
    private val textColor = Color.parseColor("#000000")
    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val rect = RectF(x, top.toFloat(), x + measureText(paint, text, start, end), bottom.toFloat())
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, cornerRadius.toFloat(), cornerRadius.toFloat(), paint)
        paint.color = textColor
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: FontMetricsInt?): Int {
        return paint.measureText(text, start, end).roundToInt()
    }

    private fun measureText(paint: Paint, text: CharSequence, start: Int, end: Int): Float {
        return paint.measureText(text, start, end)
    }


    init {
        backgroundColor = color
    }
}
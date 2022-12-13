package com.example.plannerapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.text.Transliterator
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

class PlannerView(context: Context?, attributes: AttributeSet? = null) : View(context, attributes) {

    val rect1: Rect = Rect()
    val rect2: Rect = Rect()


    init {
        setUp()
    }

    fun setUp(){
        rect1.top = 10
        rect1.left = 10
        rect1.bottom = rect1.top + 100
        rect1.right = rect1.left + 100

        rect2.top = 150
        rect2.left = 10
        rect2.bottom = rect2.top + 100
        rect2.right = rect2.left + 100

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.setColor(Color.BLACK)

        if (canvas != null) {
            canvas.drawRect(rect1, paint)
            canvas.drawRect(rect2, paint)
        }
    }

    fun setNewCoordinates(posX: Int, posY: Int){
        rect1.top = posX
        rect1.left = posY
        rect1.bottom = rect1.top + 100
        rect1.right = rect1.left + 100

    }


    fun onClick(posX: Int, posY: Int){
        if(rect1.left <= posX && rect1.right >= posX && rect1.top <= posY && rect1.bottom >= posY){
            Toast.makeText(context, "1 rect", Toast.LENGTH_SHORT).show()
        }
        if(rect2.left <= posX && rect2.right >= posX && rect2.top <= posY && rect2.bottom >= posY){
            Toast.makeText(context, "2 rect", Toast.LENGTH_SHORT).show()
        }
        performClick()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }


}
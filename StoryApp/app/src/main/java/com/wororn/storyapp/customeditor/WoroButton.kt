package com.wororn.storyapp.customeditor

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.wororn.storyapp.R

class WoroButton : AppCompatButton {
    private var enabledBackground: Drawable? = null
    private var disabledBackground: Drawable? = null
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = when {
            isEnabled -> enabledBackground
            else -> disabledBackground
        }

        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER

    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, R.color.blue_more_dark)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button)
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable)
    }
}
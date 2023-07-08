package com.wororn.storyapp.customeditor

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet

import androidx.core.content.ContextCompat
import com.wororn.storyapp.R

class PasswordEditText: CustomEditText {
    private lateinit var passwordButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        passwordButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24dp) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (text?.length in 1..7) {
                    error = resources.getString(R.string.invalid_password, 8)
                    setButtonDrawables()
                } else {
                    setButtonDrawables()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setButtonDrawables(startOfTheText: Drawable = passwordButtonImage, topOfTheText: Drawable? = null, endOfTheText: Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }
}
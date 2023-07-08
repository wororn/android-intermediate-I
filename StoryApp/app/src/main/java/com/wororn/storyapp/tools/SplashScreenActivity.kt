package com.wororn.storyapp.tools

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.wororn.storyapp.R
import com.wororn.storyapp.interfaces.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: AppCompatActivity()  {
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
           supportActionBar?.hide()
           hideSystemUI(this.window)
           Handler(Looper.getMainLooper()).postDelayed({
               val intent = Intent(
                   this@SplashScreenActivity,
                   LoginActivity::class.java
               )
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
               startActivity(intent)
               finish()
           }, 3000 )
    }

}
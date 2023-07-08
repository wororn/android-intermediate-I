package com.wororn.storyapp.interfaces.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wororn.storyapp.R
import com.wororn.storyapp.tools.animeView
import com.wororn.storyapp.databinding.ActivityLoginBinding
import com.wororn.storyapp.factory.UsersViewModelFactory
import com.wororn.storyapp.interfaces.main.MainActivity
import com.wororn.storyapp.interfaces.register.RegisterActivity
import com.wororn.storyapp.tools.Status
import com.wororn.storyapp.tools.hideSystemUI

class LoginActivity : AppCompatActivity() {

    private lateinit var loginbunching: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginbunching = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginbunching.root)
        supportActionBar?.hide()
        hideSystemUI(this.window)

        settingViewModel()
        settingAction()
        playAnimation()

        loginbunching.tvRegLogin.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                val intent = Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }, 2000 )
        }
    }

    private fun settingViewModel() {
        val factory: UsersViewModelFactory = UsersViewModelFactory.getInstance(this@LoginActivity)
        loginViewModel = ViewModelProvider(this@LoginActivity, factory)[LoginViewModel::class.java]
        loginViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {

                Toast.makeText(
                    this@LoginActivity,
                    "Information: The Token has already made",
                    Toast.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(
                    this@LoginActivity,
                    MainActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                }, 3500 )
            }
        }
    }

    private fun settingAction() {
        loginbunching.ceEmailLogin.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    validEmail()
                }
            }
        loginbunching.btnLogin.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    noEmptyEmail()
                    noEmptyPassword()
                }
            }
        loginbunching.btnLogin.setOnClickListener {
            val email = loginbunching.ceEmailLogin.text.toString()
            val password = loginbunching.cePasswordLogin.text.toString()

                    loginViewModel.login(email, password).observe(this@LoginActivity) { login ->
                        if (login != null) {
                            when(login) {
                                is Status.Process -> {
                                    showLoading(true)
                                }
                                is Status.Done -> {
                                    showLoading(false)
                                    val data = login.data
                                    if (data.error) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else {
                                        val token = data.loginResult?.token?:""
                                        Log.e("TagLogin", token)

                                        if (token.isNotEmpty()) {

                                            loginViewModel.setToken(token)

                                            val alertBuilder = AlertDialog.Builder(this).create()
                                            alertBuilder.apply {
                                                setTitle("Information")
                                                setMessage(resources.getString(R.string.log_message))
                                                setIcon(R.drawable.ic_baseline_check_green_24dp)
                                                setCancelable(false)
                                            }.show()
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                alertBuilder.dismiss()
                                                val intent = Intent(
                                                    this@LoginActivity,
                                                    MainActivity::class.java
                                                )
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                startActivity(intent)
                                                finish()
                                            }, 2000)
                                        }//token empty
                                         }
                                }//done
                                is Status.Fail -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        resources.getString(R.string.login_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }//fail
                            }//when
                        }
                        else{
                            showLoading(false)
                            Toast.makeText(
                                this@LoginActivity,
                                "Watch Out: The Connection might in Trouble",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

        }
        showLoading(false)
    }

    private fun validEmail(): String {
        val email = loginbunching.ceEmailLogin.text.toString()
       if (email!=="") {
           if (!email.matches(".*[@].*".toRegex())) {
               loginbunching.ceEmailLogin.error = "Must contain at(@) Character"
           }
           if (!email.matches(".*[.].*".toRegex())) {
               loginbunching.ceEmailLogin.error = "Must contain dot(.) Character"
           }
       }
        return email
    }
    private fun noEmptyEmail():String {
        val email = loginbunching.ceEmailLogin.text.toString()
        if(email =="") {
            loginbunching.ceEmailLogin.error = resources.getString(R.string.input_message, "Email")
        }
        return email
    }
    private fun noEmptyPassword():String {
        val password = loginbunching.cePasswordLogin.text.toString()
        if(password =="") {
            loginbunching.cePasswordLogin.error = resources.getString(R.string.input_message, "Password")
        }
        return password
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(loginbunching.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(loginbunching.tvLoginPage, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(loginbunching.tvEmailLogin, View.ALPHA, 1f).setDuration(500)
        val ceEmail = ObjectAnimator.ofFloat(loginbunching.ceEmailLogin, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(loginbunching.tvPasswordLogin, View.ALPHA, 1f).setDuration(500)
        val cePassword = ObjectAnimator.ofFloat(loginbunching.cePasswordLogin, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(loginbunching.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvReg = ObjectAnimator.ofFloat(loginbunching.tvTxtReg, View.ALPHA, 1f).setDuration(500)
        val tvRegLog = ObjectAnimator.ofFloat(loginbunching.tvRegLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvEmail, ceEmail, tvPassword, cePassword, btnLogin, tvReg, tvRegLog)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        loginbunching.apply {
            ceEmailLogin.isEnabled = !isLoading
            cePasswordLogin.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            if (isLoading) {
                progressBarLayout.animeView(true)
            } else {
                progressBarLayout.animeView(false)
            }
        }
    }
}
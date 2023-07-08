package com.wororn.storyapp.interfaces.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wororn.storyapp.R
import com.wororn.storyapp.tools.animeView
import com.wororn.storyapp.databinding.ActivityRegisterBinding
import com.wororn.storyapp.factory.UsersViewModelFactory
import com.wororn.storyapp.interfaces.login.LoginActivity
import com.wororn.storyapp.tools.Status
import com.wororn.storyapp.tools.hideSystemUI


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerbunching: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerbunching = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerbunching.root)
        supportActionBar?.hide()
        hideSystemUI(this.window)

        settingViewModel()
        settingAction()
        playAnimation()

        registerbunching.tvLoginReg.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
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
        val factory: UsersViewModelFactory = UsersViewModelFactory.getInstance(this@RegisterActivity)
        registerViewModel = ViewModelProvider(this@RegisterActivity, factory)[RegisterViewModel::class.java]
    }

    private fun settingAction() {
        registerbunching.ceEmailReg.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    validEmail()
                }
            }
        registerbunching.cePasswordReg.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    validPassword()
                }
            }
        registerbunching.btnRegister.onFocusChangeListener=
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    noEmptyEmail()
                    noEmptyPassword()
                }
            }
        registerbunching.btnRegister.setOnClickListener {
            val name = registerbunching.ceNameReg.text.toString()
            val email = registerbunching.ceEmailReg.text.toString()
            val password = registerbunching.cePasswordReg.text.toString()


                    registerViewModel.register(name, email, password).observe(this@RegisterActivity) { sign ->
                        if (sign != null) {
                            when(sign) {
                                is Status.Process -> {
                                    showLoading(true)
                                }
                                is Status.Done -> {
                                    showLoading(false)
                                    val data = sign.data
                                    if (data.error) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            data.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else {
                                        val alertBuilder = AlertDialog.Builder(this).create()
                                        alertBuilder.apply {
                                            setTitle("Information")
                                            setMessage(resources.getString(R.string.reg_message))
                                            setIcon(R.drawable.ic_baseline_check_green_24dp)
                                            setCancelable(false)
                                            }.show()
                                                Handler(Looper.getMainLooper()).postDelayed({
                                                    alertBuilder.dismiss()
                                                    val intent = Intent(
                                                     this@RegisterActivity,
                                                        LoginActivity::class.java
                                                    )
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                                    startActivity(intent)
                                                    finish()
                                        }, 2000)
                                    }
                                }//done
                                is Status.Fail -> {
                                    showLoading(false)
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        resources.getString(R.string.register_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }//fail
                            }//when
                        }
                        else {
                            showLoading(false)
                            Toast.makeText(
                                this@RegisterActivity,
                                "Watch Out: The Connection might in Trouble",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

        }
        showLoading(false)
    }

    private fun validPassword(): String {
        val password = registerbunching.cePasswordReg.text.toString()

        if (password!=="") {

            if (!password.matches(".*[A-Z].*".toRegex())) {
                registerbunching.cePasswordReg.error = "Must contain 1 Upper-case Character"
            }
            if (!password.matches(".*[a-z].*".toRegex())) {
                registerbunching.cePasswordReg.error = "Must contain 1 Lower-case Character"
            }
            if (!password.matches(".*[0-9].*".toRegex())) {
                registerbunching.cePasswordReg.error = "Must contain 1 Numerical Character"
            }
            if (!password.matches(".*[@#\$%^&*+=!-<>?].*".toRegex())) {
                registerbunching.cePasswordReg.error =
                    "Must contain 1 Special Character(@#\$%^&*+=!-<>?)"
            }
        }
       return password
    }

    private fun validEmail(): String {
        val email = registerbunching.ceEmailReg.text.toString()

        if (email!=="") {

            if (!email.matches(".*[@].*".toRegex())) {
                registerbunching.ceEmailReg.error = "Must contain at(@) Character"
            }
            if (!email.matches(".*[.].*".toRegex())) {
                registerbunching.ceEmailReg.error = "Must contain dot(.) Character"
            }
        }
        return email
    }

    private fun noEmptyEmail():String {
        val email = registerbunching.ceEmailReg.text.toString()
        if(email =="") {
            registerbunching.ceEmailReg.error = resources.getString(R.string.input_message, "Email")
        }
        return email
    }
    private fun noEmptyPassword():String {
        val password = registerbunching.cePasswordReg.text.toString()
        if(password =="") {
            registerbunching.cePasswordReg.error = resources.getString(R.string.input_message, "Password")
        }
        return password
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(registerbunching.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(registerbunching.tvRegPage, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(registerbunching.tvNameReg, View.ALPHA, 1f).setDuration(500)
        val ceName = ObjectAnimator.ofFloat(registerbunching.ceNameReg, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(registerbunching.tvEmailReg, View.ALPHA, 1f).setDuration(500)
        val ceEmail = ObjectAnimator.ofFloat(registerbunching.ceEmailReg, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(registerbunching.tvPasswordReg, View.ALPHA, 1f).setDuration(500)
        val cePassword = ObjectAnimator.ofFloat(registerbunching.cePasswordReg, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(registerbunching.btnRegister, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(registerbunching.tvTxtLogin, View.ALPHA, 1f).setDuration(500)
        val tvLoginReg = ObjectAnimator.ofFloat(registerbunching.tvLoginReg, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvName, ceName, tvEmail, ceEmail, tvPassword, cePassword, btnRegister, tvLogin, tvLoginReg)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        registerbunching.apply {
            ceNameReg.isEnabled = !isLoading
            ceEmailReg.isEnabled = !isLoading
            cePasswordReg.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            if (isLoading) {
                progressBarLayout.animeView(true)
            } else {
                progressBarLayout.animeView(false)
            }
        }
    }
}
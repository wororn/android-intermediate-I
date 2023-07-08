package com.wororn.storyapp.interfaces.setmodes

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.wororn.storyapp.R
import com.wororn.storyapp.factory.UsersViewModelFactory

class ModesActivity :  AppCompatActivity() {

    private lateinit var modesViewModel: ModesViewModel

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_theme)

        supportActionBar?.title = "Set Mode"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switchMode)
        val factory: UsersViewModelFactory = UsersViewModelFactory.getInstance(this)
        modesViewModel = ViewModelProvider(this, factory)[ModesViewModel::class.java]
        modesViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            modesViewModel.saveThemeSetting(isChecked)
        }
     }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}



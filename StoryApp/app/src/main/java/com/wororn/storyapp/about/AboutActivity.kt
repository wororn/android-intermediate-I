package com.wororn.storyapp.about

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.wororn.storyapp.R
import com.wororn.storyapp.factory.StoriesViewModelFactory
import com.wororn.storyapp.factory.UsersViewModelFactory
import com.wororn.storyapp.interfaces.main.*
import com.wororn.storyapp.interfaces.setmodes.ModesActivity
import com.wororn.storyapp.interfaces.setmodes.ModesViewModel

class AboutActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var modesViewModel: ModesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
            this.title = "About"

        settingDarkMode()
    }
    private fun settingDarkMode(){
        val factory: StoriesViewModelFactory = StoriesViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this@AboutActivity, factory)[MainViewModel::class.java]

        val factoryModes: UsersViewModelFactory = UsersViewModelFactory.getInstance(this@AboutActivity)
        modesViewModel = ViewModelProvider(this@AboutActivity, factoryModes)[ModesViewModel::class.java]
        modesViewModel.getThemeSettings().observe(this@AboutActivity
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val addMenu = menu.findItem(R.id.data_stories)
        val aboutMenu = menu.findItem(R.id.about)

        addMenu.isVisible = false
        aboutMenu.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main -> {
                startActivity(Intent(this@AboutActivity, MainActivity::class.java))
                finish()
            }
            R.id.settings -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            R.id.theme_mode -> {
                Intent(this@AboutActivity, ModesActivity::class.java).also{
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(it)
                    }, 2000 )
                }
            }
            R.id.logout -> {
                mainViewModel.logout()
                finish()
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}
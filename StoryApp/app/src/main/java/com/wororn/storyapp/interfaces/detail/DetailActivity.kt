package com.wororn.storyapp.interfaces.detail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wororn.storyapp.R
import com.wororn.storyapp.componen.response.TabStoriesItem
import com.wororn.storyapp.databinding.ActivityDetailBinding
import com.wororn.storyapp.interfaces.main.*
import com.wororn.storyapp.tools.withDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var detailbunching: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailbunching = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailbunching.root)

        this.title = resources.getString(R.string.detail_stories)

        val table = intent.getParcelableExtra<TabStoriesItem>(EXTRA_DATA)
        detailbunching.apply {
            if (table != null) {
                tvNameDetail.text = table.name
                tvCreated.withDateFormat(table.createdAt.toString())
                tvDesc.text = table.description
            }
        }
        Glide.with(this)
            .load(table?.photoUrl)
            .into(detailbunching.imgDetail)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val addMenu = menu.findItem(R.id.data_stories)
        val aboutMenu = menu.findItem(R.id.about)
        val themeMenu = menu.findItem(R.id.theme_mode)

        addMenu.isVisible = false
        aboutMenu.isVisible = false
        themeMenu.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main -> {
                startActivity(Intent(this@DetailActivity, MainActivity::class.java))
                finish()
            }
            R.id.settings -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
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

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}
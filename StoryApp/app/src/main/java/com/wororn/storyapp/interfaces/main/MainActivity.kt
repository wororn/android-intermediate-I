package com.wororn.storyapp.interfaces.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wororn.storyapp.R
import com.wororn.storyapp.about.AboutActivity
import com.wororn.storyapp.adapter.LoadingStateAdapter
import com.wororn.storyapp.adapter.TabStoriesAdapter
import com.wororn.storyapp.databinding.ActivityMainBinding
import com.wororn.storyapp.factory.StoriesViewModelFactory
import com.wororn.storyapp.interfaces.login.LoginActivity
import com.wororn.storyapp.interfaces.stories.DataStoriesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainbunching: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String
    private  var  query:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainbunching = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbunching.root)

        settingViewModel()
    }

    private fun settingViewModel() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainbunching.rvStory.layoutManager = GridLayoutManager(this@MainActivity, 2)
        } else {
            mainbunching.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val factory: StoriesViewModelFactory = StoriesViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this@MainActivity, factory)[MainViewModel::class.java]

        mainViewModel.getToken().observe(this@MainActivity) { token ->
            if (token.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Watch Out: The Token is Empty,You should login again",
                    Toast.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                }, 2000)
            }
        }

        mainViewModel.getToken().observe(this@MainActivity) { token ->
            this.token = token

            Log.e("TagMain", token)
            if (token.isNotEmpty()) {
                val adapter = TabStoriesAdapter()
                mainbunching.rvStory.adapter = adapter.withLoadStateHeaderAndFooter(
                       header = LoadingStateAdapter {
                       adapter.retry()
                        },
                        footer = LoadingStateAdapter {
                           adapter.retry()
                        }
                )
                mainViewModel.searchStory(token,query).observe(this@MainActivity) { main ->
                 adapter.submitData(lifecycle, main)
                }

                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = mainbunching.Searchview

                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = getString(R.string.Cari)
                showLoading(false)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null && query !="") {
                            showLoading(true)
                            mainbunching.rvStory.scrollToPosition(0)
                            mainViewModel.searchQuery(query)
                            searchView.clearFocus()
                        }
                        else {
                            Toast.makeText(
                                this@MainActivity,
                                "Watch Out: The Query is Empty,You should login again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showLoading(false)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }

                })
                showLoading(false)
            }//token
        }//mainview
    }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_main, menu)

            val modeMenu = menu.findItem(R.id.theme_mode)
            modeMenu.isVisible = false
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.data_stories -> {
                    startActivity(Intent(this@MainActivity, DataStoriesActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                    true
                }
                R.id.logout -> {
                    mainViewModel.logout()
                    finish()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }

        private fun showLoading(isLoading: Boolean) {
            mainbunching.progressBarLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

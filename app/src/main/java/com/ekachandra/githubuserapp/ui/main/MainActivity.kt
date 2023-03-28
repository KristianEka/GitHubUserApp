package com.ekachandra.githubuserapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekachandra.githubuserapp.R
import com.ekachandra.githubuserapp.data.local.entity.User
import com.ekachandra.githubuserapp.data.remote.response.ItemsItem
import com.ekachandra.githubuserapp.databinding.ActivityMainBinding
import com.ekachandra.githubuserapp.helper.SettingPreferences
import com.ekachandra.githubuserapp.ui.adapters.UsersAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingPreferences.PreferencesKeys.THEME_SETTING_KEY)
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(
            application, SettingPreferences.getInstance(dataStore)
        )
    }

    private val currentNightMode = AppCompatDelegate.getDefaultNightMode()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.apply {
            username.observe(this@MainActivity) { username ->
                setUserData(username)
            }

            isLoading.observe(this@MainActivity) {
                showLoading(it)
            }

            isError.observe(this@MainActivity) {
                if (it) {
                    Toast.makeText(
                        this@MainActivity, getString(R.string.api_failed), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

    }

    private fun setUserData(userProfiles: List<ItemsItem>) {
        val listUsers = ArrayList<User>()

        for (user in userProfiles) {
            listUsers.add(User(user.login, user.avatarUrl))
        }

        val adapter = UsersAdapter(listUsers)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.findUsers(query.toString())
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val themeChanger = menu.findItem(R.id.theme_changer)
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            themeChanger.setIcon(R.drawable.ic_light_mode_24)
        } else {
            themeChanger.setIcon(R.drawable.ic_night_mode_24)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.favorite -> {
                val favIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favIntent)
            }

            R.id.theme_changer -> {
                if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    mainViewModel.saveThemeSetting(true)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    mainViewModel.saveThemeSetting(false)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
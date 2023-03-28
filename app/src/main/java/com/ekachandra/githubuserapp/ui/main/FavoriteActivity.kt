package com.ekachandra.githubuserapp.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekachandra.githubuserapp.data.local.entity.User
import com.ekachandra.githubuserapp.databinding.ActivityFavoriteBinding
import com.ekachandra.githubuserapp.helper.SettingPreferences
import com.ekachandra.githubuserapp.ui.adapters.UsersAdapter
import com.ekachandra.githubuserapp.ui.insert.FavoriteViewModel


class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingPreferences.PreferencesKeys.THEME_SETTING_KEY)
    private val favViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application, SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favViewModel.getAllFavoriteUser().observe(this) {
            val items = ArrayList<User>()

            for (user in it) {
                items.add(User(user.username, user.profilePicture))
            }
            binding.rvFavUsers.layoutManager = LinearLayoutManager(this)
            binding.rvFavUsers.adapter = UsersAdapter(items)
        }

    }
}
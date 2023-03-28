package com.ekachandra.githubuserapp.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ekachandra.githubuserapp.R
import com.ekachandra.githubuserapp.data.local.entity.User
import com.ekachandra.githubuserapp.data.remote.response.DetailUserResponse
import com.ekachandra.githubuserapp.databinding.ActivityDetailAcivityBinding
import com.ekachandra.githubuserapp.helper.SettingPreferences
import com.ekachandra.githubuserapp.ui.adapters.SectionsPagerAdapter
import com.ekachandra.githubuserapp.ui.insert.FavoriteViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAcivityBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingPreferences.PreferencesKeys.THEME_SETTING_KEY)

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(
            application, SettingPreferences.getInstance(dataStore)
        )
    }

    private val favViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(
            application, SettingPreferences.getInstance(dataStore)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAcivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("DEPRECATION") val data = intent.getParcelableExtra<User>(USER)

        if (data != null && savedInstanceState == null) {
            mainViewModel.getUser(data.username)
            favViewModel.getFavoriteUserByUsername(data.username)
        }

        mainViewModel.apply {
            detailUsername.observe(this@DetailActivity) { username ->
                setUserDetailData(username)
            }

            isLoading.observe(this@DetailActivity) { loading ->
                showLoading(loading)
            }

            isError.observe(this@DetailActivity) {
                if (it) {
                    Toast.makeText(
                        this@DetailActivity, getString(R.string.api_failed), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        favViewModel.favoriteUser.observe(this) { user ->
            if (user == null) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_white_24)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_white_24)
            }
        }

        binding.fabFavorite.setOnClickListener {
            val isFavorite = favViewModel.favoriteUser.value != null
            if (isFavorite) {
                favViewModel.delete(data as User)
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_white_24)
            } else {
                favViewModel.insert(data as User)
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_white_24)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, data?.username.toString())
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    @SuppressLint("SetTextI18n")
    private fun setUserDetailData(username: DetailUserResponse) {

        binding.apply {
            userNameDetail.text = username.name
            userUsernameDetail.text = username.login
            userFollower.text = resources.getString(R.string.followers_count, username.followers)
            userFollowing.text = resources.getString(R.string.following_count, username.following)

        }
        Glide.with(this)
            .load(username.avatarUrl)
            .into(binding.userDetailPhoto)

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val USER = "kristian"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower, R.string.following
        )
    }

}
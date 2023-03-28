package com.ekachandra.githubuserapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ekachandra.githubuserapp.data.remote.response.DetailUserResponse
import com.ekachandra.githubuserapp.data.remote.response.GithubResponse
import com.ekachandra.githubuserapp.data.remote.response.ItemsItem
import com.ekachandra.githubuserapp.data.remote.retrofit.ApiConfig
import com.ekachandra.githubuserapp.helper.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(val application: Application, private val pref: SettingPreferences) :
    ViewModel() {
    private val _username = MutableLiveData<List<ItemsItem>>()
    val username: LiveData<List<ItemsItem>> = _username

    private val _detailUsername = MutableLiveData<DetailUserResponse>()
    val detailUsername: LiveData<DetailUserResponse> = _detailUsername

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isLoadingFollow = MutableLiveData<Boolean>()
    val isLoadingFollow: LiveData<Boolean> = _isLoadingFollow

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isErrorFollow = MutableLiveData<Boolean>()
    val isErrorFollow: LiveData<Boolean> = _isErrorFollow

    init {
        findUsers(USERNAME)
    }

    fun findUsers(name: String) {
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().searchUsers(name)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _username.value = response.body()?.items
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    fun getUser(username: String) {
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().getUsername(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUsername.value = response.body()
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoadingFollow.value = true
        _isErrorFollow.value = false
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    _isErrorFollow.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollow.value = false
                _isErrorFollow.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoadingFollow.value = true
        _isErrorFollow.value = false
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    _isErrorFollow.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollow.value = false
                _isErrorFollow.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val USERNAME = "kristian"
    }

}


package com.ekachandra.githubuserapp.ui.insert

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekachandra.githubuserapp.data.FavoriteRepository
import com.ekachandra.githubuserapp.data.local.entity.User
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _favoriteUser = MutableLiveData<User?>()
    val favoriteUser: LiveData<User?> = _favoriteUser

    fun getFavoriteUserByUsername(user: String) {
        mFavoriteRepository.getFavoriteUserByUsername(user).observeForever { username ->
            _favoriteUser.value = username
        }
    }

    fun getAllFavoriteUser() = mFavoriteRepository.getAllFavoriteUser()

    fun insert(user: User) {
        viewModelScope.launch {
            mFavoriteRepository.insert(user)
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            mFavoriteRepository.delete(user)
        }
    }
}


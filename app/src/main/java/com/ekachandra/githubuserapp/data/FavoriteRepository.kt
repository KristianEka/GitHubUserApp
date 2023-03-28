package com.ekachandra.githubuserapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.ekachandra.githubuserapp.data.local.entity.User
import com.ekachandra.githubuserapp.data.local.room.FavoriteDao
import com.ekachandra.githubuserapp.data.local.room.FavoriteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(application: Application) {

    private val mFavoriteDao: FavoriteDao

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    suspend fun insert(user: User) {
        withContext(Dispatchers.IO) {
            mFavoriteDao.insert(user)
        }
    }

    suspend fun delete(user: User) {
        withContext(Dispatchers.IO) {
            mFavoriteDao.delete(user)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<User?> {
        return mFavoriteDao.getFavoriteUserByUsername(username)
    }

    fun getAllFavoriteUser(): LiveData<List<User>> {
        return mFavoriteDao.getAllFavoriteUser()
    }

}
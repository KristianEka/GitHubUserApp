package com.ekachandra.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ekachandra.githubuserapp.data.local.entity.User

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: User)

    @Delete
    suspend fun delete(favorite: User)

    @Query("SELECT * FROM User WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<User?>

    @Query("SELECT * FROM User")
    fun getAllFavoriteUser(): LiveData<List<User>>
}
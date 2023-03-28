package com.ekachandra.githubuserapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ekachandra.githubuserapp.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private const val DATABASE_NAME = "favorite_database"

        @Volatile
        private var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, FavoriteDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

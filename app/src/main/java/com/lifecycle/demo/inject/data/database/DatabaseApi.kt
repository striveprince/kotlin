package com.lifecycle.demo.inject.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lifecycle.demo.inject.data.database.dao.UserDao
import com.lifecycle.demo.inject.data.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class DatabaseApi:RoomDatabase(){
    abstract fun getUserDao():UserDao
    companion object{
        const val DATABASE_NAME: String = "app_db"
    }
}
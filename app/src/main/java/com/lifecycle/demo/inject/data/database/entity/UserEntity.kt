package com.lifecycle.demo.inject.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (
    @PrimaryKey
    val id:Int,
    @ColumnInfo
    val password:String
)
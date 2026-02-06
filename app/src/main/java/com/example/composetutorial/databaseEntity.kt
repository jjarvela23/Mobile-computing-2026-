package com.example.composetutorial

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class databaseEntity {
    @Entity
    data class User(
        val username: String,
        val profPic: String,
        @PrimaryKey(autoGenerate = true)
        val uid: Int = 0
    )
}
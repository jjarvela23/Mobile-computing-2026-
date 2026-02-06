package com.example.composetutorial

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composetutorial.databaseEntity.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

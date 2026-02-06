package com.example.composetutorial

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.composetutorial.databaseEntity.User

    @Dao
    interface UserDao {
        @Query("SELECT * FROM user")
        fun getAll(): List<User>

        //@Query("SELECT * FROM user WHERE uid IN (:userIds)")
        //fun loadAllByIds(userIds: IntArray): List<User>

        @Query("SELECT * FROM user WHERE username LIKE :first LIMIT 1")
        fun findByName(first: String): User

        @Insert
        fun insertAll(vararg users: User)

        @Query("SELECT username FROM user WHERE :uid = 0")
        fun selectFirst(uid: Int): String

        @Delete
        fun delete(user: User)

        @Insert
        fun insertUser(user: User)
    }
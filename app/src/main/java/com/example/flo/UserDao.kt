package com.example.flo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email AND  password = :password")
    fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM UserTable WHERE id = :userIdx")
    fun getUserByIdx(userIdx: Int): User?

    @Query("SELECT * FROM UserTable WHERE email = :email")
    fun existUser(email: String) : User?


}
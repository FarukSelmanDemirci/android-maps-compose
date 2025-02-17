package com.google.maps.android.compose.theme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.google.maps.android.compose.UserData

@Dao
interface UserDao {
  //  @Insert
  //  suspend fun registerUser(user: User)
  @Insert
  suspend fun insertUser(user: UserData)
   // @Query("SELECT * FROM User WHERE username=:username AND password=:password")
  //  fun validateUser(username: String, password: String): User?
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    fun validateUser(username: String, password: String): UserData?
   // fun registerUser(user: User)

}
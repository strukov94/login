package com.strukov.login.login.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface LoginDao {
    @Query("SELECT id FROM users_table WHERE token = :token")
    Single<List<Integer>> userExist(String token);

    @Insert
    Single<Long> insertUser(Users user);

    @Update
    Single<Integer> updateUser(Users users);
}
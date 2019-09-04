package com.strukov.login.login.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class}, version = 1)
public abstract class LoginDatabase extends RoomDatabase {
    public abstract LoginDao loginDao();
}

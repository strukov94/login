package com.strukov.login.login.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users_table", indices = @Index("token"))
public class Users {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int mId;

    @ColumnInfo(name = "token")
    @SerializedName("token")
    private String mToken;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String mName;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    private String mEmail;

    @Ignore
    public Users() {}

    public Users(int id, String token, String name, String email) {
        this.mId = id;
        this.mToken = token;
        this.mName = name;
        this.mEmail = email;
    }

    public int getId() {
        return mId;
    }

    public String getToken() {
        return mToken;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setId(int id) {
        mId = id;
    }
}

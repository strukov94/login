package com.strukov.login.login.models;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    @SerializedName("id")
    private int mId;
    @SerializedName("token")
    private String mToken;
    @SerializedName("access_token")
    private String mAccessToken;
    @SerializedName("refresh_token")
    private String mRefreshToken;

    public LoginUser(int id, String token, String accessToken, String refreshToken) {
        this.mId = id;
        this.mToken = token;
        mAccessToken = accessToken;
        mRefreshToken = refreshToken;
    }

    public int getId() {
        return mId;
    }

    public String getToken() {
        return mToken;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.mRefreshToken = refreshToken;
    }
}

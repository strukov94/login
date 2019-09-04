package com.strukov.login.login.models;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("access_token")
    private String mToken;

    public AccessToken(String token) {
        this.mToken = token;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }
}

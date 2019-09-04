package com.strukov.login.login.models;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName(value = "token")
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }
}

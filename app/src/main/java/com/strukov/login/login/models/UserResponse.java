package com.strukov.login.login.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("response")
    private User[] mResponse;

    public User[] getResponse () {
        return mResponse;
    }

    public void setResponse (User[] response) {
        mResponse = response;
    }
}

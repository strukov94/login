package com.strukov.login.login.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int mId;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("bdate")
    private String mBirthDate;

    public void setId(int id) {
        this.mId = id;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.mBirthDate = birthDate;
    }

    public int getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getBirthDate() {
        return mBirthDate;
    }

    public String getFullName() {
        return mFirstName + " " + mLastName;
    }
}

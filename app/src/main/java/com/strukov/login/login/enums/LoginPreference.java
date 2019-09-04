package com.strukov.login.login.enums;

public enum LoginPreference {
    PREF_TOKEN("pref_token"),
    PREF_USER_ID("pref_user_id"),
    PREF_ACCESS_TOKEN("access_token"),
    PREF_REFRESH_TOKEN("refresh_token");

    private final String name;

    LoginPreference(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }
}

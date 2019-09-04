package com.strukov.login.login.utils.net;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.strukov.login.login.enums.LoginPreference;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private SharedPreferences mPreferences;

    public HeaderInterceptor(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (chain.request().header("Authorization") != null) {
            return chain.proceed(chain.request());
        } else {
            Request request = chain.request();
            Request requestHeader = request.newBuilder()
                    .addHeader(
                            "Authorization",
                            "Bearer " + mPreferences.getString(LoginPreference.PREF_ACCESS_TOKEN.name(), ""))
                    .build();
            return chain.proceed(requestHeader);
        }
    }
}
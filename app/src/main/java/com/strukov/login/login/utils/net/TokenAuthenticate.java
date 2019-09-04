package com.strukov.login.login.utils.net;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strukov.login.login.enums.LoginPreference;
import com.strukov.login.login.models.AccessToken;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticate implements Authenticator {
    private SharedPreferences mPreferences;

    public TokenAuthenticate(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {
        if (response.code() == 401) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://example.com")
                    .header(
                            "Authorization",
                            "Bearer " + mPreferences.getString(LoginPreference.PREF_REFRESH_TOKEN.name(), ""))
                    .build();

            Response responseRefresh = client.newCall(request).execute();

            if (responseRefresh.isSuccessful() && responseRefresh.body() != null) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                AccessToken accessToken = gson.fromJson(responseRefresh.body().string(), AccessToken.class);

                mPreferences.edit().putString(LoginPreference.PREF_ACCESS_TOKEN.name(), accessToken.getToken()).apply();

                return response.request().newBuilder()
                        .header("Authorization",
                                "Bearer " + accessToken.getToken())
                        .build();
            }
        }
        return null;
    }
}
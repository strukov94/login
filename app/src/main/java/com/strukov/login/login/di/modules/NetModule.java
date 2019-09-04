package com.strukov.login.login.di.modules;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strukov.login.login.api.LoginApi;
import com.strukov.login.login.utils.net.HeaderInterceptor;
import com.strukov.login.login.utils.net.TokenAuthenticate;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    private String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(SharedPreferences preferences) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(preferences))
                .authenticator(new TokenAuthenticate(preferences))
                .build();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory providesRxAdapter() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient, RxJava2CallAdapterFactory factory) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(factory)
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    LoginApi providesLoginApi(Retrofit retrofit) {
        return retrofit.create(LoginApi.class);
    }
}
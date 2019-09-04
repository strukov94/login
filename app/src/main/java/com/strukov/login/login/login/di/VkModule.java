package com.strukov.login.login.login.di;

import com.google.gson.Gson;
import com.strukov.login.login.api.VkApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class VkModule {

    private String baseUrl;

    public VkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @LoginScope
    @Provides
    @Named("login")
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient, RxJava2CallAdapterFactory factory) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(factory)
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @LoginScope
    VkApi providesVkApi(@Named("login") Retrofit retrofit) {
        return retrofit.create(VkApi.class);
    }
}
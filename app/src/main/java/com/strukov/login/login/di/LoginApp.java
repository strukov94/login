package com.strukov.login.login.di;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.strukov.login.login.di.component.AppComponent;
import com.strukov.login.login.di.component.DaggerAppComponent;
import com.strukov.login.login.di.modules.AppModule;
import com.strukov.login.login.di.modules.DataModule;
import com.strukov.login.login.di.modules.NetModule;
import com.strukov.login.login.login.di.LoginComponent;
import com.strukov.login.login.login.di.LoginModule;
import com.strukov.login.login.login.di.VkModule;
import com.vk.sdk.VKSdk;

public class LoginApp extends Application {
    private static AppComponent sAppComponent;
    private static LoginComponent sLoginComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        VKSdk.initialize(this);

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .netModule(new NetModule("https://www.example.ru/"))
                .build();
    }

    public static LoginApp getApp(Context context) {
        return (LoginApp) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return sAppComponent;
    }

    public LoginComponent getLoginComponent() {
        return sLoginComponent == null ?
                sLoginComponent = getAppComponent().createLoginComponent(new LoginModule(), new VkModule("https://api.vk.com/method/")) : sLoginComponent;
    }

    public void releaseLoginComponent() {
        sLoginComponent = null;
    }
}
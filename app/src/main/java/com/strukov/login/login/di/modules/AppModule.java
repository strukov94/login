package com.strukov.login.login.di.modules;

import android.content.Context;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context mContext;

    public AppModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mContext;
    }
}

package com.strukov.login.login.di.component;

import com.strukov.login.login.di.modules.AppModule;
import com.strukov.login.login.di.modules.DataModule;
import com.strukov.login.login.di.modules.NetModule;
import com.strukov.login.login.login.di.LoginComponent;
import com.strukov.login.login.login.di.LoginModule;
import com.strukov.login.login.login.di.VkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class, NetModule.class})
public interface AppComponent {
    LoginComponent createLoginComponent(LoginModule module, VkModule vkModule);
}

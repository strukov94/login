package com.strukov.login.login.login.di;

import com.strukov.login.login.login.LoginActivity;

import dagger.Subcomponent;

@LoginScope
@Subcomponent(modules = {LoginModule.class, VkModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
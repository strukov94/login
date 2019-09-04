package com.strukov.login.login.login;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface LoginPresenter {
    void onCreate(LoginView view);

    void onDestroy();

    void onClickSignInGoogle();

    void firebaseAuthWithGoogle(Activity activity, GoogleSignInAccount acct);

    void firebaseAuthWithCustomToken(Activity activity, String userId, String accessToken, String email);

    void onTransfer();

    void onNotTransfer();

    boolean isBlock();
}

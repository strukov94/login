package com.strukov.login.login.login.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.strukov.login.login.api.LoginApi;
import com.strukov.login.login.api.VkApi;
import com.strukov.login.login.data.LoginDao;
import com.strukov.login.login.data.LoginDatabase;
import com.strukov.login.login.dialog.GuestDialogFragment;
import com.strukov.login.login.login.LoginModel;
import com.strukov.login.login.login.LoginModelImpl;
import com.strukov.login.login.login.LoginPresenter;
import com.strukov.login.login.login.LoginPresenterImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class LoginModule {
    @LoginScope
    @Provides
    GoogleSignInOptions providesGoogleSignInOptions(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("")
                .requestEmail()
                .build();
    }

    @Provides
    @LoginScope
    LoginDao providesPddDao(LoginDatabase database) {
        return database.loginDao();
    }

    @LoginScope
    @Provides
    FirebaseAuth providesFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @LoginScope
    @Provides
    GoogleSignInClient providesGoogleSignInClient(Context context, GoogleSignInOptions gso) {
        return GoogleSignIn.getClient(context, gso);
    }

    @LoginScope
    @Provides
    GuestDialogFragment providesGuestDialogFragment() {
        return new GuestDialogFragment();
    }

    @LoginScope
    @Provides
    LoginModel providesLoginModel(LoginApi api, VkApi vKapi, LoginDao dao, SharedPreferences preferences) {
        return new LoginModelImpl(api, vKapi, dao, preferences);
    }

    @LoginScope
    @Provides
    @Named("login")
    CompositeDisposable providesCompositeDisposable() {
        return new CompositeDisposable();
    }

    @LoginScope
    @Provides
    LoginPresenter providesLoginPresenter(Context context, LoginModel model, FirebaseAuth auth, GoogleSignInClient client,
                                          @Named("login") CompositeDisposable disposable) {
        return new LoginPresenterImpl(context, model, auth, client, disposable);
    }
}
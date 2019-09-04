package com.strukov.login.login.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.strukov.login.R;
import com.strukov.login.login.data.Users;
import com.strukov.login.login.utils.crashlytics.Crashlytics;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter {
    private static final String TAG = LoginPresenterImpl.class.getSimpleName();
    private Context mContext;
    private LoginModel mModel;
    private LoginView mView;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mClient;
    private CompositeDisposable mDisposable;
    private boolean mBlock;

    public LoginPresenterImpl(Context context, LoginModel model,
                              FirebaseAuth auth, GoogleSignInClient client, CompositeDisposable disposable) {
        this.mContext = context;
        this.mModel = model;
        this.mAuth = auth;
        this.mClient = client;
        this.mDisposable = disposable;
    }

    @Override
    public void onCreate(LoginView view) {
        this.mView = view;
    }

    @Override
    public void onDestroy() {
        if (!mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }

    @Override
    public void onClickSignInGoogle() {
        Intent intent = mClient.getSignInIntent();
        mView.startActivityForResult(intent);
    }

    @Override
    public void firebaseAuthWithCustomToken(Activity activity, String userId, String accessToken, String email) {
        mDisposable.add(
                mModel.getUserToken(userId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(token -> {
                            mAuth.signInWithCustomToken(token.getToken())
                                    .addOnCompleteListener(
                                            activity,
                                            task -> onCompleteAuth(task, "CustomToken", accessToken, email));
                        })
        );
    }

    @Override
    public void firebaseAuthWithGoogle(Activity activity, GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> onCompleteAuth(task, "Google", null, null));
    }

    @Override
    public void onTransfer() {

    }

    @Override
    public void onNotTransfer() {

    }

    @Override
    public boolean isBlock() {
        return mBlock;
    }

    private void onCompleteAuth(Task<AuthResult> task, String signInWith, String accessToken, String email) {
        if (task.isSuccessful()) {
            if (accessToken != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    updateUserName(user, accessToken, email);
                }
            } else {
                createUser(null);
            }
        } else {
            mView.authFailure();
            Crashlytics.logException(task.getException());
        }
    }

    private void updateUserName(FirebaseUser user, String accessToken, String email) {
        mDisposable.add(
                mModel.getUserInfo(
                        user.getUid(),
                        accessToken,
                        mContext.getString(R.string.vk_version))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(response.body().getResponse()[0].getFullName())
                                                    .build();

                                            user.updateProfile(profile)
                                                    .addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            createUser(email);
                                                        } else {
                                                            Crashlytics.logException(task.getException());
                                                        }
                                                    });
                                        } else {
                                            mView.authFailure();
                                        }
                                    }
                                }, Crashlytics::logException
                        )
        );
    }

    private void createUser(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            mDisposable.add(
                    mModel.userExist(user.getUid())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    integers -> {
                                        addUser(
                                                new Users(
                                                        0,
                                                        user.getUid(),
                                                        user.getDisplayName(),
                                                        email == null ? user.getEmail() : email
                                                ),
                                                integers.size() != 0
                                        );

                                        setToken(user.getUid());
                                    }, Crashlytics::logException
                            )
            );
        }
    }

    private void addUser(Users user, boolean exist) {
        mDisposable.add(
                mModel.addUser(user)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                examPddUser -> {
                                    user.setId(examPddUser.getId());
                                    if (exist) {
                                        updateUser(user);
                                    } else {
                                        insertUser(user);
                                    }
                                    setUserData(
                                            examPddUser.getId(),
                                            examPddUser.getAccessToken(),
                                            examPddUser.getRefreshToken()
                                    );
                                }, Crashlytics::logException
                        )
        );
    }

    private void insertUser(Users user) {
        mDisposable.add(
                mModel.insertUser(user)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                longs -> {},
                                Crashlytics::logException)
        );
    }

    private void updateUser(Users user) {
        mDisposable.add(
                mModel.updateUser(user)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                longs -> {},
                                Crashlytics::logException)
        );
    }

    private void setToken(String token) {
        mModel.setToken(token)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                    }
                });
    }

    private void setUserData(int userId, String accessToken, String refreshToken) {
        mModel.setUserId(userId)
                .andThen(mModel.setJWT(accessToken, refreshToken))
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                        mView.showGuestDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                    }
                });
    }
}
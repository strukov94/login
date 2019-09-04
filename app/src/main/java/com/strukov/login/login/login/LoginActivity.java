package com.strukov.login.login.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.strukov.login.R;
import com.strukov.login.login.dialog.GuestDialogFragment;
import com.strukov.login.login.di.LoginApp;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {
    @Inject
    GuestDialogFragment guestDialog;

    @Inject
    LoginPresenter presenter;

    @BindView(R.id.progress_login)
    RelativeLayout viewLogin;

    @BindView(R.id.button_sign_in_google)
    Button buttonGoogle;

    @BindView(R.id.button_sign_in_vk)
    Button buttonVk;

    private static final int RC_SIGN_IN = 1001;
    private static final String CLICK = "click_button";
    private boolean mIsClickButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        LoginApp.getApp(this).getLoginComponent().inject(this);

        presenter.onCreate(this);

        buttonGoogle.setOnClickListener(this);
        buttonVk.setOnClickListener(this);

        if (savedInstanceState != null) {
            mIsClickButton = savedInstanceState.getBoolean(CLICK);
            if (mIsClickButton) {
                buttonGoogle.setClickable(false);
                buttonVk.setClickable(false);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@androidx.annotation.NonNull Bundle outState) {
        outState.putBoolean(CLICK, mIsClickButton);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            LoginApp.getApp(this).releaseLoginComponent();
        }
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent) {
        super.startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.firebaseAuthWithGoogle(this, account);
            } catch (ApiException e) {
                Snackbar.make(findViewById(R.id.view_sign_buttons), R.string.sign_failed, Snackbar.LENGTH_SHORT).show();
                mIsClickButton = false;
                buttonGoogle.setClickable(true);
                buttonVk.setClickable(true);
            }
        } else {
            if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    presenter.firebaseAuthWithCustomToken(LoginActivity.this, res.userId, res.accessToken, res.email);
                }

                @Override
                public void onError(VKError error) {
                    Snackbar.make(findViewById(R.id.view_sign_buttons), R.string.sign_failed, Snackbar.LENGTH_SHORT).show();
                    mIsClickButton = false;
                    buttonGoogle.setClickable(true);
                    buttonVk.setClickable(true);
                }
            })) {

            }
        }
    }

    @Override
    public void authSuccess() {
        finish();
    }

    @Override
    public void authFailure() {
        Snackbar.make(findViewById(R.id.view_sign_buttons), R.string.auth_failure, Snackbar.LENGTH_SHORT).show();
        mIsClickButton = false;
        buttonGoogle.setClickable(true);
        buttonVk.setClickable(true);
    }

    @Override
    public void syncError() {
        new Handler(getMainLooper()).post(() -> {
            Snackbar.make(findViewById(R.id.view_sign_buttons), R.string.sync_error, Snackbar.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void showGuestDialog() {
        guestDialog.setCancelable(false);
        guestDialog.show(getSupportFragmentManager(), "GuestDialogFragment");
    }

    @Override
    public void onTransfer() {
        presenter.onTransfer();
        viewLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNotTransfer() {
        presenter.onNotTransfer();
        viewLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (presenter.isBlock()) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in_google:
                mIsClickButton = true;
                buttonGoogle.setClickable(false);
                buttonVk.setClickable(false);
                presenter.onClickSignInGoogle();
                break;
            case R.id.button_sign_in_vk:
                mIsClickButton = true;
                buttonVk.setClickable(false);
                buttonGoogle.setClickable(false);
                VKSdk.login(this, VKScope.EMAIL);
                break;
        }
    }
}
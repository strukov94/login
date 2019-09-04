package com.strukov.login.login.login;

import android.content.Intent;

import com.strukov.login.login.dialog.GuestDialogFragment;

public interface LoginView extends GuestDialogFragment.GuestDialogListener {
    void startActivityForResult(Intent intent);

    void authSuccess();

    void authFailure();

    void syncError();

    void showGuestDialog();
}

package com.strukov.login.login.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.strukov.login.R;

public class GuestDialogFragment extends DialogFragment {
    private Context mContext;
    private GuestDialogListener mListener;

    public GuestDialogFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;

        try {
            mListener = (GuestDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement GuestDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder
                .setMessage(R.string.dialog_guest_title)
                .setCancelable(false)
                .setNegativeButton(R.string.dialog_guest_cancel, ((dialogInterface, i) -> {
                    mListener.onNotTransfer();
                }))
                .setPositiveButton(R.string.dialog_guest_ok, ((dialogInterface, i) -> {
                    mListener.onTransfer();
                }));

        return builder.create();
    }

    public interface GuestDialogListener {
        void onTransfer();

        void onNotTransfer();
    }
}
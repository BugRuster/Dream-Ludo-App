package com.definiteautomation.dreamludo.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.definiteautomation.dreamludo.listner.ProgressListener;

public class ProgressBar implements ProgressListener {
    private final ProgressDialog dialog;

    public ProgressBar(Context context, boolean isCancelable) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.setMessage("Please wait...");
    }

    @Override
    public void showProgressDialog() {
        if (dialog != null) {
            try {
                dialog.show();
            }
            catch (WindowManager.BadTokenException|IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hideProgressDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            }
            catch (WindowManager.BadTokenException|IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }
}

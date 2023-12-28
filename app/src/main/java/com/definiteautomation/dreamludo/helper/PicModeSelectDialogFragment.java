package com.definiteautomation.dreamludo.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class PicModeSelectDialogFragment extends DialogFragment {

    private final String[] picMode = {AppConstant.PicModes.CAMERA, AppConstant.PicModes.GALLERY};

    private IPicModeSelectListener iPicModeSelectListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(picMode, (dialog, which) -> {
            if (iPicModeSelectListener != null)
                iPicModeSelectListener.onPicModeSelected(picMode[which]);
        });
        return builder.create();
    }

    public void setIPicModeSelectListener(IPicModeSelectListener iPicModeSelectListener) {
        this.iPicModeSelectListener = iPicModeSelectListener;
    }

    public interface IPicModeSelectListener {
        void onPicModeSelected(String mode);
    }
}

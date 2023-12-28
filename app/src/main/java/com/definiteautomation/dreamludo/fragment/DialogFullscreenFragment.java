package com.definiteautomation.dreamludo.fragment;

import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.view.TouchImageView;

public class DialogFullscreenFragment extends DialogFragment {

    public TouchImageView photoIv;
    public ImageButton closeBt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fullscreen, container, false);

        Window window = requireActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(requireActivity().getResources().getColor(R.color.black));

        photoIv = view.findViewById(R.id.photoIv);
        closeBt = view.findViewById(R.id.closeBt);

        Bundle bundle;
        bundle = getArguments();
        if (bundle != null) {
            Glide.with(requireActivity()).load(bundle.getString("POST_KEY"))
                    .apply(new RequestOptions().override(120,120))
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_post).error(R.drawable.placeholder_post))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .into(photoIv);
        }

        closeBt.setOnClickListener(v -> dismiss());

        return view;
    }
}
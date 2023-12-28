package com.definiteautomation.dreamludo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;

import java.text.MessageFormat;
import java.util.Objects;

public class ReferralActivity extends AppCompatActivity {

    public TextView noteTv, referTv;
    public Button shareBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        noteTv = findViewById(R.id.noteTv);
        referTv = findViewById(R.id.referTv);
        shareBt = findViewById(R.id.shareBt);

        noteTv.setText(MessageFormat.format("Invite your friends to play our app and you''ll get {0}% of the joining fees everytime your referred user join a paid contest above {1}{2}. This will be added to your bonus balance so, you can use it to join any contest.", AppConstant.REFERRAL_PERCENTAGE, AppConstant.CURRENCY_SIGN, AppConstant.MIN_JOIN_LIMIT));
        referTv.setText(Preferences.getInstance(this).getString(Preferences.KEY_USERNAME));

        shareBt.setOnClickListener(v -> Function.shareApp(ReferralActivity.this, referTv.getText().toString()));
    }
}
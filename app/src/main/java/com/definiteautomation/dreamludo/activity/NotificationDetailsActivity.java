package com.definiteautomation.dreamludo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

public class NotificationDetailsActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public CollapsingToolbarLayout collapsingToolbar;

    public TextView titleTv, dateTv;
    public WebView webView;
    public Button viewMoreBt;
    public ImageView imageIv;

    public String title,description,image,url,created;
    private boolean isWhichScreenNotification;

    public SharedPreferences preferences;
    public String prefName = "Ludo";
    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.personal_collapsed_title);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.personal_expanded_title);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        viewMoreBt = findViewById(R.id.viewMoreBt);
        imageIv = findViewById(R.id.imageIv);
        webView = findViewById(R.id.webView);

        Intent intent = getIntent();
        isWhichScreenNotification = intent.getBooleanExtra("isNotification", false);
        if (!isWhichScreenNotification) {
            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            image = intent.getStringExtra("image");
            url = intent.getStringExtra("url");
            created = intent.getStringExtra("created");

            try {
                if (!image.equals("null")) {
                    Glide.with(NotificationDetailsActivity.this).load(AppConstant.API_URL+ image)
                            .apply(new RequestOptions().override(720,480))
                            .apply(new RequestOptions().placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .into(imageIv);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
        else {
            getCounter();
            saveCounter(count);

            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            image = intent.getStringExtra("image");
            url = intent.getStringExtra("url");
            created = intent.getStringExtra("created");

            try {
                if (!image.equals("null")) {
                    Glide.with(NotificationDetailsActivity.this).load(AppConstant.API_URL+ image)
                            .apply(new RequestOptions().override(720,480))
                            .apply(new RequestOptions().placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .into(imageIv);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
        toolbar.setTitle(title);
        titleTv.setText(title);
        dateTv.setText(created);
        webView.setBackgroundColor(0);
        webView.loadData(description,"text/html", "UTF-8");

        if (!url.isEmpty()) {
            if (!url.equals("false")) {
                viewMoreBt.setVisibility(View.VISIBLE);
            }
            else {
                viewMoreBt.setVisibility(View.GONE);
            }
        }
        else {
            viewMoreBt.setVisibility(View.GONE);
        }

        viewMoreBt.setOnClickListener(v -> {
            try {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(NotificationDetailsActivity.this, Uri.parse(url));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void saveCounter(int count) {
        count = count-1;

        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("counter", count);
        editor.apply();
    }

    public void getCounter() {
        preferences = this.getSharedPreferences(prefName, 0);
        count = preferences.getInt("counter", 0);
    }

    @Override
    public void onBackPressed() {
        if (!isWhichScreenNotification) {
            super.onBackPressed();

        } else {
            Intent intent = new Intent(NotificationDetailsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
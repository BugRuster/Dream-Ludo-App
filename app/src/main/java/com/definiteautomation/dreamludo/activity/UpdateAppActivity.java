package com.definiteautomation.dreamludo.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.Preferences;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateAppActivity extends AppCompatActivity {

    private String updateURL;
    public String updatedOn;
    public String whatsNewData;
    public String isForceUpdate = "0";
    public String latestVersion;

    public Button later;
    public Button update;
    public TextView updateDate;
    public WebView whatsNew;
    public TextView newVersion;
    public TextView forceUpdateNote;

    public int code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);

        updateDate = findViewById(R.id.date);
        newVersion = findViewById(R.id.version);
        whatsNew = findViewById(R.id.whatsnew);
        forceUpdateNote = findViewById(R.id.forceUpdateNote);
        later = findViewById(R.id.laterButton);
        update = findViewById(R.id.updateButton);

        isForceUpdate = getIntent().getStringExtra("forceUpdate");
        updateURL = getIntent().getStringExtra("updateURL");
        updatedOn = getIntent().getStringExtra("updateDate");
        whatsNewData = getIntent().getStringExtra("whatsNew");
        latestVersion = getIntent().getStringExtra("latestVersionName");

        updateDate.setText(String.format("Updated On: %s", updatedOn));
        newVersion.setText(String.format("New Version: v%s", latestVersion));

        whatsNew.setBackgroundColor(0);
        if (whatsNewData != null){
            String htmlData="<font color='white'>" + whatsNewData + "</font>";
            whatsNew.loadData(Base64.encodeToString(htmlData.getBytes(), Base64.NO_PADDING),"text/html", "base64");
        }

        whatsNew.setOnClickListener(v -> openWebPage(updateURL));

        if (isForceUpdate.equals("1")) {
            later.setVisibility(View.GONE);
            forceUpdateNote.setVisibility(View.VISIBLE);
        }

        update.setOnClickListener(v -> openWebPage(updateURL));

        later.setOnClickListener(v -> {
            if (Preferences.getInstance(UpdateAppActivity.this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                webpage = Uri.parse("http://" + url);
            }
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install verifyDownload web browser or check your URL.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

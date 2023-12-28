package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.ConfigurationModel;

import java.util.List;
import java.util.Objects;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;
    public String pageSt;

    private ProgressBar progressBar;
    private ApiCalling api;

    Handler handler = new Handler(Looper.getMainLooper(), message -> {
        // Your code logic goes here.
        if (message.what == 1) {
            webViewGoBack();
        }
        return true;
    });

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        webView = findViewById(R.id.webView);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                handler.sendEmptyMessage(1);
                return true;
            }
            return false;
        });

        if (getIntent().getExtras() != null) {
            pageSt = getIntent().getExtras().getString("PAGE_KEY");
            toolbar.setTitle(pageSt);

            switch (pageSt) {
                case "FAQ":
                    getFAQ();
                    break;
                case "Privacy Policy":
                    getPrivacyPolicy();
                    break;
                case "legal":
                    getLegalPolicy();
                    break;
                case "About Us":
                    getAboutUs();
                    break;
                case "Terms & Conditions":
                    getTermsCondition();
                    break;
            }
        }

    }

    private void getFAQ() {
        progressBar.showProgressDialog();

        Call<ConfigurationModel> call = api.getFAQ(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            webView.loadDataWithBaseURL(null, res.get(0).getFaq(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void getPrivacyPolicy() {
        progressBar.showProgressDialog();

        Call<ConfigurationModel> call = api.getPrivacyPolicy(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            webView.loadDataWithBaseURL(null, res.get(0).getPrivacy_policy(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void getLegalPolicy() {
        progressBar.showProgressDialog();

        Call<ConfigurationModel> call = api.getLegalPolicy(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            webView.loadDataWithBaseURL(null, res.get(0).getLegal_policy(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void getAboutUs() {
        progressBar.showProgressDialog();

        Call<ConfigurationModel> call = api.getAboutUs(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            webView.loadDataWithBaseURL(null, res.get(0).getAbout_us(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void getTermsCondition() {
        progressBar.showProgressDialog();

        Call<ConfigurationModel> call = api.getTermsCondition(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            webView.loadDataWithBaseURL(null, res.get(0).getTerms_condition(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }


    private void webViewGoBack(){
        webView.goBack();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.hideProgressDialog();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(WebviewActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
        }
    }

}
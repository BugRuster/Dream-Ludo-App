package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.definiteautomation.dreamludo.BuildConfig;
import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.model.AppModel;
import com.definiteautomation.dreamludo.model.UserModel;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ApiCalling api;
    private TextView statusTv;
    private String forceUpdate, whatsNew, updateDate, latestVersionName, latestVersionCode, updateUrl;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        api = MyApplication.getRetrofit().create(ApiCalling.class);

        changeStatusBarColor();
        printHashKey();

        statusTv = findViewById(R.id.statusTv);

        if(Function.checkNetworkConnection(SplashActivity.this)) {
            if (Preferences.getInstance(SplashActivity.this).getString(Preferences.KEY_USER_ID) != null) {
                updateUserProfileFCM();
            }

            getAppDetails();
        }
        else {
            statusTv.setText("No internet Connection, please try again later.");
        }

    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
    }

    private void printHashKey() {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void updateUserProfileFCM() {
        if (Function.checkNetworkConnection(SplashActivity.this)) {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get new FCM registration token
                    String token = task.getResult();

                    Call<UserModel> callToken = api.updateUserProfileToken(AppConstant.PURCHASE_KEY, Preferences.getInstance(SplashActivity.this).getString(Preferences.KEY_USER_ID), token);
                    callToken.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {

                        }

                        @Override
                        public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {

                        }
                    });
                }
            });
        }
    }

    private void getAppDetails() {
        Call<AppModel> call = api.getAppDetails(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<AppModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AppModel> call, @NonNull Response<AppModel> response) {
                if (response.isSuccessful()) {
                    AppModel legalData = response.body();
                    List<AppModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            AppConstant.COUNTRY_CODE = res.get(0).getCountry_code();
                            AppConstant.CURRENCY_CODE = res.get(0).getCurrency_code();
                            AppConstant.CURRENCY_SIGN = res.get(0).getCurrency_sign();
                            AppConstant.PAYTM_M_ID = res.get(0).getPaytm_mer_id();
                            AppConstant.PAYU_M_ID = res.get(0).getPayu_id();
                            AppConstant.PAYU_M_KEY = res.get(0).getPayu_key();
                            AppConstant.MIN_JOIN_LIMIT = res.get(0).getMin_entry_fee();
                            AppConstant.REFERRAL_PERCENTAGE = res.get(0).getRefer_percentage();
                            AppConstant.MAINTENANCE_MODE = res.get(0).getMaintenance_mode();
                            AppConstant.MODE_OF_PAYMENT = res.get(0).getMop();
                            AppConstant.WALLET_MODE = res.get(0).getWallet_mode();
                            AppConstant.MIN_WITHDRAW_LIMIT = res.get(0).getMin_withdraw();
                            AppConstant.MAX_WITHDRAW_LIMIT = res.get(0).getMax_withdraw();
                            AppConstant.MIN_DEPOSIT_LIMIT = res.get(0).getMin_deposit();
                            AppConstant.MAX_DEPOSIT_LIMIT = res.get(0).getMax_deposit();
                            AppConstant.GAME_NAME = res.get(0).getGame_name();
                            AppConstant.PACKAGE_NAME = res.get(0).getPackage_name();
                            AppConstant.HOW_TO_PLAY = res.get(0).getHow_to_play();
                            AppConstant.SUPPORT_EMAIL = res.get(0).getCus_support_email();
                            AppConstant.SUPPORT_MOBILE = res.get(0).getCus_support_mobile();

                            forceUpdate = res.get(0).getForce_update();
                            whatsNew = res.get(0).getWhats_new();
                            updateDate = res.get(0).getUpdate_date();
                            latestVersionName = res.get(0).getLatest_version_name();
                            latestVersionCode = res.get(0).getLatest_version_code();
                            updateUrl = res.get(0).getUpdate_url();

                            try {
                                if (BuildConfig.VERSION_CODE < Integer.parseInt(latestVersionCode)) {
                                    if (forceUpdate.equals("1")) {
                                        Intent intent = new Intent(SplashActivity.this, UpdateAppActivity.class);
                                        intent.putExtra("forceUpdate", forceUpdate);
                                        intent.putExtra("whatsNew", whatsNew);
                                        intent.putExtra("updateDate", updateDate);
                                        intent.putExtra("latestVersionName", latestVersionName);
                                        intent.putExtra("updateURL", updateUrl);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    } else if (forceUpdate.equals("0")) {
                                        Intent intent = new Intent(SplashActivity.this, UpdateAppActivity.class);
                                        intent.putExtra("forceUpdate", forceUpdate);
                                        intent.putExtra("whatsNew", whatsNew);
                                        intent.putExtra("updateDate", updateDate);
                                        intent.putExtra("latestVersionName", latestVersionName);
                                        intent.putExtra("updateURL", updateUrl);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                                else if (AppConstant.MAINTENANCE_MODE == 0) {
                                    new Handler().postDelayed(() -> {
                                        if (Preferences.getInstance(SplashActivity.this).getString(Preferences.KEY_IS_AUTO_LOGIN).equals("1")) {
                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            intent.putExtra("finish", true);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                            intent.putExtra("finish", true);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        finish();
                                    },1000);
                                }
                                else {
                                    statusTv.setText("App is under maintenance, please try again later.");
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppModel> call, @NonNull Throwable t) {

            }
        });
    }
}
package com.definiteautomation.dreamludo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflinePayActivity extends AppCompatActivity {

    public RadioButton payTmRb, payuRb, flutterWaveRb;
    private TextInputEditText nameEt, numberEt, amountEt;
    public TextView accountIdTv, signTv, noteTv, alertTv;
    private Button submitBt;

    private ProgressBar progressBar;
    private ApiCalling api;

    private String nameSt;
    private String numberSt;
    private String amountSt;
    private String mopSt;
    public double deposit = 0, winning = 0, bonus = 0, total = 0;
    public String orderIdSt, paymentIdSt, checksumSt, tokenSt;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_pay);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getUserDetails();

        accountIdTv = findViewById(R.id.accountIdTv);
        payTmRb = findViewById(R.id.payTmRb);
        payuRb = findViewById(R.id.payuRb);
        flutterWaveRb = findViewById(R.id.flutterWaveRb);
        nameEt = findViewById(R.id.nameEt);
        numberEt = findViewById(R.id.numberEt);
        amountEt = findViewById(R.id.amountEt);
        noteTv = findViewById(R.id.noteTv);
        alertTv = findViewById(R.id.alertTv);
        signTv = findViewById(R.id.signTv);
        submitBt = findViewById(R.id.submitBt);

        signTv.setText(AppConstant.CURRENCY_SIGN);
        alertTv.setText(String.format("Minimum Request Amount is %s%d", AppConstant.CURRENCY_SIGN, AppConstant.MIN_DEPOSIT_LIMIT));

        if (payTmRb.isChecked()) {
            accountIdTv.setText("01850491731");
            mopSt = "Rocket";
        } else if (payuRb.isChecked()) {
            accountIdTv.setText("01719887035");
            mopSt = "Bkash";
        } else if (flutterWaveRb.isChecked()) {
            accountIdTv.setText("01719887035");
            mopSt = "Nagad";
        }

        payTmRb.setOnClickListener(v -> {
            accountIdTv.setText("01850491731");
            mopSt = "Rocket";
        });

        payuRb.setOnClickListener(v -> {
            accountIdTv.setText("01719887035");
            mopSt = "Bkash";
        });

        flutterWaveRb.setOnClickListener(v -> {
            accountIdTv.setText("01719887035");
            mopSt = "Nagad";
        });

        submitBt.setOnClickListener(v -> {
            submitBt.setEnabled(false);
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Random rand = new Random();
            int min =1000, max= 9999;

            // nextInt as provided by Random is exclusive of the top value so you need to add 1
            int randomNum = rand.nextInt((max - min) + 1) + min;
            orderIdSt = System.currentTimeMillis() + randomNum + Preferences.getInstance(this).getString(Preferences.KEY_USER_ID);

            nameSt = Objects.requireNonNull(nameEt.getText()).toString();
            paymentIdSt = Objects.requireNonNull(numberEt.getText()).toString();
            amountSt = Objects.requireNonNull(amountEt.getText()).toString();
            if (!amountSt.isEmpty()) {
                double payout = Integer.parseInt(amountEt.getText().toString());

                if (nameSt.isEmpty()){
                    submitBt.setEnabled(true);
                    alertTv.setVisibility(View.VISIBLE);
                    alertTv.setText("Please Enter Valid Name");
                    alertTv.setTextColor(Color.parseColor("#ff0000"));
                } else if (paymentIdSt.length() < 5 || paymentIdSt.length() > 20){
                    submitBt.setEnabled(true);
                    alertTv.setVisibility(View.VISIBLE);
                    alertTv.setText("Please Enter Valid Transaction Number");
                    alertTv.setTextColor(Color.parseColor("#ff0000"));
                } else if (payout < AppConstant.MIN_DEPOSIT_LIMIT) {
                    submitBt.setEnabled(true);
                    alertTv.setVisibility(View.VISIBLE);
                    alertTv.setText(String.format("Minimum Request Amount is %s%d", AppConstant.CURRENCY_SIGN, AppConstant.MIN_DEPOSIT_LIMIT));
                    alertTv.setTextColor(Color.parseColor("#ff0000"));
                } else if (payout > AppConstant.MAX_DEPOSIT_LIMIT) {
                    submitBt.setEnabled(true);
                    alertTv.setVisibility(View.VISIBLE);
                    alertTv.setText(String.format("Maximum Request Amount is %s%d", AppConstant.CURRENCY_SIGN, AppConstant.MAX_DEPOSIT_LIMIT));
                    alertTv.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    alertTv.setVisibility(View.GONE);
                    try {
                        submitBt.setEnabled(false);
                        postRequestDeposit();
                    } catch (NullPointerException e) {
                        submitBt.setEnabled(true);
                    }
                }
            } else {
                submitBt.setEnabled(true);
                alertTv.setVisibility(View.VISIBLE);
                alertTv.setText(String.format("Minimum Request Amount is %s%d", AppConstant.CURRENCY_SIGN, AppConstant.MIN_DEPOSIT_LIMIT));
                alertTv.setTextColor(Color.parseColor("#ff0000"));
            }
        });
    }

    private void getUserDetails() {
        Call<UserModel> call = api.getUserDetails(AppConstant.PURCHASE_KEY, Preferences.getInstance(this).getString(Preferences.KEY_USER_ID));
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel legalData = response.body();
                    List<UserModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            deposit = res.get(0).getDeposit_bal();
                            winning = res.get(0).getWon_bal();
                            bonus = res.get(0).getBonus_bal();
                            total = deposit + winning + bonus;

                            if (res.get(0).getIs_block() == 1) {
                                Preferences.getInstance(OfflinePayActivity.this).setString(Preferences.KEY_IS_AUTO_LOGIN,"0");

                                Intent i = new Intent(OfflinePayActivity.this, LoginActivity.class);
                                i.putExtra("finish", true);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                            else if (res.get(0).getIs_active() == 0) {
                                Preferences.getInstance(OfflinePayActivity.this).setString(Preferences.KEY_IS_AUTO_LOGIN,"0");

                                Intent i = new Intent(OfflinePayActivity.this, LoginActivity.class);
                                i.putExtra("finish", true);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void postRequestDeposit() {
        checksumSt = "123";
        progressBar.showProgressDialog();
        Call<UserModel> call = api.postBalance(AppConstant.PURCHASE_KEY, Preferences.getInstance(this).getString(Preferences.KEY_USER_ID), orderIdSt, paymentIdSt, checksumSt, Double.parseDouble(amountSt), mopSt );
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    UserModel legalData = response.body();
                    List<UserModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        Function.showToast(OfflinePayActivity.this, res.get(0).getMsg());
                        onBackPressed();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }
}
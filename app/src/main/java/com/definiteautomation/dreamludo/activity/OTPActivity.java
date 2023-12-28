package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.UserModel;
import com.goodiebag.pinview.Pinview;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private Pinview otpView;
    private TextView reEnterTv, timerTv;

    private String mobileSt, pageSt, deviceIdSt, tokenSt;
    public int counter = 60;

    private ProgressBar progressBar;
    private ApiCalling api;

    private static final String TAG = "PhoneAuth";
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth auth;

    public DatabaseReference mDatabase;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        auth = FirebaseAuth.getInstance();
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(OTPActivity.this, false);
        deviceIdSt = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        changeStatusBarColor();

        if (getIntent().getExtras() != null) {
            pageSt = getIntent().getExtras().getString("PAGE_KEY");
        }

        otpView = findViewById(R.id.otpView);
        reEnterTv = findViewById(R.id.reEnterTv);
        timerTv = findViewById(R.id.timerTv);

        setUpVerificationCallbacks();
        mobileSt ="+" + Preferences.getInstance(this).getString(Preferences.KEY_COUNTRY_CODE) + Preferences.getInstance(this).getString(Preferences.KEY_MOBILE);
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(mobileSt)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(verificationCallbacks)
                        .build());

        new CountDownTimer(60000, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                timerTv.setText(String.format("Resend in %ds", counter));
                timerTv.setVisibility(View.VISIBLE);
                reEnterTv.setVisibility(View.GONE);
                counter--;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                counter = 60;
                reEnterTv.setText("Resend");
                reEnterTv.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.GONE);
            }
        }.start();
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
    }

    public void onLoginClick(View view){
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void onSubmitClick(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        verifyCode();
    }

    public void onResendClick(View view){
        resendVerificationCode(resendToken);
        new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timerTv.setText("Resend in "+ counter + "s");
                timerTv.setVisibility(View.VISIBLE);
                reEnterTv.setVisibility(View.GONE);
                counter--;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                counter = 60;
                reEnterTv.setText("Resend");
                reEnterTv.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.GONE);
            }
        }.start();

    }


    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d(TAG, "Invalid credential: " + e.getLocalizedMessage());
                    Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // SMS quota exceeded
                    Log.d(TAG, "SMS Quota exceeded.");
                    Toast.makeText(OTPActivity.this, "SMS Quota exceeded.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NotNull String verificationId, @NotNull PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                resendToken = token;

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                if (pageSt.equals("Register")) {
                    if(Function.checkNetworkConnection(OTPActivity.this)) {
                        if (!Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_REFER_CODE).equals("")) {
                            customerRegistrationWithRefer();
                        } else {
                            customerRegistrationWithoutRefer();
                        }
                    }
                }
                else {
                    Function.showToast(OTPActivity.this, "Otp is verified..");
                    Function.fireIntent(OTPActivity.this, ResetActivity.class);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                }

            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(OTPActivity.this, "verification code entered was invalid.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verifyCode() {
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code = otpView.getValue();
        if (code != null) {
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }catch (IllegalArgumentException | NullPointerException e){
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(OTPActivity.this, "verification code entered was invalid.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken token) {
        // ForceResendingToken from callbacks
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(mobileSt)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(verificationCallbacks)
                        .setForceResendingToken(token)
                        .build());
    }


    private void customerRegistrationWithRefer() {
        progressBar.showProgressDialog();

        FirebaseMessaging.getInstance ().getToken ().addOnCompleteListener ( task -> {
            if (!task.isSuccessful ()) {
                //Could not get FirebaseMessagingToken
                return;
            }
            if (null != task.getResult ()) {
                //Got FirebaseMessagingToken
                tokenSt = Objects.requireNonNull ( task.getResult () );
                //Use firebaseMessagingToken further
                Call<UserModel> call = api.customerRegistrationWithRefer(AppConstant.PURCHASE_KEY, Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_FULL_NAME), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_USERNAME), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_EMAIL), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_COUNTRY_CODE), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_MOBILE), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_PASSWORD), tokenSt, deviceIdSt, Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_REFER_CODE));
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel legalData = response.body();
                            List<UserModel.Result> res;
                            if (legalData != null) {
                                res = legalData.getResult();
                                if (res.get(0).getSuccess() == 1) {
                                    registerUser(res.get(0).getId());

                                    Function.showToast(OTPActivity.this, res.get(0).getMsg());

                                    Preferences.getInstance(OTPActivity.this).setString(Preferences.KEY_USER_ID, res.get(0).getId());
                                    Preferences.getInstance(OTPActivity.this).setString(Preferences.KEY_PROFILE_PHOTO, "");
                                    Preferences.getInstance(OTPActivity.this).setString(Preferences.KEY_IS_AUTO_LOGIN, "1");

                                    Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                    intent.putExtra("finish", true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                                } else {
                                    progressBar.hideProgressDialog();
                                    Function.showToast(OTPActivity.this, res.get(0).getMsg());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                        progressBar.hideProgressDialog();
                    }
                });
            }
        } );
    }

    private void customerRegistrationWithoutRefer() {
        progressBar.showProgressDialog();

        FirebaseMessaging.getInstance ().getToken ().addOnCompleteListener ( task -> {
            if (!task.isSuccessful ()) {
                //Could not get FirebaseMessagingToken
                return;
            }
            if (null != task.getResult ()) {
                //Got FirebaseMessagingToken
                tokenSt = Objects.requireNonNull ( task.getResult () );
                //Use firebaseMessagingToken further
                Call<UserModel> call = api.customerRegistrationWithoutRefer(AppConstant.PURCHASE_KEY, Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_FULL_NAME), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_USERNAME), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_EMAIL), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_COUNTRY_CODE), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_MOBILE), Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_PASSWORD), tokenSt, deviceIdSt);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel legalData = response.body();
                            List<UserModel.Result> res;
                            if (legalData != null) {
                                res = legalData.getResult();
                                if (res.get(0).getSuccess() == 1) {
                                    registerUser(res.get(0).getId());
                                    progressBar.hideProgressDialog();
                                    Function.showToast(OTPActivity.this, res.get(0).getMsg());

                                    Preferences.getInstance(OTPActivity.this).setString(Preferences.KEY_USER_ID, res.get(0).getId());
                                    Preferences.getInstance(OTPActivity.this).setString(Preferences.KEY_PROFILE_PHOTO, "");
                                    Preferences.getInstance(OTPActivity.this).setString(Preferences.KEY_IS_AUTO_LOGIN, "1");

                                    Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                    intent.putExtra("finish", true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                                } else {
                                    progressBar.hideProgressDialog();
                                    Function.showToast(OTPActivity.this, res.get(0).getMsg());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                        progressBar.hideProgressDialog();
                    }
                });
            }
        } );
    }

    private void registerUser(String id) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);


        FirebaseMessaging.getInstance ().getToken ().addOnCompleteListener (task -> {
            if (!task.isSuccessful ()) {
                //Could not get FirebaseMessagingToken
                return;
            }
            if (null != task.getResult ()) {
                //Got FirebaseMessagingToken
                String device_token = Objects.requireNonNull ( task.getResult () );
                //Use firebaseMessagingToken further
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("name", Preferences.getInstance(OTPActivity.this).getString(Preferences.KEY_FULL_NAME));
                userMap.put("status", "Hi there I'm using Ludo King Cash App.");
                userMap.put("image", "default");
                userMap.put("thumb_image", "default");
                userMap.put("device_token", device_token);
                userMap.put("online", String.valueOf(System.currentTimeMillis()));

                mDatabase.setValue(userMap).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Intent mainIntent = new Intent(OTPActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();

                    }

                });
            }
        } );
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBar.hideProgressDialog();
    }
}
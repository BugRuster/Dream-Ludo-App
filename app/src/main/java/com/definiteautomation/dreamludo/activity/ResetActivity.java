package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.UserModel;

import java.util.List;
import java.util.Objects;

public class ResetActivity extends AppCompatActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    public Button submitButton;

    private String strNewPassword, strConfirmPassword;

    private ProgressBar progressBar;
    private ApiCalling api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        changeStatusBarColor();

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            try {
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            strNewPassword = editTextNewPassword.getText().toString().trim();
            strConfirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (strNewPassword.equals("") && strConfirmPassword.equals("")) {
                Function.showToast(ResetActivity.this, "All fields are mandatory");
            } else if (strNewPassword.equals("")) {
                Function.showToast(ResetActivity.this, "Please enter password");
            }
            else if (strNewPassword.length() < 7) {
                Function.showToast(ResetActivity.this, "Minimum 6 character");
            } else if (!strNewPassword.equals(strConfirmPassword)) {
                Function.showToast(ResetActivity.this, "Password mismatch");
            }
            else {
                if((Function.checkNetworkConnection(ResetActivity.this))) {
                    userResetPassword();
                }
            }

        });

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

    private void userResetPassword() {
        progressBar.showProgressDialog();

        Call<UserModel> call = api.userResetPassword(AppConstant.PURCHASE_KEY, Preferences.getInstance(ResetActivity.this).getString(Preferences.KEY_MOBILE), strConfirmPassword);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel legalData = response.body();
                    List<UserModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            Function.showToast(ResetActivity.this, res.get(0).getMsg());

                            Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                            intent.putExtra("finish", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                        } else {
                            progressBar.hideProgressDialog();
                            Function.showToast(ResetActivity.this, res.get(0).getMsg());
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
}
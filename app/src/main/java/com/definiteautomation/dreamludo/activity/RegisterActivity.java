package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private CoordinatorLayout parentLyt;
    private EditText editTextName, editTextEmail, editTextMobile, editTextPassword, editTextReferral;
    public ImageView btnFB, btnGoogle;
    private CountryCodePicker countryCodePicker;

    private String strName, strUsername, strEmail, strCountryCode, strMobile, strPassword, strReferral, strDeviceId;
    public String emailPattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private ProgressBar progressBar;
    private ApiCalling api;

    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    private static final int REQ_CODE =9001;
    private static final String TAG = "SignInActivity";

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    public LinearLayout bottomSheetNumber;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_register);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);
        strDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        changeStatusBarColor();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextReferral = findViewById(R.id.editTextReferral);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        parentLyt = findViewById(R.id.parentLyt);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFB = findViewById(R.id.btnFb);

        if (getIntent().getExtras() != null) {
            strName = getIntent().getExtras().getString("FULL_NAME_KEY");
            strUsername = getIntent().getExtras().getString("USERNAME_KEY");
            strEmail = getIntent().getExtras().getString("EMAIL_KEY");
            strPassword = getIntent().getExtras().getString("PASSWORD_KEY");

            try {
                if (strName != null) {
                    editTextName.setText(strName);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (strEmail != null) {
                    editTextEmail.setText(strEmail);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (strPassword != null) {
                    editTextPassword.setText(strPassword);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        btnFB.setOnClickListener(v -> {
            if (Function.checkNetworkConnection(RegisterActivity.this)) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if (isLoggedIn){
                    disconnectFromFacebook();
                }

                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        runOnUiThread(() -> setFacebookData(loginResult));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(RegisterActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(RegisterActivity.this, "ERROR" + exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, options);

        btnGoogle.setOnClickListener(v -> {
            if (Function.checkNetworkConnection(RegisterActivity.this)) {
                try {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                    if (googleSignInClient != null && account != null) {
                        disconnectFromGoogle();
                    }
                    signIn();
                } catch (Exception e) {
                    Log.d("DISCONNECT ERROR", e.toString());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            }
        });

        bottomSheetNumber = findViewById(R.id.bottom_sheet_number);
        mBehavior = BottomSheetBehavior.from(bottomSheetNumber);
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

    public void onMainClick(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        strName = editTextName.getText().toString().trim();
        strEmail = editTextEmail.getText().toString().trim();
        strCountryCode = countryCodePicker.getSelectedCountryCode();
        strMobile = editTextMobile.getText().toString().trim();
        strPassword = editTextPassword.getText().toString().trim();
        strReferral = editTextReferral.getText().toString().trim();

        if (strName.equals("") && strEmail.equals("") && strMobile.equals("") && strPassword.equals("")) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else if(!strEmail.matches(emailPattern)) {
            Toast.makeText(this, "Enter valid email id", Toast.LENGTH_SHORT).show();
        } else if (strPassword.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (strPassword.length() < 7) {
            Toast.makeText(this, "Minimum 6 character", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (strEmail != null){
                    String[] resArray = strEmail.split("@");
                    strUsername = resArray[0];
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                e.printStackTrace();
            }

            showBottomSheetDialog();
        }

    }

    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.layout_phone_number, null);

        CardView cvApplyCouponSocial;
        EditText etPhoneBotom, etCouponSocial;
        TextView imgRemoveCouponSocial, promoAppliedTextSocial, txtApplySocial;
        CountryCodePicker ccp;
        LinearLayout cardSubmitNumber;
        ImageView imgCheckSocial;

        etPhoneBotom = view.findViewById(R.id.editTextMobile);
        etCouponSocial = view.findViewById(R.id.et_coupon_social);
        cvApplyCouponSocial = view.findViewById(R.id.cv_apply_coupon_social);
        imgRemoveCouponSocial = view.findViewById(R.id.img_remove_coupon_social);
        promoAppliedTextSocial = view.findViewById(R.id.promo_applied_text_social);
        txtApplySocial = view.findViewById(R.id.txt_apply_social);
        ccp = view.findViewById(R.id.countryCodePicker);
        cardSubmitNumber = view.findViewById(R.id.card_submit_number);
        imgCheckSocial = view.findViewById(R.id.img_check_social);

        imgRemoveCouponSocial.setOnClickListener(v -> {
            cvApplyCouponSocial.setVisibility(View.VISIBLE);
            imgRemoveCouponSocial.setVisibility(View.GONE);
            promoAppliedTextSocial.setVisibility(View.GONE);
            imgCheckSocial.setVisibility(View.GONE);
            etCouponSocial.setText("");
            promoAppliedTextSocial.setText("");
            strReferral = "";
        });

        txtApplySocial.setOnClickListener(v -> {
            strReferral = etCouponSocial.getText().toString().trim();

            if (!strReferral.isEmpty()) {
                Call<UserModel> call = api.verifyUserRefer(AppConstant.PURCHASE_KEY, strReferral);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel legalData = response.body();
                            List<UserModel.Result> res;
                            if (legalData != null) {
                                res = legalData.getResult();
                                if (res.get(0).getSuccess() == 1) {
                                    progressBar.hideProgressDialog();
                                    promoAppliedTextSocial.setText(String.format("%s refer code applied", strReferral));

                                    try {
                                        cvApplyCouponSocial.setVisibility(View.GONE);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                    try {
                                        promoAppliedTextSocial.setVisibility(View.VISIBLE);
                                        imgRemoveCouponSocial.setVisibility(View.VISIBLE);
                                        imgCheckSocial.setVisibility(View.VISIBLE);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }

                                    Function.showToast(RegisterActivity.this, res.get(0).getMsg());
                                } else {
                                    progressBar.hideProgressDialog();
                                    try {
                                        cvApplyCouponSocial.setVisibility(View.VISIBLE);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                    try {
                                        promoAppliedTextSocial.setVisibility(View.GONE);
                                        imgRemoveCouponSocial.setVisibility(View.GONE);
                                        imgCheckSocial.setVisibility(View.GONE);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                    etCouponSocial.setText("");
                                    strReferral = "";

                                    Function.showToast(RegisterActivity.this, res.get(0).getMsg());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                        progressBar.hideProgressDialog();

                        try {
                            cvApplyCouponSocial.setVisibility(View.VISIBLE);
                        }catch (NullPointerException e1){
                            e1.printStackTrace();
                        }
                        try {
                            imgRemoveCouponSocial.setVisibility(View.GONE);
                            imgRemoveCouponSocial.setVisibility(View.GONE);
                            imgCheckSocial.setVisibility(View.GONE);
                        }catch (NullPointerException e2){
                            e2.printStackTrace();
                        }
                        etCouponSocial.setText("");
                        imgRemoveCouponSocial.setText("");
                        strReferral = "";
                    }
                });
            }
        });

        (view.findViewById(R.id.img_close)).setOnClickListener(view1 -> mBottomSheetDialog.dismiss());

        cardSubmitNumber.setOnClickListener(v -> {
            cardSubmitNumber.setEnabled(false);
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            strMobile = etPhoneBotom.getText().toString().trim();
            strCountryCode = ccp.getSelectedCountryCode().trim();
            strReferral = etCouponSocial.getText().toString().trim();

            if (!strCountryCode.isEmpty() && !strMobile.isEmpty()) {
                progressBar.showProgressDialog();

                Call<UserModel> call = api.verifyUserRegister(AppConstant.PURCHASE_KEY, strDeviceId, strMobile, strEmail, strUsername);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            UserModel legalData = response.body();
                            List<UserModel.Result> res;
                            if (legalData != null) {
                                res = legalData.getResult();
                                if (res.get(0).getSuccess() == 1) {
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_FULL_NAME, strName);
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_USERNAME, strUsername);
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_EMAIL, strEmail);
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_COUNTRY_CODE, strCountryCode);
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_MOBILE, strMobile);
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_PASSWORD, strPassword);
                                    Preferences.getInstance(RegisterActivity.this).setString(Preferences.KEY_REFER_CODE, strReferral);

                                    cardSubmitNumber.setEnabled(true);
                                    progressBar.hideProgressDialog();

                                    try {
                                        mBottomSheetDialog.dismiss();
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }

                                    Intent i = new Intent(RegisterActivity.this, OTPActivity.class);
                                    i.putExtra("PAGE_KEY","Register");
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    Function.fireIntentWithData(RegisterActivity.this, i);
                                    overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                                } else {
                                    progressBar.hideProgressDialog();
                                    Function.showToast(RegisterActivity.this, res.get(0).getMsg());

                                    cardSubmitNumber.setEnabled(true);
                                    disconnectFromFacebook();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                        progressBar.hideProgressDialog();
                        cardSubmitNumber.setEnabled(true);
                    }
                });
            }
            else {
                cardSubmitNumber.setEnabled(true);
                Snackbar snackbar = Snackbar.make(parentLyt,"Mobile Number should be of 8 to 13 Digits",Snackbar.LENGTH_LONG);
                View view12 = snackbar.getView();
                CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams) view12.getLayoutParams();
                params.gravity = Gravity.TOP;
                view12.setLayoutParams(params);
                snackbar.show();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        try {
            mBottomSheetDialog.show();
        }catch (WindowManager.BadTokenException e){
            e.printStackTrace();
        }
        mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
    }

    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                (object, response) -> {
                    if (object != null && response != null) {
                        try {
                            Log.i("Response", response.toString());

                            try {
                                if (!object.getString("id").equals("null")){
                                    strPassword = object.getString("id");
                                }
                            } catch (JSONException | NullPointerException  e) {
                                e.printStackTrace();
                            }

                            try {
                                if (!object.getString("name").equals("null")){
                                    strName = object.getString("name");
                                    editTextName.setText(strName);
                                }
                            } catch (JSONException | NullPointerException  e) {
                                e.printStackTrace();
                            }

                            try {
                                if (!object.getString("email").equals("null")){
                                    strEmail = object.getString("email");
                                    editTextEmail.setText(strEmail);
                                }
                            } catch (JSONException | NullPointerException  e) {
                                e.printStackTrace();
                            }

                            try{
                                if (!object.getString("email").equals("null")) {
                                    String[] resArray = strEmail.split("@");
                                    strUsername = resArray[0];
                                }
                            }catch (JSONException | ArrayIndexOutOfBoundsException | NullPointerException e){
                                e.printStackTrace();
                            }
                        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,name,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            try {
                strName = account.getDisplayName();
                strEmail = account.getEmail();
                strPassword = account.getId();

                try {
                    if (strEmail != null){
                        String[] resArray1 = strEmail.split("@");
                        strUsername = resArray1[0];
                    }
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    e.printStackTrace();
                }

                try{
                    try {
                        if (strName != null) {
                            editTextName.setText(strName);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (strEmail != null) {
                            editTextEmail.setText(strEmail);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        disconnectFromFacebook();
        disconnectFromGoogle();
    }

    @Override
    public void onPause() {
        super.onPause();
        disconnectFromGoogle();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
        super.onActivityResult(requestCode, responseCode, intent);

        //Google login
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            handleSignInResult(task);
        }
    }


    private void disconnectFromGoogle() {
        googleSignInClient.signOut();
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> LoginManager.getInstance().logOut()).executeAsync();
    }
}
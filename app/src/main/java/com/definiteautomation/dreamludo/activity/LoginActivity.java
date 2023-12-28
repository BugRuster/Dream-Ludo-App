package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    public ImageView btnFB, btnGoogle;
    private String strName, strUsername, strEmail, strPassword;

    private ProgressBar progressBar;
    private ApiCalling api;

    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    private static final int REQ_CODE =9001;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        changeStatusBarColor();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnFB = findViewById(R.id.btnFb);

        btnFB.setOnClickListener(v -> {
            if(Function.checkNetworkConnection(LoginActivity.this)) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if (isLoggedIn){
                    disconnectFromFacebook();
                }

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        runOnUiThread(() -> setFacebookData(loginResult));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "ERROR" + exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, options);

        btnGoogle.setOnClickListener(v -> {
            if(Function.checkNetworkConnection(LoginActivity.this)) {
                try {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                    if (googleSignInClient != null && account != null) {
                        disconnectFromGoogle();
                    }
                    signIn();
                } catch (Exception e) {
                    Log.d("DISCONNECT ERROR", e.toString());
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.white));
    }

    public void onRegisterClick(View View){
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    public void onForgotClick(View View){
        startActivity(new Intent(this, ForgotActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    public void onMainClick(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        strEmail = editTextEmail.getText().toString().trim();
        strPassword = editTextPassword.getText().toString().trim();

        if (strEmail.equals("") && strPassword.equals("")) {
            Function.showToast(LoginActivity.this, "All fields are mandatory");
        } else if (strEmail.equals("")) {
            Function.showToast(LoginActivity.this, "Please enter email");
        }else if (strPassword.equals("")) {
            Function.showToast(LoginActivity.this, "Please enter password");
        } else {
            if (Function.checkNetworkConnection(LoginActivity.this)) {
                loginUser("regular", strEmail, strPassword);
            }
        }
    }


    private void loginUser(String type, String strEmail, String strPassword) {
        progressBar.showProgressDialog();

        Call<UserModel> call = api.loginUser(AppConstant.PURCHASE_KEY, strEmail, strPassword, type);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel legalData = response.body();
                    List<UserModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            Function.showToast(LoginActivity.this, res.get(0).getMsg());

                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_USER_ID, res.get(0).getId());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_FULL_NAME, res.get(0).getFull_name());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_PROFILE_PHOTO, res.get(0).getProfile_img());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_USERNAME, res.get(0).getUsername());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_EMAIL, res.get(0).getEmail());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_COUNTRY_CODE, res.get(0).getCountry_code());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_MOBILE, res.get(0).getMobile());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_WHATSAPP, res.get(0).getWhatsapp_no());
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_PASSWORD, LoginActivity.this.strPassword);
                            Preferences.getInstance(LoginActivity.this).setString(Preferences.KEY_IS_AUTO_LOGIN, "1");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("finish", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                        } else {
                            progressBar.hideProgressDialog();

                            if (type.equals("regular")) {
                                Function.showToast(LoginActivity.this, res.get(0).getMsg());
                            }
                            else {
                                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                                i.putExtra("FULL_NAME_KEY",strName);
                                i.putExtra("USERNAME_KEY",strUsername);
                                i.putExtra("EMAIL_KEY",strEmail);
                                i.putExtra("PASSWORD_KEY",strPassword);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                Function.fireIntentWithData(LoginActivity.this, i);
                                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                            }
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


    private void setFacebookData(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                (object, response) -> {
                    if (object != null && response != null) {
                        try {
                            Log.i("Response", response.toString());

                            try {
                                if (!object.getString("name").equals("null")){
                                    strName = object.getString("name");
                                }
                            } catch (JSONException | NullPointerException  e) {
                                e.printStackTrace();
                            }

                            try {
                                if (!object.getString("email").equals("null")){
                                    strEmail = object.getString("email");
                                }
                            } catch (JSONException | NullPointerException  e) {
                                e.printStackTrace();
                            }

                            try{
                                String[] resArray = strEmail.split("@");
                                strUsername = resArray[0];
                            }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
                                e.printStackTrace();
                            }

                            try {
                                if (!object.getString("id").equals("null")){
                                    strPassword = (object.getString("id"));
                                }
                            } catch (JSONException | NullPointerException  e) {
                                e.printStackTrace();
                            }

                            try{
                                loginUser("social", strEmail, strPassword);
                            }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
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
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, REQ_CODE);
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.i("Response", account.toString());

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
                    loginUser("social", strEmail, strPassword);
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
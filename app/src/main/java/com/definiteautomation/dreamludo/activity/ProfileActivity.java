package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.PicModeSelectDialogFragment;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements PicModeSelectDialogFragment.IPicModeSelectListener {

    public static final String ERROR = "error";

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 218;
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    private EditText editTextName, editTextEmail, editTextMobile, editTextWhatsApp, editTextPassword;
    private CountryCodePicker countryCodePicker;
    private TextView profileTv;
    private CircularImageView profileIv;
    public Button saveButton;

    private String uriFile;
    public String strName, strEmail, strCountryCode, strMobile, stWhatsApp, strPassword;

    private ProgressBar progressBar;
    private ApiCalling api;

    // Storage Firebase
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(Preferences.getInstance(this).getString(Preferences.KEY_USER_ID));
        mUserDatabase.keepSynced(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextWhatsApp = findViewById(R.id.editTextWhatsApp);
        editTextPassword = findViewById(R.id.editTextPassword);

        profileTv = findViewById(R.id.profileTv);
        profileIv = findViewById(R.id.profileIv);
        saveButton = findViewById(R.id.saveButton);

        editTextName.setText(Preferences.getInstance(this).getString(Preferences.KEY_FULL_NAME));
        editTextEmail.setText(Preferences.getInstance(this).getString(Preferences.KEY_EMAIL));
        countryCodePicker.setCountryForPhoneCode(Integer.parseInt(Preferences.getInstance(this).getString(Preferences.KEY_COUNTRY_CODE)));
        editTextMobile.setText(Preferences.getInstance(this).getString(Preferences.KEY_MOBILE));
        editTextWhatsApp.setText(Preferences.getInstance(this).getString(Preferences.KEY_WHATSAPP));
        editTextPassword.setText(Preferences.getInstance(this).getString(Preferences.KEY_PASSWORD));

        if (Preferences.getInstance(this).getString(Preferences.KEY_PROFILE_PHOTO).equals("")) {
            profileTv.setVisibility(View.VISIBLE);
            profileIv.setVisibility(View.GONE);
            try {
                profileTv.setText(Preferences.getInstance(this).getString(Preferences.KEY_FULL_NAME));
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        } else {
            profileIv.setVisibility(View.VISIBLE);
            profileTv.setVisibility(View.GONE);
            try {
                Glide.with(ProfileActivity.this).load(AppConstant.API_URL+Preferences.getInstance(this).getString(Preferences.KEY_PROFILE_PHOTO))
                        .apply(new RequestOptions().override(120,120))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_user).error(R.drawable.ic_user))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .into(profileIv);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        profileTv.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    showAddProfilePicDialog();
                }
            } else {
                showAddProfilePicDialog();
            }
        });

        profileIv.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    showAddProfilePicDialog();
                }
            } else {
                showAddProfilePicDialog();
            }
        });

        saveButton.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
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
        stWhatsApp = editTextWhatsApp.getText().toString().trim();
        strPassword = editTextPassword.getText().toString().trim();

        if (strName.equals("") && stWhatsApp.equals("") && strPassword.equals("")) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
        else {
            if (Function.checkNetworkConnection(ProfileActivity.this)) {

                if (!Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_FULL_NAME).equals(strName) || !Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_EMAIL).equals(stWhatsApp) || !Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_COUNTRY_CODE).equals(strCountryCode) || !Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_MOBILE).equals(strMobile)) {
                    progressBar.showProgressDialog();

                    Map updateHashMap = new HashMap();
                    updateHashMap.put("name", strName);

                    mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener((OnCompleteListener<Void>) Task::isSuccessful);

                    Call<UserModel> callInfo = api.updateUserProfileInfo(AppConstant.PURCHASE_KEY, Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_USER_ID), strName, stWhatsApp);
                    callInfo.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            progressBar.hideProgressDialog();

                            if (response.isSuccessful()) {
                                UserModel legalData = response.body();
                                List<UserModel.Result> res;
                                if (legalData != null) {
                                    res = legalData.getResult();
                                    if (res.get(0).getSuccess() == 1) {
                                        Preferences.getInstance(ProfileActivity.this).setString(Preferences.KEY_FULL_NAME, strName);
                                        Preferences.getInstance(ProfileActivity.this).setString(Preferences.KEY_WHATSAPP, stWhatsApp);
                                        Function.showToast(ProfileActivity.this, res.get(0).getMsg());
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

                if (!Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_PASSWORD).equals(strPassword)) {
                    progressBar.showProgressDialog();

                    Call<UserModel> callPass = api.updateUserProfilePassword(AppConstant.PURCHASE_KEY, Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_USER_ID), strPassword);
                    callPass.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            progressBar.hideProgressDialog();

                            if (response.isSuccessful()) {
                                UserModel legalData = response.body();
                                List<UserModel.Result> res;
                                if (legalData != null) {
                                    res = legalData.getResult();
                                    if (res.get(0).getSuccess() == 1) {
                                        Preferences.getInstance(ProfileActivity.this).setString(Preferences.KEY_PASSWORD, strPassword);
                                        Function.showToast(ProfileActivity.this, res.get(0).getMsg());
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
        }
    }

    @Override
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(AppConstant.PicModes.CAMERA) ? AppConstant.IntentExtras.ACTION_CAMERA : AppConstant.IntentExtras.ACTION_GALLERY;
        if (action.equals(AppConstant.IntentExtras.ACTION_CAMERA)) {
            takePic();
        } else {
            pickImage();
        }
    }

    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setIPicModeSelectListener(this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAddProfilePicDialog();
            }
        }
    }

    private void pickImage() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_GALLERY);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void takePic() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bmp != null){
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            try {
                onCaptureImageResultInstrument(result);
            } catch (Exception ex) {
                errorValidation();
            }
        } else if (requestCode == REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                userCancelled();
            } else if (resultCode == RESULT_OK && result != null && result.getData() != null) {
                try {
                    onGalleryImageResultInstrument(result);
                } catch (Exception e) {
                    errorValidation();
                }
            } else {
                errorValidation();
            }
        }
    }

    public void userCancelled() {
        Toast.makeText(this, "User canceled.", Toast.LENGTH_SHORT).show();
    }

    public void errorValidation() {
        Intent intent = new Intent();
        intent.putExtra(ERROR, true);
        intent.putExtra(ERROR, "Error while opening the image file. Please try again.");
        finish();
    }

    private void onCaptureImageResultInstrument(Intent data) {

        //Getting the Bitmap from Gallery
        Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

        //Setting the Bitmap to ImageView
        uriFile = getStringImage(bitmap);
        profileIv.setImageBitmap(bitmap);

        profileTv.setVisibility(View.GONE);
        profileIv.setVisibility(View.VISIBLE);

        if (bitmap != null) {
            uploadPic(uriFile);
        }

    }

    private void onGalleryImageResultInstrument(Intent data) {
        final Uri saveUri = data.getData();

        try {
            //Getting the Bitmap from Gallery
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), saveUri);
            //Setting the Bitmap to ImageView
            uriFile = getStringImage(bitmap);
            profileIv.setImageBitmap(bitmap);

            profileTv.setVisibility(View.GONE);
            profileIv.setVisibility(View.VISIBLE);

            if (saveUri != null) {
                uploadPic(uriFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadPic(String uriFile) {
        progressBar.showProgressDialog();

        Map updateHashMap = new HashMap();
        updateHashMap.put("image", "upload/profile/"+Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_USER_ID)+".jpg");
        updateHashMap.put("thumb_image", "upload/profile/"+Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_USER_ID)+".jpg");

        mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener((OnCompleteListener<Void>) Task::isSuccessful);

        Call<UserModel> call = api.updateUserPicture(AppConstant.PURCHASE_KEY, Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_USER_ID), uriFile);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel legalData = response.body();
                    List<UserModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            Preferences.getInstance(ProfileActivity.this).setString(Preferences.KEY_PROFILE_PHOTO, "upload/profile/"+Preferences.getInstance(ProfileActivity.this).getString(Preferences.KEY_USER_ID)+".jpg");
                            Function.showToast(ProfileActivity.this, res.get(0).getMsg());
                            Function.fireIntent(ProfileActivity.this, MainActivity.class);
                        } else {
                            progressBar.hideProgressDialog();
                            Function.showToast(ProfileActivity.this, res.get(0).getMsg());
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
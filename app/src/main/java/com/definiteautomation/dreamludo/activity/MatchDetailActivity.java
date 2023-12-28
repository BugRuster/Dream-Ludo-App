package com.definiteautomation.dreamludo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.adapter.UpcomingAdapter;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.ConfigurationModel;
import com.definiteautomation.dreamludo.model.MatchModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MatchDetailActivity extends AppCompatActivity {

    public TextView timerTv, nameTv, prizeTv, boardTv, whatsAppTv, remarkTv;
    private WebView rulesWv;
    private Button uploadBt, playBt;
    private CheckBox winCb, lossCb, cancelCb;
    private ImageView proofIv;
    private CardView resultCv, uploadCv;

    public String matchIdSt, fParticipantIdSt, sParticipantIdSt, fParticipantNameSt, sParticipantNameSt;
    private int typeSt;
    public double feesSt, prizeSt;
    private int status = 0;

    private ProgressBar progressBar;
    private ApiCalling api;

    public static final String ERROR = "error";
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 218;
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;

    private String uriFile = "";

    private long mMinutes = 0;
    private long mSeconds = 0;
    private long mMilliSeconds = 0;

    public UpcomingAdapter.TimerListener mListener;
    private CountDownTimer mCountDownTimer;
    public String startTime, currentTime;

    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;
    private final int UPDATE_INTERVAL = 10000;
    boolean run = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        timerTv = findViewById(R.id.timerTv);
        nameTv = findViewById(R.id.nameTv);
        prizeTv = findViewById(R.id.prizeTv);
        boardTv = findViewById(R.id.boardTv);
        whatsAppTv = findViewById(R.id.whatsappTv);
        remarkTv = findViewById(R.id.remarkTv);
        uploadBt = findViewById(R.id.uploadBt);
        playBt = findViewById(R.id.playBt);
        winCb = findViewById(R.id.winCb);
        lossCb = findViewById(R.id.lossCb);
        cancelCb = findViewById(R.id.cancelCb);
        proofIv = findViewById(R.id.proofIv);
        uploadCv = findViewById(R.id.uploadCv);
        resultCv = findViewById(R.id.resultCv);

        if (getIntent().getExtras() != null) {
            matchIdSt = getIntent().getExtras().getString("ID_KEY");
            feesSt = getIntent().getExtras().getDouble("FEE_KEY");
            prizeSt = getIntent().getExtras().getDouble("PRIZE_KEY");
            typeSt = getIntent().getExtras().getInt("TYPE_KEY");
            currentTime = getIntent().getExtras().getString("CURR_TIME_KEY");
            startTime = getIntent().getExtras().getString("PLAY_TIME_KEY");
            fParticipantIdSt = getIntent().getExtras().getString("PARTI1_ID_KEY");
            sParticipantIdSt = getIntent().getExtras().getString("PARTI2_ID_KEY");
            fParticipantNameSt = getIntent().getExtras().getString("PARTI1_NAME_KEY","0");
            sParticipantNameSt = getIntent().getExtras().getString("PARTI2_NAME_KEY","0");

            if (!fParticipantNameSt.equals("0") && !sParticipantNameSt.equals("0")) {
                nameTv.setText(String.format("%s Vs %s", fParticipantNameSt, sParticipantNameSt));
                remarkTv.setText("Please, Share room code to opponent join match.");
                resultCv.setVisibility(View.VISIBLE);
                uploadBt.setVisibility(View.VISIBLE);
                playBt.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.GONE);
            }
            else if (!fParticipantNameSt.equals("0") && typeSt == 1) {
                nameTv.setText(String.format("%s Vs Team 2", fParticipantNameSt));
                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                resultCv.setVisibility(View.GONE);
                uploadBt.setVisibility(View.GONE);
                playBt.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.VISIBLE);
            }
            else if (!fParticipantNameSt.equals("0") && typeSt == 0) {
                nameTv.setText(String.format("%s Vs Player 2", fParticipantNameSt));
                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                resultCv.setVisibility(View.GONE);
                uploadBt.setVisibility(View.GONE);
                playBt.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.VISIBLE);
            }
            else if (typeSt == 1) {
                nameTv.setText("Team 1 Vs Team 2");
                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                resultCv.setVisibility(View.GONE);
                uploadBt.setVisibility(View.GONE);
                playBt.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.VISIBLE);
            }
            else if (typeSt == 0) {
                nameTv.setText("Player 1 Vs Player 2");
                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                resultCv.setVisibility(View.GONE);
                uploadBt.setVisibility(View.GONE);
                playBt.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.VISIBLE);
            }

            boardTv.setText("#"+matchIdSt);
            prizeTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, prizeSt));

            try {
                if (Integer.parseInt(currentTime) >= Integer.parseInt(startTime)) {
                    timerTv.setVisibility(View.GONE);
                }
                else {
                    if (!fParticipantNameSt.equals("0") && !sParticipantNameSt.equals("0")) {
                        timerTv.setVisibility(View.GONE);
                    }
                    else {
                        int time = Integer.parseInt(startTime) - Integer.parseInt(currentTime);
                        setTime(time * 1000L);
                        startCountDown();
                        timerTv.setVisibility(View.VISIBLE);

                        searchParticipant();
                    }
                }
            }
            catch (NumberFormatException e) {
                timerTv.setVisibility(View.GONE);
            }
        }

        getRules();

        whatsAppTv.setOnClickListener(v -> {
            if (!fParticipantNameSt.equals("0") && !sParticipantNameSt.equals("0")) {
                if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(fParticipantIdSt)) {
                    Intent chatIntent = new Intent(this, ChatActivity.class);
                    chatIntent.putExtra("user_id", sParticipantIdSt);
                    chatIntent.putExtra("user_name", sParticipantNameSt);
                    chatIntent.putExtra("match_id", matchIdSt);
                    startActivity(chatIntent);
                }
                else {
                    Intent chatIntent = new Intent(this, ChatActivity.class);
                    chatIntent.putExtra("user_id", fParticipantIdSt);
                    chatIntent.putExtra("user_name", fParticipantNameSt);
                    chatIntent.putExtra("match_id", matchIdSt);
                    startActivity(chatIntent);
                }
            }
            else {
                Toast.makeText(this, "Please, Wait some time till opponent join match.", Toast.LENGTH_SHORT).show();
            }
        });

        winCb.setOnClickListener(v -> {
            status = 1;
            winCb.setChecked(true);
            lossCb.setChecked(false);
            cancelCb.setChecked(false);
            uploadCv.setVisibility(View.VISIBLE);
        });

        lossCb.setOnClickListener(v -> {
            status = 2;
            winCb.setChecked(false);
            lossCb.setChecked(true);
            cancelCb.setChecked(false);
            uploadCv.setVisibility(View.GONE);
        });

        cancelCb.setOnClickListener(v -> {
            status = 3;
            winCb.setChecked(false);
            lossCb.setChecked(false);
            cancelCb.setChecked(true);
            uploadCv.setVisibility(View.VISIBLE);
        });

        proofIv.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    pickImage();
                }
            } else {
                pickImage();
            }
        });

        uploadBt.setOnClickListener(v -> uploadResult());

        playBt.setOnClickListener(v -> {
            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(AppConstant.PACKAGE_NAME);
            if (launchIntentForPackage != null) {
                startActivity(launchIntentForPackage);
            } else {
                Toast.makeText(MatchDetailActivity.this, ""+AppConstant.GAME_NAME+" is Not Installed", Toast.LENGTH_SHORT).show();
            }
        });

        if (fParticipantNameSt.equals("0") || sParticipantNameSt.equals("0")) {
            mRepeatHandler = new Handler();
            mRepeatRunnable = () -> {
                searchParticipant();
                mRepeatHandler.postDelayed(mRepeatRunnable, UPDATE_INTERVAL);
            };
            mRepeatHandler.postDelayed(mRepeatRunnable, UPDATE_INTERVAL);
        }
    }

    private void searchParticipant() {
        if(run) {
            Call<List<MatchModel>> call = api.searchParticipant(AppConstant.PURCHASE_KEY, matchIdSt);
            call.enqueue(new Callback<List<MatchModel>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<List<MatchModel>> call, @NonNull Response<List<MatchModel>> response) {
                    if (response.isSuccessful()) {
                        List<MatchModel> legalData = response.body();
                        if (legalData != null) {
                            fParticipantIdSt = legalData.get(0).getParti1_id();
                            sParticipantIdSt = legalData.get(0).getParti2_id();
                            fParticipantNameSt = legalData.get(0).getParti1_name();
                            sParticipantNameSt = legalData.get(0).getParti2_name();

                            if (!fParticipantNameSt.equals("0") && !sParticipantNameSt.equals("0")) {
                                nameTv.setText(String.format("%s Vs %s", fParticipantNameSt, sParticipantNameSt));
                                remarkTv.setText("Please, Share room code to opponent join match.");
                                resultCv.setVisibility(View.VISIBLE);
                                uploadBt.setVisibility(View.VISIBLE);
                                playBt.setVisibility(View.VISIBLE);
                                timerTv.setVisibility(View.GONE);
                            } else if (!fParticipantNameSt.equals("0") && typeSt == 1) {
                                nameTv.setText(String.format("%s Vs Team 2", fParticipantNameSt));
                                resultCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                playBt.setVisibility(View.VISIBLE);
                                timerTv.setVisibility(View.VISIBLE);
                                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                            } else if (!fParticipantNameSt.equals("0") && typeSt == 0) {
                                nameTv.setText(String.format("%s Vs Player 2", fParticipantNameSt));
                                resultCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                playBt.setVisibility(View.VISIBLE);
                                timerTv.setVisibility(View.VISIBLE);
                                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                            } else if (typeSt == 1) {
                                nameTv.setText("Team 1 Vs Team 2");
                                resultCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                playBt.setVisibility(View.VISIBLE);
                                timerTv.setVisibility(View.VISIBLE);
                                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                            } else if (typeSt == 0) {
                                nameTv.setText("Player 1 Vs Player 2");
                                resultCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                playBt.setVisibility(View.VISIBLE);
                                timerTv.setVisibility(View.VISIBLE);
                                remarkTv.setText("Please, Don't press back until waiting time over or opponent join match.");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<MatchModel>> call, @NonNull Throwable t) {

                }
            });
        }
    }

    private void deleteParticipant() {
        progressBar.showProgressDialog();

        Call<MatchModel> call = api.deleteParticipant(AppConstant.PURCHASE_KEY, matchIdSt, Preferences.getInstance(this).getString(Preferences.KEY_USER_ID));
        call.enqueue(new Callback<MatchModel>() {
            @Override
            public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                if (response.isSuccessful()) {
                    MatchModel legalData = response.body();
                    List<MatchModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        timerTv.setVisibility(View.GONE);
                        progressBar.hideProgressDialog();
                        Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                        Function.fireIntent(MatchDetailActivity.this, MainActivity.class);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }

    private void uploadResult() {
        if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(fParticipantIdSt) && status == 2) {
            progressBar.showProgressDialog();

            Call<MatchModel> call = api.updateResultParti1WithoutProof(AppConstant.PURCHASE_KEY, matchIdSt, String.valueOf(status));
            call.enqueue(new Callback<MatchModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                    if (response.isSuccessful()) {
                        MatchModel legalData = response.body();
                        List<MatchModel.Result> res;
                        if (legalData != null) {
                            res = legalData.getResult();
                            if (res.get(0).getSuccess() == 1) {
                                run=false;
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                                remarkTv.setText("Your Result uploaded successfully");
                                resultCv.setVisibility(View.GONE);
                                uploadCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                uploadBt.setEnabled(false);
                                playBt.setVisibility(View.GONE);
                                progressBar.hideProgressDialog();
                            } else {
                                progressBar.hideProgressDialog();
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                    progressBar.hideProgressDialog();
                    Log.d("tag",t.getMessage());
                }
            });
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(fParticipantIdSt) && uriFile.equals("")) {
            Function.showToast(MatchDetailActivity.this, "Please upload proof before submit result");
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(fParticipantIdSt) && !uriFile.equals("")) {
            progressBar.showProgressDialog();

            Call<MatchModel> call = api.updateResultParti1WithProof(AppConstant.PURCHASE_KEY, matchIdSt, Preferences.getInstance(this).getString(Preferences.KEY_USER_ID), String.valueOf(status), uriFile);
            call.enqueue(new Callback<MatchModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                    if (response.isSuccessful()) {
                        MatchModel legalData = response.body();
                        List<MatchModel.Result> res;
                        if (legalData != null) {
                            res = legalData.getResult();
                            if (res.get(0).getSuccess() == 1) {
                                run=false;
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                                remarkTv.setText("Your Result uploaded successfully");
                                resultCv.setVisibility(View.GONE);
                                uploadCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                uploadBt.setEnabled(false);
                                playBt.setVisibility(View.GONE);
                                progressBar.hideProgressDialog();
                            } else {
                                progressBar.hideProgressDialog();
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                    progressBar.hideProgressDialog();
                }
            });
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(sParticipantIdSt) && status == 2) {
            progressBar.showProgressDialog();

            Call<MatchModel> call = api.updateResultParti2WithoutProof(AppConstant.PURCHASE_KEY, matchIdSt, String.valueOf(status));
            call.enqueue(new Callback<MatchModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                    if (response.isSuccessful()) {
                        MatchModel legalData = response.body();
                        List<MatchModel.Result> res;
                        if (legalData != null) {
                            res = legalData.getResult();
                            if (res.get(0).getSuccess() == 1) {
                                run=false;
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                                remarkTv.setText("Your Result uploaded successfully");
                                resultCv.setVisibility(View.GONE);
                                uploadCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                uploadBt.setEnabled(false);
                                playBt.setVisibility(View.GONE);
                                progressBar.hideProgressDialog();
                            } else {
                                progressBar.hideProgressDialog();
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                    progressBar.hideProgressDialog();
                }
            });
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(sParticipantIdSt) && uriFile.equals("")) {
            Function.showToast(MatchDetailActivity.this, "Please upload proof before submit result");
        }
        else if (Preferences.getInstance(this).getString(Preferences.KEY_USER_ID).equals(sParticipantIdSt) && !uriFile.equals("")) {
            progressBar.showProgressDialog();

            Call<MatchModel> call = api.updateResultParti2WithProof(AppConstant.PURCHASE_KEY, matchIdSt, Preferences.getInstance(this).getString(Preferences.KEY_USER_ID), String.valueOf(status), uriFile);
            call.enqueue(new Callback<MatchModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<MatchModel> call, @NonNull Response<MatchModel> response) {
                    if (response.isSuccessful()) {
                        MatchModel legalData = response.body();
                        List<MatchModel.Result> res;
                        if (legalData != null) {
                            res = legalData.getResult();
                            if (res.get(0).getSuccess() == 1) {
                                run=false;
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                                remarkTv.setText("Your Result uploaded successfully");
                                resultCv.setVisibility(View.GONE);
                                uploadCv.setVisibility(View.GONE);
                                uploadBt.setVisibility(View.GONE);
                                uploadBt.setEnabled(false);
                                playBt.setVisibility(View.GONE);
                                progressBar.hideProgressDialog();
                            } else {
                                progressBar.hideProgressDialog();
                                Function.showToast(MatchDetailActivity.this, res.get(0).getMsg());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchModel> call, @NonNull Throwable t) {
                    progressBar.hideProgressDialog();
                }
            });
        }
    }

    private void getRules() {
        rulesWv = findViewById(R.id.rulesWv);
        rulesWv.setBackgroundColor(0);

        Call<ConfigurationModel> call = api.getRules(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            rulesWv.loadDataWithBaseURL(null, res.get(0).getRules(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
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
        if (requestCode == REQUEST_CODE_PICK_GALLERY) {
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
        Toast.makeText(this, "User Cancelled", Toast.LENGTH_SHORT).show();
    }

    public void errorValidation() {
        Intent intent = new Intent();
        intent.putExtra(ERROR, true);
        intent.putExtra(ERROR, "Error while opening the image file. Please try again.");
        finish();
    }

    private void onGalleryImageResultInstrument(Intent data) {
        final Uri saveUri = data.getData();

        try {
            //Getting the Bitmap from Gallery
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), saveUri);

            //Setting the Bitmap to ImageView
            uriFile = getStringImage(bitmap);
            proofIv.setImageBitmap(bitmap);

            proofIv.setVisibility(View.GONE);
            proofIv.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void initCounter() {
        mCountDownTimer = new CountDownTimer(mMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                calculateTime(millisUntilFinished);
                if (mListener != null) {
                    mListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                calculateTime(0);
                if (mListener != null) {
                    mListener.onFinish();
                }
            }
        };
    }

    public void startCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    public void setTime(long milliSeconds) {
        mMilliSeconds = milliSeconds;
        initCounter();
        calculateTime(milliSeconds);
    }

    private void calculateTime(long milliSeconds) {
        if (milliSeconds != 0) {
            mSeconds = (milliSeconds / 1000) % 60;
            mMinutes = (milliSeconds / (1000 * 60)) % 60;

            if (!fParticipantNameSt.equals("0") && sParticipantNameSt.equals("0")) {
                timerTv.setVisibility(View.VISIBLE);
                displayText(timerTv);
            }
            else {
                timerTv.setVisibility(View.GONE);
            }
        }
        else {
            if (!fParticipantNameSt.equals("0") && sParticipantNameSt.equals("0")) {
                timerTv.setVisibility(View.GONE);
                deleteParticipant();
            }
        }
    }

    private void displayText(TextView timeText) {
        try{
            String stringBuilder = "Board close in\n" + getTwoDigitNumber(mMinutes) + "m : " + getTwoDigitNumber(mSeconds) + "s";
            timeText.setText(stringBuilder);
        }catch (NullPointerException e){
            timeText.setVisibility(View.GONE);
        }
    }

    private String getTwoDigitNumber(long number) {
        if (number >= 0 && number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

}
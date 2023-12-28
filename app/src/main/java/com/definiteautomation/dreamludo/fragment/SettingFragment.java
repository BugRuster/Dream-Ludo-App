package com.definiteautomation.dreamludo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.activity.LeaderBoardActivity;
import com.definiteautomation.dreamludo.activity.LoginActivity;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.model.UserModel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.activity.DepositActivity;
import com.definiteautomation.dreamludo.activity.HistoryActivity;
import com.definiteautomation.dreamludo.activity.NotificationActivity;
import com.definiteautomation.dreamludo.activity.ProfileActivity;
import com.definiteautomation.dreamludo.activity.ReferralActivity;
import com.definiteautomation.dreamludo.activity.StatisticsActivity;
import com.definiteautomation.dreamludo.activity.WebviewActivity;
import com.definiteautomation.dreamludo.activity.WithdrawActivity;

import java.util.List;


public class SettingFragment extends Fragment {

    private ApiCalling api;

    public CircularImageView photoIv;
    public LinearLayout withdrawLyt;
    public TextView balanceTv, depositTv, withdrawTv, bonusTv, photoTv, nameTv, phoneTv;
    private double deposit = 0, winning = 0, bonus = 0, total = 0;

    private DatabaseReference mUserRef;
    public GoogleSignInClient googleSignInClient;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));

        photoIv = view.findViewById(R.id.photoIv);
        photoTv = view.findViewById(R.id.photoTv);
        nameTv = view.findViewById(R.id.nameTv);
        phoneTv = view.findViewById(R.id.phoneTv);

        balanceTv = view.findViewById(R.id.balanceTv);
        depositTv = view.findViewById(R.id.depositTv);
        withdrawTv = view.findViewById(R.id.withdrawTv);
        bonusTv = view.findViewById(R.id.bonusTv);
        withdrawLyt = view.findViewById(R.id.withdrawLyt);

        if (AppConstant.WALLET_MODE == 0) {
            withdrawLyt.setVisibility(View.VISIBLE);
        }
        else {
            withdrawLyt.setVisibility(View.GONE);
        }

        view.findViewById(R.id.depositBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), DepositActivity.class)));

        view.findViewById(R.id.withdrawBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), WithdrawActivity.class)));

        view.findViewById(R.id.profileBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), ProfileActivity.class)));

        view.findViewById(R.id.historyBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), HistoryActivity.class)));

        view.findViewById(R.id.statisticsBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), StatisticsActivity.class)));

        view.findViewById(R.id.leaderboardBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), LeaderBoardActivity.class)));

        view.findViewById(R.id.referBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), ReferralActivity.class)));

        view.findViewById(R.id.notificationBt).setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationActivity.class)));

        view.findViewById(R.id.helpBt).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + AppConstant.SUPPORT_EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        view.findViewById(R.id.faqBt).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WebviewActivity.class);
            i.putExtra("PAGE_KEY","FAQ");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Function.fireIntentWithData(requireActivity(), i);
        });

        view.findViewById(R.id.policyBt).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WebviewActivity.class);
            i.putExtra("PAGE_KEY","Privacy Policy");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Function.fireIntentWithData(requireActivity(), i);
        });

        view.findViewById(R.id.legalBt).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WebviewActivity.class);
            i.putExtra("PAGE_KEY","legal");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Function.fireIntentWithData(requireActivity(), i);
        });

        view.findViewById(R.id.aboutBt).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WebviewActivity.class);
            i.putExtra("PAGE_KEY","About Us");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Function.fireIntentWithData(requireActivity(), i);
        });

        view.findViewById(R.id.termsBt).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), WebviewActivity.class);
            i.putExtra("PAGE_KEY","Terms & Conditions");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Function.fireIntentWithData(requireActivity(), i);
        });

        view.findViewById(R.id.logoutBt).setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                    // set dialog icon
                    .setIcon(R.drawable.ic_logout)
                    // Set Dialog Title
                    .setTitle("LOGOUT ACCOUNT")
                    // Set Dialog Message
                    .setMessage("Are you sure you want to logout?")
                    // positive button
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireActivity());
                        if (googleSignInClient != null && account != null) {
                            disconnectFromGoogle();
                        }
                        else if (AccessToken.getCurrentAccessToken() != null) {
                            disconnectFromFacebook();
                        }
                        else {
                            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
                            Preferences.getInstance(getActivity()).setlogout();
                        }
                    })
                    // negative button
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    }).create();

            alertDialog.show();
        });

        nameTv.setText(Preferences.getInstance(getActivity()).getString(Preferences.KEY_FULL_NAME));
        phoneTv.setText(String.format("+%s %s", Preferences.getInstance(getActivity()).getString(Preferences.KEY_COUNTRY_CODE), Preferences.getInstance(getActivity()).getString(Preferences.KEY_MOBILE)));

        if (Preferences.getInstance(getActivity()).getString(Preferences.KEY_PROFILE_PHOTO).equals("")) {
            photoTv.setVisibility(View.VISIBLE);
            photoIv.setVisibility(View.GONE);
            try {
                photoTv.setText(Preferences.getInstance(getActivity()).getString(Preferences.KEY_FULL_NAME));
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        } else {
            photoIv.setVisibility(View.VISIBLE);
            photoTv.setVisibility(View.GONE);
            try {
                Glide.with(requireActivity()).load(AppConstant.API_URL+Preferences.getInstance(requireActivity()).getString(Preferences.KEY_PROFILE_PHOTO))
                        .apply(new RequestOptions().override(120,120))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_user).error(R.drawable.ic_user))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .into(photoIv);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        if(Function.checkNetworkConnection(requireActivity())) {
            getUserDetails();
        }

        return view;
    }

    private void getUserDetails() {
        Call<UserModel> call = api.getUserDetails(AppConstant.PURCHASE_KEY, Preferences.getInstance(getActivity()).getString(Preferences.KEY_USER_ID));
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

                            depositTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, deposit));
                            withdrawTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, winning));
                            bonusTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, bonus));
                            balanceTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, total));

                            if (res.get(0).getIs_block() == 1) {
                                Preferences.getInstance(getActivity()).setString(Preferences.KEY_IS_AUTO_LOGIN,"0");

                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                i.putExtra("finish", true);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                            else if (res.get(0).getIs_active() == 0) {
                                Preferences.getInstance(getActivity()).setString(Preferences.KEY_IS_AUTO_LOGIN,"0");

                                Intent i = new Intent(getActivity(), LoginActivity.class);
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

    @Override
    public void onResume() {
        super.onResume();
        getUserDetails();
    }

    private void disconnectFromGoogle() {
        googleSignInClient.signOut();

        mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
        Preferences.getInstance(getActivity()).setlogout();
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> LoginManager.getInstance().logOut()).executeAsync();

        mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
        Preferences.getInstance(getActivity()).setlogout();
    }
}
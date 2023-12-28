package com.definiteautomation.dreamludo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.definiteautomation.dreamludo.MyApplication;
import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.adapter.LeaderboardAdapter;
import com.definiteautomation.dreamludo.api.ApiCalling;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Function;
import com.definiteautomation.dreamludo.helper.ProgressBar;
import com.definiteautomation.dreamludo.model.LeaderboardModel;

import java.util.List;
import java.util.Objects;


public class LeaderBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noDataTv;

    private ProgressBar progressBar;
    private ApiCalling api;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBar = new ProgressBar(this, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if(Function.checkNetworkConnection(LeaderBoardActivity.this)) {
            getLeaderboard();
        }
    }

    private void getLeaderboard() {
        recyclerView = findViewById(R.id.recyclerView);
        noDataTv = findViewById(R.id.noDataTv);
        progressBar.showProgressDialog();

        Call<List<LeaderboardModel>> call = api.getLeaderboard(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<List<LeaderboardModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<LeaderboardModel>> call, @NonNull Response<List<LeaderboardModel>> response) {
                progressBar.hideProgressDialog();

                if (response.isSuccessful()) {
                    List<LeaderboardModel> legalData = response.body();
                    if (legalData != null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LeaderBoardActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        leaderboardAdapter = new LeaderboardAdapter(LeaderBoardActivity.this, legalData);

                        if (leaderboardAdapter.getItemCount() != 0) {
                            leaderboardAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(leaderboardAdapter);

                            recyclerView.setVisibility(View.VISIBLE);
                            noDataTv.setVisibility(View.GONE);
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            noDataTv.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LeaderboardModel>> call, @NonNull Throwable t) {
                progressBar.hideProgressDialog();
            }
        });
    }
}
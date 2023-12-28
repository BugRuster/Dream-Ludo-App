package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.model.LeaderboardModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    public Context context;
    private final List<LeaderboardModel> dataArrayList;

    public LeaderboardAdapter(Context context, List<LeaderboardModel> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position + 1;

        holder.posTv.setText(String.format("%d", i));
        holder.nameTv.setText(dataArrayList.get(position).getFull_name());
        holder.prizeTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getWon_prize()));
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView posTv;
        TextView nameTv;
        TextView prizeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posTv = itemView.findViewById(R.id.posTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            prizeTv = itemView.findViewById(R.id.prizeTv);
        }
    }

}


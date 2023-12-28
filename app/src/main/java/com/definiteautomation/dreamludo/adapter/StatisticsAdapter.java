package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.model.StatisticsModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    public Context context;
    private final List<StatisticsModel> dataArrayList;

    public StatisticsAdapter(Context applicationContext, List<StatisticsModel> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_statistics, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position + 1;

        holder.posTv.setText(String.format("%d", i));
        holder.titleTv.setText(String.format("#%s Board Number", dataArrayList.get(position).getId()));
        holder.dateTv.setText(dataArrayList.get(position).getPlay_time());
        holder.feesTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getMatch_fee()));

        if (dataArrayList.get(position).getWin().equals("1")) {
            holder.prizeTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getWon_prize()));
        }
        else {
            holder.prizeTv.setText(String.format("%s0", AppConstant.CURRENCY_SIGN));
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView posTv;
        TextView titleTv;
        TextView dateTv;
        TextView feesTv;
        TextView prizeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posTv = itemView.findViewById(R.id.posTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            feesTv = itemView.findViewById(R.id.feesTv);
            prizeTv = itemView.findViewById(R.id.prizeTv);
        }
    }

}

package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.helper.Preferences;
import com.definiteautomation.dreamludo.model.MatchModel;
import com.definiteautomation.dreamludo.view.VerticalTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.ViewHolder> {

    private final Context context;
    private final List<MatchModel> dataArrayList;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public OngoingAdapter(Context applicationContext, List<MatchModel> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_ongoing, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!dataArrayList.get(position).getParti1_name().equals("0") && !dataArrayList.get(position).getParti2_name().equals("0")) {
            holder.titleTv.setText(String.format("%s Vs %s", dataArrayList.get(position).getParti1_name(), dataArrayList.get(position).getParti2_name()));
        }
        else if (!dataArrayList.get(position).getParti1_name().equals("0") && dataArrayList.get(position).getParti2_name().equals("0") && dataArrayList.get(position).getType() == 1) {
            holder.titleTv.setText(String.format("%s Vs Team 2", dataArrayList.get(position).getParti1_name()));
        }
        else if (!dataArrayList.get(position).getParti1_name().equals("0") && dataArrayList.get(position).getParti2_name().equals("0") && dataArrayList.get(position).getType() == 0) {
            holder.titleTv.setText(String.format("%s Vs Player 2", dataArrayList.get(position).getParti1_name()));
        }
        else if (dataArrayList.get(position).getType() == 1) {
            holder.titleTv.setText("Team 1 Vs Team 2");
        }
        else if (dataArrayList.get(position).getType() == 0) {
            holder.titleTv.setText("Player 1 Vs Player 2");
        }

        holder.feesTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getMatch_fee()));
        holder.prizeTv.setText(String.format("%s%s", AppConstant.CURRENCY_SIGN, dataArrayList.get(position).getPrize()));
        holder.timeTv.setText(String.format("Start On\n%s", dataArrayList.get(position).getStart_time()));

        holder.progressBar.setMax(dataArrayList.get(position).getTable_size());
        holder.progressBar.setProgress(dataArrayList.get(position).getTable_joined());

        if (dataArrayList.get(position).getType() != 0) {
            holder.typeTv.setText("Team");

            if (dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
                holder.roomStatusTv.setText("Hurry Up! Opponent is waiting.");
                holder.roomSizeTv.setText(String.format("Player: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
            } else {
                int leftSize = dataArrayList.get(position).getTable_size() - dataArrayList.get(position).getTable_joined();
                holder.roomStatusTv.setText("Only "+leftSize+" Team left");
                holder.roomSizeTv.setText(String.format("Team: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
            }
        }
        else {
            holder.typeTv.setText("Single");
            holder.roomSizeTv.setText(String.format("Player: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
            if (dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
                holder.roomStatusTv.setText("Hurry Up! Opponent is waiting.");
            } else {
                int leftSize = dataArrayList.get(position).getTable_size() - dataArrayList.get(position).getTable_joined();
                holder.roomStatusTv.setText("Only "+leftSize+" Player left");
            }
        }

        try {
            if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti1_id()) && !dataArrayList.get(position).getParti1_status().equals("0")) {
                holder.statusBt.setText("SUBMITTED");
                holder.statusBt.setClickable(false);
                holder.statusBt.setEnabled(false);
                holder.roomStatusTv.setText("Under Review! Result submitted.");
                holder.roomStatusTv.setTextColor(context.getResources().getColor(R.color.colorWarning));
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti1_id()) && dataArrayList.get(position).getParti1_status().equals("0") && !dataArrayList.get(position).getParti2_status().equals("0") && dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.statusBt.setText("NEXT");
                holder.statusBt.setClickable(true);
                holder.statusBt.setEnabled(true);
                holder.roomStatusTv.setText("Hurry Up! Opponent submitted result.");
                holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti1_id()) && dataArrayList.get(position).getParti1_status().equals("0") && dataArrayList.get(position).getTable_joined() < dataArrayList.get(position).getTable_size()) {
                holder.statusBt.setText("NEXT");
                holder.statusBt.setClickable(true);
                holder.statusBt.setEnabled(true);
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti1_id()) && dataArrayList.get(position).getParti1_status().equals("0") && dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.statusBt.setText("NEXT");
                holder.statusBt.setClickable(true);
                holder.statusBt.setEnabled(true);
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti2_id()) && !dataArrayList.get(position).getParti2_status().equals("0")) {
                holder.statusBt.setText("SUBMITTED");
                holder.statusBt.setClickable(false);
                holder.statusBt.setEnabled(false);
                holder.roomStatusTv.setText("Under Review! Result submitted.");
                holder.roomStatusTv.setTextColor(context.getResources().getColor(R.color.colorWarning));
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti2_id()) && dataArrayList.get(position).getParti2_status().equals("0") && !dataArrayList.get(position).getParti1_status().equals("0") && dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.statusBt.setText("NEXT");
                holder.statusBt.setClickable(true);
                holder.statusBt.setEnabled(true);
                holder.roomStatusTv.setText("Hurry Up! Opponent submitted result.");
                holder.roomStatusTv.setTextColor(Color.parseColor("#ff0000"));
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti2_id()) && dataArrayList.get(position).getParti2_status().equals("0") && dataArrayList.get(position).getTable_joined() < dataArrayList.get(position).getTable_size()) {
                holder.statusBt.setText("NEXT");
                holder.statusBt.setClickable(true);
                holder.statusBt.setEnabled(true);
            } else if (dataArrayList.get(position).getIs_joined() == 1 && Preferences.getInstance(context).getString(Preferences.KEY_USER_ID).equals(dataArrayList.get(position).getParti2_id()) && dataArrayList.get(position).getParti2_status().equals("0") && dataArrayList.get(position).getTable_joined() >= dataArrayList.get(position).getTable_size()) {
                holder.statusBt.setText("NEXT");
                holder.statusBt.setClickable(true);
                holder.statusBt.setEnabled(true);
            } else {
                holder.statusLi.setVisibility(View.GONE);
                holder.statusBt.setText("NOT JOINED");
                holder.statusBt.setClickable(false);
                holder.statusBt.setEnabled(false);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        holder.statusBt.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, dataArrayList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, MatchModel obj, int pos);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView prizeTv;
        TextView timeTv;
        TextView feesTv;
        TextView winnerTv;
        TextView roomSizeTv;
        TextView roomStatusTv;
        Button statusBt;
        LinearLayout statusLi;
        VerticalTextView typeTv;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            prizeTv = itemView.findViewById(R.id.prizeTv);
            timeTv = itemView.findViewById(R.id.timerTv);
            feesTv = itemView.findViewById(R.id.feesTv);
            winnerTv = itemView.findViewById(R.id.winnerTv);
            roomSizeTv = itemView.findViewById(R.id.roomSizeTv);
            roomStatusTv = itemView.findViewById(R.id.roomStatusTv);
            statusBt = itemView.findViewById(R.id.statusBt);
            statusLi = itemView.findViewById(R.id.statusLi);
            typeTv = itemView.findViewById(R.id.typeTv);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}



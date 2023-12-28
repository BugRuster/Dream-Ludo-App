package com.definiteautomation.dreamludo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.definiteautomation.dreamludo.R;
import com.definiteautomation.dreamludo.helper.AppConstant;
import com.definiteautomation.dreamludo.model.MatchModel;
import com.definiteautomation.dreamludo.view.VerticalTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.ViewHolder> {

    private final Context context;
    private final List<MatchModel> dataArrayList;

    public CompletedAdapter(Context applicationContext, List<MatchModel> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_completed, parent, false);
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
        holder.timeTv.setText(String.format("Played On\n%s", dataArrayList.get(position).getPlay_time()));

        holder.progressBar.setMax(dataArrayList.get(position).getTable_size());
        holder.progressBar.setProgress(dataArrayList.get(position).getTable_joined());

        if (dataArrayList.get(position).getType() != 0) {
            holder.typeTv.setText("Team");
            holder.roomSizeTv.setText(String.format("Team: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
        }
        else {
            holder.typeTv.setText("Single");
            holder.roomSizeTv.setText(String.format("Player: %d/%d", dataArrayList.get(position).getTable_joined(), dataArrayList.get(position).getTable_size()));
        }

        if (dataArrayList.get(position).getIs_joined() == 1 && dataArrayList.get(position).getWin() == 1 && dataArrayList.get(position).getResult_status().equals("1")) {
            holder.statusLi.setVisibility(View.VISIBLE);
            holder.statusBt.setText("Won");
            holder.statusBt.setBackgroundResource(R.drawable.button_background_green);
            holder.winnerTv.setText(String.format("Winner: %s", dataArrayList.get(position).getWinner_name()));
            holder.winnerTv.setTextColor(context.getResources().getColor(R.color.colorSuccess));
        }
        else if (dataArrayList.get(position).getIs_joined() == 1 && dataArrayList.get(position).getWin() == 0 && dataArrayList.get(position).getResult_status().equals("1")) {
            holder.statusLi.setVisibility(View.VISIBLE);
            holder.statusBt.setText("Lost");
            holder.statusBt.setBackgroundResource(R.drawable.button_background_red);
            holder.winnerTv.setText(String.format("Winner: %s", dataArrayList.get(position).getWinner_name()));
            holder.winnerTv.setTextColor(context.getResources().getColor(R.color.colorSuccess));
        }
        else if (dataArrayList.get(position).getIs_joined() == 1 && dataArrayList.get(position).getResult_status().equals("2")) {
            holder.statusLi.setVisibility(View.VISIBLE);
            holder.statusBt.setText("Reject");
            holder.statusBt.setBackgroundResource(R.drawable.button_background_red);
            holder.winnerTv.setText("Your submission was rejected.");
            holder.winnerTv.setTextColor(context.getResources().getColor(R.color.colorWarning));
        }
        else if (dataArrayList.get(position).getResult_status().equals("1")) {
            holder.statusLi.setVisibility(View.GONE);
            holder.winnerTv.setText(String.format("Winner: %s", dataArrayList.get(position).getWinner_name()));
            holder.winnerTv.setTextColor(context.getResources().getColor(R.color.colorSuccess));
        }
        else {
            holder.statusLi.setVisibility(View.GONE);
            holder.winnerTv.setText("Under Review! Result will update soon");
            holder.winnerTv.setTextColor(context.getResources().getColor(R.color.colorWarning));
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView prizeTv;
        TextView timeTv;
        TextView feesTv;
        TextView winnerTv;
        TextView roomSizeTv;
        Button statusBt;
        VerticalTextView typeTv;
        ProgressBar progressBar;
        LinearLayout statusLi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            prizeTv = itemView.findViewById(R.id.prizeTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            feesTv = itemView.findViewById(R.id.feesTv);
            winnerTv = itemView.findViewById(R.id.winnerTv);
            roomSizeTv = itemView.findViewById(R.id.roomSizeTv);
            statusBt = itemView.findViewById(R.id.statusBt);
            typeTv = itemView.findViewById(R.id.typeTv);
            progressBar = itemView.findViewById(R.id.progressBar);
            statusLi = itemView.findViewById(R.id.statusLi);
        }
    }

}


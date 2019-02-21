package com.daily.android;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DailyAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView textView = new TextView(viewGroup.getContext());
        textView.setTextSize(18);
        return new DailyViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((TextView) viewHolder.itemView).setText("position:" + i);
    }

    @Override
    public int getItemCount() {
        return 50;
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {
        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

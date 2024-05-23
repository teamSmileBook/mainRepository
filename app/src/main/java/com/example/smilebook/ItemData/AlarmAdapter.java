package com.example.smilebook.ItemData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.R;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>{
    private List<AlarmData> alarmList;

    // 생성자
    public AlarmAdapter(List<AlarmData> alarmList) {
        this.alarmList = alarmList;
    }
    // ViewHolder 클래스
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmTextView;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            alarmTextView = itemView.findViewById(R.id.alarm_text);
        }
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmData currentItem = alarmList.get(position);
        holder.alarmTextView.setText(currentItem.getAlarm_text());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}

package com.example.smilebook.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>{
    private List<AlarmData> alarmList;
    private Context context;

    // 생성자
    public AlarmAdapter(Context context, List<AlarmData> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }
    // ViewHolder 클래스
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmTextView;
        public ImageButton deleteButton;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            alarmTextView = itemView.findViewById(R.id.alarm_text);
            deleteButton = itemView.findViewById(R.id.alarm_delete);
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

        //알림 삭제
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    alarmList.remove(adapterPosition); // 항목 제거
                    notifyItemRemoved(adapterPosition); // 어댑터에 변경 사항 알림
                    notifyItemRangeChanged(adapterPosition, alarmList.size()); // 변경된 범위 알림

                    // SharedPreferences에서 항목 제거
                    SharedPreferences prefs = context.getSharedPreferences("FCM_PREFS", Context.MODE_PRIVATE);
                    String existingData = prefs.getString("notifications", "");
                    try {
                        JSONArray jsonArray = new JSONArray(existingData);
                        JSONArray newArray = new JSONArray();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i != adapterPosition) {
                                newArray.put(jsonArray.get(i));
                            }
                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("notifications", newArray.toString());
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}

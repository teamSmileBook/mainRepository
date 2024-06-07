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

// RecyclerView의 아이템을 표시하기 위한 Adapter 파일. 알림 목록을 관리하고 사용자가 알림을 삭제할 수 있는 제공
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

    // 새로운 ViewHolder 생성
    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(itemView);
    }

    // ViewHolder의 데이터를 설정
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmData currentItem = alarmList.get(position);
        holder.alarmTextView.setText(currentItem.getAlarm_text());

        // 알림 삭제 기능 구현
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    alarmList.remove(adapterPosition); // 목록에서 항목 제거
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

    // 데이터 리스트의 크기 반환
    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}

package com.example.smilebook.alarm;

// RecyclerView의 알림 목록에 표시될 각 알림 데이터를 나타냄.
public class AlarmData {
    private String alarmText;

    // 알림 텍스트 반환
    public String getAlarm_text() {
        return alarmText;
    }

    // 알림 텍스트 설정
    public void setAlarm_text(String alarmText) {
        this.alarmText = alarmText;
    }
}

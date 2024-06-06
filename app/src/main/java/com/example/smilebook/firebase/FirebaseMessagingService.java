package com.example.smilebook.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.smilebook.MainActivity;
import com.example.smilebook.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Firebase 클라우드 메시징을 통해 수신된 메시지를 처리
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String ALARM_KEY = "alarms";
    private static final String TAG = "MyFirebaseMsgService";

    // 메시지 수신 시 호출되는 메서드
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            // 알림 내용 저장
            showNotification(title, body);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    // 알림 값을 직접 SharedPreferences에 추가하는 메서드
    private void saveTestAlarm(String title, String body) {
        SharedPreferences sharedPreferences = getSharedPreferences("AlarmStorage", MODE_PRIVATE);
        Set<String> alarms = sharedPreferences.getStringSet(ALARM_KEY, new HashSet<>());
        alarms.add(title + ":" + body);

        // 저장된 값은 최근 10개 알림으로 유지
        List<String> alarmList = new ArrayList<>(alarms);
        if (alarmList.size() > 10) {
            alarmList = alarmList.subList(alarmList.size() - 10, alarmList.size());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(ALARM_KEY, new HashSet<>(alarmList));
        editor.apply();
    }

    // 알림을 생성하고 표시하는 메서드
    private void showNotification(String title, String body) {
        // 알림 생성 및 표시
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannelId")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.icons8_smile_32)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, builder.build());
    }

    // 알림 채널을 생성하는 메서드
    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyChannelId", "MyChannel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

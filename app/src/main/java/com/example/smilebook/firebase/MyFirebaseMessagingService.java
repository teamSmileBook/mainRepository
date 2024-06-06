package com.example.smilebook.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.smilebook.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Firebase 클라우드 메시징을 통해 수신된 메시지를 처리
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "default_channel_id";
    private static final String CHANNEL_NAME = "Default Channel";

    // 메시지 수신 시 호출되는 메서드
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            showNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            // SharedPreferences에 알림 저장
            saveDataToSharedPreferences(getApplicationContext(), title, body);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    //수신한 알림을 SharedPreferences에 저장하는 메서드
    private void saveDataToSharedPreferences(Context context, String title, String body) {
        SharedPreferences prefs = context.getSharedPreferences("FCM_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String existingData = prefs.getString("notifications", "");
        JSONArray jsonArray;

        try {
            if (existingData.isEmpty()) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(existingData);
            }

            // 알림의 최대 개수를 10개로 제한
            if (jsonArray.length() >= 10) {
                jsonArray.remove(0);
            }

            // 새로운 알림을 JSON 객체로 생성하여 배열에 추가
            JSONObject newNotification = new JSONObject();
            newNotification.put("title", title);
            newNotification.put("body", body);
            jsonArray.put(newNotification);

            // 업데이트된 배열을 SharedPreferences에 저장
            editor.putString("notifications", jsonArray.toString());
            editor.apply();

            // 저장된 body 로그 출력
            String savedData = prefs.getString("notifications", "");
            Log.d(TAG, "Current SharedPreferences data: " + savedData);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON Exception: " + e.getMessage());
        }
    }


    // 알림을 생성하고 표시하는 메서드
    private void showNotification(Context context, String title, String body) {
        // Notification을 생성하고 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icons8_smile_32)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(0, builder.build());
    }


    private void createNotificationChannel(NotificationManager notificationManager) {
        // Android O 이상에서는 NotificationChannel을 생성하여 사용해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // BroadcastReceiver를 통해 알림을 수신하고 표시하는 내부 클래스
    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");
            showNotification(context, title, body);
        }

        // 알림을 생성하고 표시하는 메서드
        private void showNotification(Context context, String title, String body) {
            // Notification을 생성하고 표시
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            createNotificationChannel(notificationManager);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icons8_smile_32)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager.notify(0, builder.build());
        }

        private void createNotificationChannel(NotificationManager notificationManager) {
            // Android O 이상에서는 NotificationChannel을 생성하여 사용해야 함
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
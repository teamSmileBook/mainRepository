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

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "default_channel_id";
    private static final String CHANNEL_NAME = "Default Channel";

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

            if (jsonArray.length() >= 10) {
                jsonArray.remove(0);
            }

            JSONObject newNotification = new JSONObject();
            newNotification.put("title", title);
            newNotification.put("body", body);
            jsonArray.put(newNotification);

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

    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");
            showNotification(context, title, body);
        }

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
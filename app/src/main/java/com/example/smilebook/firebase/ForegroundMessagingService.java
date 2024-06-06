package com.example.smilebook.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.smilebook.MainActivity;
import com.example.smilebook.R;
import com.google.firebase.messaging.RemoteMessage;

//포그라운드에서 Firebase 클라우드 메시징을 통해 수신된 메시지를 처리
public class ForegroundMessagingService extends FirebaseMessagingService {

    private static final String TAG = "ForegroundMsgService";

    // 메시지 수신 시 호출되는 메서드
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 메시지에 데이터가 포함된 경우
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            showNotification(title, body);
        }

        // 메시지에 알림이 포함된 경우
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    // 알림을 생성하고 표시하는 메서드
    private void showNotification(String title, String body) {
        // 알림 관리자 생성
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "MyChannelId";
        String channelName = "MyChannel";

        // 알림 채널을 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 클릭 시 MainActivity로 이동하도록 인텐트 설정
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림 빌더 생성 및 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.icons8_smile_32)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        // 알림 생성 및 표시
        Notification notification = builder.build();
        notificationManager.notify(0, notification);
    }
}
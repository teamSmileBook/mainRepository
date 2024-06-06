package com.example.smilebook.firebase;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.firebase.FirebaseApp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//애플리케이션 전체에서 공통적으로 사용되는 설정을 초기화하고 관리
public class YourApplicationClass extends Application {

    // 액티비티들의 약한 참조를 보관하기 위한 리스트
    private static List<WeakReference<Activity>> activities = new ArrayList<>();

    // 애플리케이션이 시작될 때 호출되는 메서드
    @Override
    public void onCreate() {
        super.onCreate();
        initializeFirebaseApp(); // Firebase 앱 초기화
        startForegroundService(); // Foreground Service 시작
    }

    // Firebase 앱을 초기화하는 메서드
    private void initializeFirebaseApp() {
        // Firebase 앱 초기화
        if(FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }

    // Foreground Service를 시작하는 메서드
    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForegroundMessagingService.class);
        startService(serviceIntent);
    }

    // 활동을 리스트에 추가하는 메서드
    public static void addActivity(Activity activity) {
        activities.add(new WeakReference<>(activity));
    }

    // 활동을 리스트에서 제거하는 메서드
    public static void removeActivity(Activity activity) {
        for (WeakReference<Activity> reference : activities) {
            Activity currentActivity = reference.get();
            if (currentActivity == activity) {
                activities.remove(reference);
                break;
            }
        }
    }

    // 현재 리스트에 있는 모든 활동을 반환하는 메서드
    public static List<WeakReference<Activity>> getActivities() {
        return activities;
    }
}
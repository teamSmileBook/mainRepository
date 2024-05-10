package com.example.smilebook;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.firebase.FirebaseApp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class YourApplicationClass extends Application {

    private static List<WeakReference<Activity>> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFirebaseApp(); // Firebase 앱을 초기화합니다.
        startForegroundService(); // Foreground Service를 시작합니다.
    }

    private void initializeFirebaseApp() {
        // FirebaseApp을 초기화합니다.
        if(FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, ForegroundMessagingService.class);
        startService(serviceIntent);
    }

    public static void addActivity(Activity activity) {
        activities.add(new WeakReference<>(activity));
    }

    public static void removeActivity(Activity activity) {
        for (WeakReference<Activity> reference : activities) {
            Activity currentActivity = reference.get();
            if (currentActivity == activity) {
                activities.remove(reference);
                break;
            }
        }
    }

    public static List<WeakReference<Activity>> getActivities() {
        return activities;
    }
}
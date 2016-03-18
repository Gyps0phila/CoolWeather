package com.example.coolweather.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gypsophila on 2016/3/18.
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();

            }
        }
    }
}

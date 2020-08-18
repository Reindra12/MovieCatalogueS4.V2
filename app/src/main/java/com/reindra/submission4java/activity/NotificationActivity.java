package com.reindra.submission4java.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.reindra.submission4java.R;
import com.reindra.submission4java.notification.DailyReceiver;
import com.reindra.submission4java.notification.ReleaseReceiver;

public class NotificationActivity extends AppCompatActivity {
    public static String DAILY;
    public static String RELEASE;
    private static String SETTING_PREFS = "";
    private Switch swDaily;
    private Switch swRelease;
    private boolean checkDaily = false;
    private boolean checkRelease = false;
    private DailyReceiver dailyReceiver;
    private ReleaseReceiver releaseReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        dailyReceiver = new DailyReceiver();
        releaseReceiver = new ReleaseReceiver();

        swDaily = findViewById(R.id.sw_daily);
        swRelease = findViewById(R.id.sw_release);

        loadPref();
        swDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean dailyIsChecked) {
                checkDaily = dailyIsChecked;
                setPref();
                if (dailyIsChecked) {
                    dailyReceiver.dailyNotificationOn(NotificationActivity.this);
                } else {
                    dailyReceiver.dailyNotificationOff(NotificationActivity.this);
                }
            }
        });
        swRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean releaseIsChecked) {
                checkRelease = releaseIsChecked;
                setPref();
                if (releaseIsChecked) {
                    releaseReceiver.releaseNotificationOn(NotificationActivity.this);
                } else {
                    releaseReceiver.releaseNotificationOff(NotificationActivity.this);
                }
            }
        });
    }

    private void setPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DAILY, checkDaily);
        editor.putBoolean(RELEASE, checkRelease);
        editor.apply();
    }

    private void loadPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        swDaily.setChecked(sharedPreferences.getBoolean(DAILY, false));
        swRelease.setChecked(sharedPreferences.getBoolean(RELEASE, false));
    }
}

package com.wu.testdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!SettingsCompat.canDrawOverlays(this)) {
                Log.e(TAG, "申请悬浮窗权限 ");
                SettingsCompat.manageDrawOverlays((Activity) this, 0);
            } else {
                Log.e(TAG, "已申请悬浮窗权限，打开mService ");
                startService(new Intent(this, mService.class));
                finish();
            }
        } else {
            Log.e(TAG, "低版本不用申请悬浮窗权限，打开mService ");
            startService(new Intent(this, mService.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!SettingsCompat.canDrawOverlays(this)) {
                    SettingsCompat.manageDrawOverlays((Activity) this, 0);
                } else {
                    startService(new Intent(this, mService.class));
                    finish();
                }
            }
        }
    }
}

package com.wu.testdemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class mService extends Service {
    private String TAG = mService.class.getSimpleName();

    private WindowManager mWindowManager;
    private View convertView;
    private Button upBtn, downBtn, editBtn;
    private EditText edit;
    private LayoutInflater inflater;
    private int h = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onStartCommand----");
        // 获取WindowManager,初始化全局滚动文章窗口
        mWindowManager = (WindowManager) mService.this.getSystemService(Context.WINDOW_SERVICE);
        inflater = LayoutInflater.from(this);
        convertView = inflater.inflate(R.layout.view_main, null);
        upBtn = convertView.findViewById(R.id.btn_up);
        downBtn = convertView.findViewById(R.id.btn_down);
        editBtn = convertView.findViewById(R.id.btn_edit);
        edit = convertView.findViewById(R.id.edit);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h < 20)
                    h++;
                else
                    h = 0;
                Intent intent = new Intent("com.wu.liveplayer.action.POSITION");
                intent.setComponent(new ComponentName("com.wu.liveplayer", "com.wu.liveplayer.receiver.AutoStartReceiver"));
                intent.putExtra("position", h);
                sendBroadcast(intent);
                Log.e(TAG, "向上换台---- ");
            }
        });

        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h > 0)
                    h--;
                else
                    h = 20;
                Intent intent = new Intent("com.wu.liveplayer.action.POSITION");
                intent.setComponent(new ComponentName("com.wu.liveplayer", "com.wu.liveplayer.receiver.AutoStartReceiver"));
                intent.putExtra("position", h);
                sendBroadcast(intent);
                Log.e(TAG, "向下换台---- ");
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit.getText().toString())) {
                    Toast.makeText(mService.this, "请先输入频道数字", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent("com.wu.liveplayer.action.POSITION");
                intent.setComponent(new ComponentName("com.wu.liveplayer", "com.wu.liveplayer.receiver.AutoStartReceiver"));
                intent.putExtra("position", Integer.parseInt(edit.getText().toString()));
                sendBroadcast(intent);
                Log.e(TAG, "输入频道数字换台---- ");
            }
        });

        mWindowManager.addView(convertView, getMaxParams());

        return super.onStartCommand(intent, flags, startId);
    }


    public WindowManager.LayoutParams getMaxParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = WindowManager.LayoutParams.MATCH_PARENT;
        params.y = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        if (Build.VERSION.SDK_INT >= 26) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        return params;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (convertView != null) {
            mWindowManager.removeView(convertView);
        }
        convertView = null;
        Log.i(TAG, "removeCallbacks------");
    }
}

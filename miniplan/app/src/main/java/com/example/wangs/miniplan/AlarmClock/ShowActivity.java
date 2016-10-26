package com.example.wangs.miniplan.AlarmClock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wangshuo on 16/3/29.
 */
public class ShowActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        if (!getIntent().getBooleanExtra("screen_off", false)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            Log.d("AlarmClokc", "唤醒屏幕");
        }
        //震动
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //等待1秒，震动2秒，等待1秒，震动3秒
        long[] pattern = { 1000, 1000, 1000};
        //-1表示不重复, 如果不是-1, 比如改成1, 表示从前面这个long数组的下标为1的元素开始重复.
        vibrator.vibrate(pattern, 1);

        //唤醒屏幕之后弹出提示框
//        String planName = this.getIntent().getStringExtra("planName");
//        Log.d("AlarmClock",planName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您该执行计划了！")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vibrator.cancel();
                        ShowActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    protected void onDestroy() {
        AlarmAlertWakeLock.releaseCpuLock();
        super.onDestroy();
    }
}

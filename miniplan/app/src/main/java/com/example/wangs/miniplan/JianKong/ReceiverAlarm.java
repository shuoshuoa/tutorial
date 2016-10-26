package com.example.wangs.miniplan.JianKong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by wangshuo on 16/2/29.
 */
public class ReceiverAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"接收到了一个广播!",Toast.LENGTH_SHORT).show();
        int flag = intent.getIntExtra("flag",0);
        if(flag == 0){
            Intent intent1 = new Intent(context,ServiceBlog.class);
            intent1.putExtra("flag",flag);
            context.startService(intent1);
        }
        else{
            Toast.makeText(context,"flag ＝＝ 1，要求停止service",Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.wangs.miniplan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.wangs.miniplan.AlarmClock.AlarmClockService;
import com.example.wangs.miniplan.AllPlans.AllPlans;
import com.example.wangs.miniplan.Database.PlanDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangshuo on 16/3/2.
 */
public class UpdatePlan extends Activity {
    private int plan_num = 1;
    private int planed_num = 1;
    private String today;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_main);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = simpleDateFormat.format(date);

        if(!isNewDay(today)){
            //如果今天为新的一天，则执行修改数据库计划操作
            ModifyPlan();
        }

        //延迟执行任务
        new Handler().postDelayed(new Runnable(){
            public void run() {
                onPostExecute();
            }
        }, 2000);

    }
    //修改数据库计划操作
    private void ModifyPlan() {
        ContentValues value = new ContentValues();
        value.put("isDone", ">");

        final PlanDatabase helper = new PlanDatabase(getApplicationContext());
        Cursor c = helper.query("PlanTable1");
        while (c.moveToNext()) {
//            plan_num++;

            if (!c.getString(5).equals(">")) {
                helper.update(value, c.getInt(0), "PlanTable1");
                Log.d("shuoshuo",c.getInt(0)+"");
            }
        }
//        for (int i = 1; i <= plan_num; i++) {
//                helper.update(value, i, "PlanTable1");
//        }
        helper.close();


        final PlanDatabase helper1 = new PlanDatabase(getApplicationContext());
        Cursor c1 = helper1.query("PlanTable2");
        while (c1.moveToNext()){
            Log.d("shuoshuodel",c1.getInt(0)+"");
            helper1.del(c1.getInt(0),"PlanTable2");
        }
//        for(int i = 1; i<=planed_num; i++){
//            helper.del(i,"PlanTable2");
//        }
        helper1.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("Date", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(today);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public boolean isNewDay(String today){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("Date");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String date = content.toString();
        return today.equals(date);
    }

    protected void onPostExecute() {
        //实现页面跳转
        Intent intent = new Intent(UpdatePlan.this, AllPlans.class);
        startActivity(intent);
//        finish();
        //两个参数分别表示进入的动画,退出的动画
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    };
}

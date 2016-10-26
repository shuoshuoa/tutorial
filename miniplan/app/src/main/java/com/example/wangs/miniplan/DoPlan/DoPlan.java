package com.example.wangs.miniplan.DoPlan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangs.miniplan.AllPlans.AllPlans;
import com.example.wangs.miniplan.Database.PlanDatabase;
import com.example.wangs.miniplan.R;
import com.example.wangs.miniplan.JianKong.ServiceBlog;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by wangshuo on 16/2/20.
 */

public class DoPlan extends Activity {

    private TextView tv_hour_decade, tv_hour_unit, tv_minute_decade, tv_minute_unit, tv_second_decade, tv_second_unit;
    private int tv_hour, tv_minute, tv_second;
    private CountDownTimerView timerView;
    private TextView PlanName, doPlan_back, doPlan, giveUpPlan;
    private TextView finished_plan_name;
    private TextView supervise;
    private int finished_time, plan_time;
    private int hour, minute;
    private String str_planName, str_planTime;
    private int _id;
    private int percentage;
    private int planed_time;
    private String planing_time;
    private SharedPreferences mySharedPreferences;  //临时存储
    private LinearLayout linearStudent; //布局的变量，用来改变颜色
    private int flag = 0; //返回按键的标记，1 执行返回 0执行退到后台
     /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doplan_main);

        ViewInit();
        //通过Bundle获取传值
        final Bundle bundle = this.getIntent().getExtras();
        _id = bundle.getInt("_id");
        str_planName = bundle.getString("planName");
        str_planTime = bundle.getString("planTime");
        PlanName.setText(str_planName);

        str_planTime = str_planTime.trim();
        hour = Integer.parseInt(str_planTime.charAt(0) + "");
        for (int i = 0; i < str_planTime.length(); i++) {
            if (str_planTime.charAt(i) == '.') {
                minute = Integer.parseInt(str_planTime.charAt(i+1)+"")*6;
                break;
            }
        }
        plan_time = hour * 60 + minute;
        timerView.setTime(hour, minute, 0);
        final Intent intent1 = new Intent(DoPlan.this, ServiceBlog.class);

        //执行计划
        doPlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //倒计时开始
                flag =1;
                timerView.start();
                timerView.setBackgroundResource(R.drawable.clock_bgred120);

                doPlan.setVisibility(View.GONE);
                //放弃按钮显示
                giveUpPlan.setVisibility(View.VISIBLE);
                //返回按钮消失
                doPlan_back.setVisibility(View.INVISIBLE);
                //学生监督按钮消失
                supervise.setVisibility(View.INVISIBLE);
                //背景变为普通的
                linearStudent.setBackgroundColor(Color.WHITE);

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date newDate = new Date(System.currentTimeMillis());
                planing_time = dateFormat.format(newDate);

                startService(intent1);
            }
        });
        //是否要做一个判断，当倒计时正常结束时说明已完成任务，而不需要在放弃任务。

        giveUpPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
                if (tv_hour == 59 && tv_minute == 59 && tv_second == 59) {
                    getPercentage();
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String planed_date = simpleDateFormat.format(date);

                    Bundle bundle2 = new Bundle();
                    bundle2.putString("planedName", str_planName);
                    bundle2.putString("planTime", str_planTime);
                    bundle2.putInt("planedTime", plan_time);
                    bundle2.putString("planedDate", planed_date);
                    bundle2.putString("planingTime", planing_time);
                    bundle2.putInt("planedPercentage", 100);

                    Intent intent = new Intent(DoPlan.this, IsFinishedPlan.class);
                    intent.putExtras(bundle2);
                    startActivity(intent);

                    final PlanDatabase helper = new PlanDatabase(getApplicationContext());
                    ContentValues value = new ContentValues();
                    value.put("isDone", "√");
                    helper.update(value, _id, "PlanTable1");
                    helper.close();

                    stopService(intent1);
                    timerView.setBackgroundResource(R.drawable.clock_bg120);
                    doPlan.setVisibility(View.GONE);
                    giveUpPlan.setVisibility(View.VISIBLE);
                } else {
                    //获取放弃时的倒计时时间
                    getPercentage();
                    //获取执行计划的日期
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String planed_date = simpleDateFormat.format(date);

                    //利用Bundle传值，并切换界面到IsFinishedPlan，完成计划界面
                    final Bundle bundle1 = new Bundle();

                    //已执行计划名，计划时长，执行时长，执行日期，执行时间，完成率
                    bundle1.putString("planedName", str_planName);
                    bundle1.putString("planTime", str_planTime);
                    bundle1.putInt("planedTime", planed_time);
                    bundle1.putString("planedDate", planed_date);
                    bundle1.putString("planingTime", planing_time);
                    bundle1.putInt("planedPercentage", percentage);

                    //找到存储文件
                    mySharedPreferences = getSharedPreferences("passwordBook", MODE_PRIVATE);
                    //获取存储的密码字符串
                    final String password = mySharedPreferences.getString("password", null);
                    if (!TextUtils.isEmpty(password)) {
                        //首先定义输入框的变量
                        final EditText editText = new EditText(DoPlan.this);
                        //判断密码不为空之后，就要弹出输入密码窗体
                        AlertDialog.Builder builder = new AlertDialog.Builder(DoPlan.this);
                        builder.setTitle("请输入密码");

                        builder.setIcon(R.drawable.ic_lock_open_red_500_18dp);
                        builder.setView(editText);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (password.equals(editText.getText().toString()) == true) {
                                    //先初始化密码，将其置为空
                                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                                    editor.putString("password",null);
                                    editor.commit();
                                    //之后就跳转
                                    //修改计划数据库
                                    final PlanDatabase helper = new PlanDatabase(getApplicationContext());
                                    ContentValues value = new ContentValues();
                                    value.put("isDone", "√");
                                    helper.update(value, _id, "PlanTable1");
                                    helper.close();

                                    stopService(intent1);
                                    Intent intent = new Intent(DoPlan.this,IsFinishedPlan.class);
                                    intent.putExtras(bundle1);
                                    startActivity(intent);
                                    timerView.setBackgroundResource(R.drawable.clock_bg120);
                                } else {
                                    Toast.makeText(DoPlan.this, "输入错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();

                    }else{
                        //判断密码为空之后，就要弹出输入确认窗体
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoPlan.this);
                        builder1.setTitle("确定要退出么！");
                        builder1.setIcon(R.drawable.ic_announcement_red_500_18dp);
                        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(DoPlan.this, IsFinishedPlan.class);
                                timerView.stop();

                                intent.putExtras(bundle1);
                                startActivity(intent);

                                //停止service
                                final PlanDatabase helper = new PlanDatabase(getApplicationContext());
                                ContentValues value = new ContentValues();
                                value.put("isDone", "√");
                                helper.update(value, _id, "PlanTable1");
                                helper.close();
                                stopService(intent1);
                                timerView.setBackgroundResource(R.drawable.clock_bg120);
                            }
                        });
                        builder1.setNegativeButton("取消", null);
                        builder1.show();
                    }
//                    //倒计时继续
//                    timerView.start();
                }
            }
        });


        doPlan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到存储文件
                mySharedPreferences = getSharedPreferences("passwordBook", MODE_PRIVATE);
                //获取存储的密码字符串
                final String password = mySharedPreferences.getString("password", null);
                if (!TextUtils.isEmpty(password)) {
                    //首先定义输入框的变量
                    final EditText editText = new EditText(DoPlan.this);
                    //判断密码不为空之后，就要弹出输入密码窗体
                    AlertDialog.Builder builder = new AlertDialog.Builder(DoPlan.this);
                    builder.setTitle("请输入密码");
                    builder.setIcon(R.drawable.headphoto);
                    builder.setView(editText);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (password.equals(editText.getText().toString()) == true) {
                                //先初始化密码，将其置为空
                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                editor.putString("password",null);
                                editor.commit();
                                //之后就跳转
                                Intent intent = new Intent(DoPlan.this, AllPlans.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DoPlan.this, "输入错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();

                }else{
                    //没有倒计时就跳转
                    Intent intent = new Intent(DoPlan.this, AllPlans.class);
                    startActivity(intent);
                }

            }
        });
        supervise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoPlan.this, Supervise.class);
                Bundle bundle = new Bundle();
                bundle.putInt("_id", _id);
                bundle.putString("planName", str_planName);
                bundle.putString("planTime", str_planTime);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    //返回键监听，点击返回键程序退到后台
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag == 1) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ViewInit() {
        timerView = (CountDownTimerView) findViewById(R.id.timerView);
        PlanName = (TextView) findViewById(R.id.planName);
        doPlan_back = (TextView) findViewById(R.id.doPlan_back);
        doPlan = (TextView) findViewById(R.id.doPlan);
        supervise = (TextView) findViewById(R.id.supervise);
        giveUpPlan = (TextView) findViewById(R.id.giveUpPlan);
        finished_plan_name = (TextView) findViewById(R.id.finished_plan_name);
        linearStudent = (LinearLayout) findViewById(R.id.linearStudent);

        tv_hour_decade = (TextView) findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (TextView) findViewById(R.id.tv_hour_unit);
        tv_minute_decade = (TextView) findViewById(R.id.tv_min_decade);
        tv_minute_unit = (TextView) findViewById(R.id.tv_min_unit);
        tv_second_decade = (TextView) findViewById(R.id.tv_sec_decade);
        tv_second_unit = (TextView) findViewById(R.id.tv_sec_unit);
    }

    //是否要做一个判断，当倒计时正常结束时说明已完成任务，而不需要在放弃任务。
    //获取TextView的时分秒数值，并转换成int型
    private void getTime() {
        tv_hour = Integer.valueOf(tv_hour_decade.getText().toString()) * 10 + Integer.valueOf(tv_hour_unit.getText().toString());
        tv_minute = Integer.valueOf(tv_minute_decade.getText().toString()) * 10 + Integer.valueOf(tv_minute_unit.getText().toString());
        tv_second = Integer.valueOf(tv_second_decade.getText().toString()) * 10 + Integer.valueOf(tv_second_unit.getText().toString());
    }

    //求完成百分比
    private void getPercentage() {
        getTime();
        finished_time = tv_hour * 60 + tv_minute;
        planed_time = plan_time - finished_time;
        percentage = (plan_time - finished_time) * 100 / plan_time;
    }
}

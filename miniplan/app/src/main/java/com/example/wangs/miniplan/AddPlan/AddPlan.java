package com.example.wangs.miniplan.AddPlan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangs.miniplan.AllPlans.AllPlans;
import com.example.wangs.miniplan.AllPlans.plans;
import com.example.wangs.miniplan.Database.PlanDatabase;
import com.example.wangs.miniplan.DoPlan.IsFinishedPlan;
import com.example.wangs.miniplan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangs on 2016/1/22.
 */
public class AddPlan extends Activity {

    private Spinner planSpinner, timeSpinner;
    private ArrayAdapter<String> planAdapter, timeAdapter;
    private String plan, time, alarmTime = "12:30";
    private PlanDatabase planDatabase;
    private TextView tvAddPlan_finish, tvAddPlan_back;
    private String alarmTimeHour = "12", alarmTimeMinute = "30";

    private PickerView hour_pv;
    private PickerView minute_pv;

    //添加计划按钮
    private Button btn_add_plan, btn_add_plan_time;

    private List<String> allPlans, allTimes;
    private final String[] planStrings = {
            "自习", "阅读", "午休", "编程", "学英语"
    };
    private final String[] timeStrings = {
            "0.5小时", "1.0小时", "1.5小时", "2.0小时", "2.5小时", "3.0小时", "3.5小时"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addplan_main);
        //初始化控件
        ViewInit();

        allPlans = new ArrayList<String>();
        for (int i = 0; i < planStrings.length; i++) {
            allPlans.add(planStrings[i]);
        }
        //planSpinner设置
        //将数据源与Adapter连接起来
        SetplanSpinner();
        //监听添加计划按钮
        btn_add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先定义输入框的变量
                final EditText editText = new EditText(AddPlan.this);
                //弹出输入密码窗体
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPlan.this);
                builder.setTitle("计划名（四字之内）");
                builder.setIcon(R.drawable.ic_bookmark_green_500_18dp);
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String plan = editText.getText().toString();
                        if (plan.length() > 4) {
                            Toast.makeText(AddPlan.this, "计划最多为四个字！", Toast.LENGTH_SHORT).show();
                        } else {
                            /* 先比较添加的值是否已存在，不存在才可添加 */
                            for (int i = 0; i < planAdapter.getCount(); i++) {
                                if (plan.equals(planAdapter.getItem(i))) {
                                    return;
                                }
                            }
                            if (!plan.equals("")) {
                                //将计划添加到字符串planStrings中
                                //  planStrings[planStrings.length] = plan;
                                /* 将值添加到adapter */
                                planAdapter.add(plan);
                                /* 取得添加的值的位置 */
                                int position = planAdapter.getPosition(plan);
                                /* 将Spinner选择在添加的值的位置 */
                                planSpinner.setSelection(position);
                                /* 将myEditText清空 */
                                editText.setText("");
                            }
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        allTimes = new ArrayList<String>();
        for (int i = 0; i < timeStrings.length; i++) {
            allTimes.add(timeStrings[i]);
        }
        //将数据源与Adapter连接起来
        //timeSpinner设置
        SettimeSpinner();
        //监听添加时间段按钮
        btn_add_plan_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先定义输入框的变量
                final EditText editText = new EditText(AddPlan.this);
                //弹出输入密码窗体
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPlan.this);
                builder.setTitle("时长（格式: *.*小时)");
                builder.setIcon(R.drawable.ic_timer_green_500_24dp);
                builder.setView(editText);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String plan_time = editText.getText().toString();

                            /* 先比较添加的值是否已存在，不存在才可添加 */
                            for (int i = 0; i < timeAdapter.getCount(); i++) {
                                if (plan_time.equals(timeAdapter.getItem(i))) {
                                    return;
                                }
                            }
                            if (!plan_time.equals("")) {
                                /* 将值添加到adapter */
                                timeAdapter.add(plan_time);
                                /* 取得添加的值的位置 */
                                int position = timeAdapter.getPosition(plan_time);
                                /* 将Spinner选择在添加的值的位置 */
                                timeSpinner.setSelection(position);
                                /* 将myEditText清空 */
                                editText.setText("");
                            }
                        }

                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });



        planSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //选择时间

        hour_pv = (PickerView) findViewById(R.id.hour_pv);
        minute_pv = (PickerView) findViewById(R.id.minute_pv);
        List<String> data = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            data.add("" + i);
        }
        for (int i = 0; i < 60; i++) {
            seconds.add(i < 24 ? "" + i : "" + i);
        }
        hour_pv.setData(data);
        hour_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text1) {

                alarmTimeHour = text1;
                if (alarmTimeHour.length() == 1)
                    alarmTimeHour = "0" + alarmTimeHour;
                alarmTime = alarmTimeHour + ":" + alarmTimeMinute;
                //    Toast.makeText(AddPlan.this,"选择的时间是"+alarmTime,Toast.LENGTH_SHORT).show();

            }
        });
        minute_pv.setData(seconds);
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text2) {
                alarmTimeMinute = text2;
                if (alarmTimeMinute.length() == 1)
                    alarmTimeMinute = "0" + alarmTimeMinute;
                alarmTime = alarmTimeHour + ":" + alarmTimeMinute;
                //   Toast.makeText(AddPlan.this,"选择的时间是"+alarmTime,Toast.LENGTH_SHORT).show();
            }


        });


        //完成添加计划
        tvAddPlan_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("planTitle", plan);
                values.put("planTime", time);
                values.put("alarm", "提醒");
                values.put("alarmTime", alarmTime);
                values.put("isDone", ">");

                planDatabase = new PlanDatabase(getApplicationContext());
                planDatabase.insert(values, "PlanTable1");

                //跳转到计划界面

                Intent intent = new Intent(AddPlan.this, AllPlans.class);
                startActivity(intent);

            }
        });

        //返回跳转
        tvAddPlan_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlan.this, AllPlans.class);
                startActivity(intent);
            }
        });
    }


    private void ViewInit() {
        planSpinner = (Spinner) findViewById(R.id.planSpinner);
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        tvAddPlan_finish = (TextView) findViewById(R.id.tvAddPlan_finish);
        tvAddPlan_back = (TextView) findViewById(R.id.tvAddPlan_back);
        btn_add_plan = (Button)findViewById(R.id.btn_add_plan_name);
        btn_add_plan_time = (Button)findViewById(R.id.btn_add_plan_time);
    }

//    private void StringsInit(){
//        planStrings = getResources().getStringArray(R.array.planSpinner);
//        timeStrings = getResources().getStringArray(R.array.timeSpinner);
//    }

    //    private void SetSpinner()  {
//        //planSpinner设置
//        //将数据源与Adapter连接起来
//        planAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,planStrings);
//        //设置Spinner下拉样式
//        planAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //将Adapter添加到Spinner中
//        planSpinner.setAdapter(planAdapter);
//        //timeSpinner设置
//        timeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,timeStrings);
//        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        timeSpinner.setAdapter(timeAdapter);
//    }
    private void SetplanSpinner() {
        //planSpinner设置
        //将数据源与Adapter连接起来
        planAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allPlans);
        //设置Spinner下拉样式
        planAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将Adapter添加到Spinner中
        planSpinner.setAdapter(planAdapter);

    }

    private void SettimeSpinner() {
        //timeSpinner设置
        //将数据源与Adapter连接起来
        timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allTimes);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

    }
}

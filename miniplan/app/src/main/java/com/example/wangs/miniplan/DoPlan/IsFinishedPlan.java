package com.example.wangs.miniplan.DoPlan;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangs.miniplan.AllPlans.AllPlans;
import com.example.wangs.miniplan.Database.PlanDatabase;
import com.example.wangs.miniplan.R;

/**
 * Created by wangshuo on 16/2/28.
 */
public class IsFinishedPlan extends Activity {
    private TextView finished_plan_name,score,welcome,finished_plan_finish;
    private PlanDatabase planDatabase;
    private EditText thinking;
    //已执行计划名，计划时长，执行时长，执行时间，执行日期，完成率
    private String planed_name,planed_date,planing_time,plan_time;
    private int planed_time,planed_percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planfinished_main);
        ViewInit();

        //获取传过来到值,并设置标题
        Bundle bundle = this.getIntent().getExtras();

        planed_name = bundle.getString("planedName");
        plan_time = bundle.getString("planTime");
        planed_time = bundle.getInt("planedTime");
        planed_date = bundle.getString("planedDate");
        planing_time = bundle.getString("planingTime");
        planed_percentage = bundle.getInt("planedPercentage");

      //  Toast.makeText(IsFinishedPlan.this,plan_name+plan_percentage,Toast.LENGTH_SHORT).show();
        finished_plan_name.setText(planed_name);

        //提示语设置
        if(planed_percentage == 100){
            welcome.setText("好样的，计划完成！！！");
            score.setText(planed_percentage+" %");
        }else{
            welcome.setText("真遗憾，你没有完成计划!!!");
            score.setText(planed_percentage+" %");
        }
        //单击完成事件
        finished_plan_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将数据存到数据库中
                ContentValues values = new ContentValues();

                values.put("planedName",planed_name);
                values.put("planTime",plan_time);
                values.put("planedTime",planed_time);
                values.put("planedDate",planed_date);
                values.put("planingTime",planing_time);
                values.put("planedMood",thinking.getText().toString());
                values.put("planedPercentage",planed_percentage);

                planDatabase = new PlanDatabase(getApplicationContext());
                //存入每天更新计划表（已执行计划）
                planDatabase.insert(values,"PlanTable2");
                //存入长期保存计划表（已执行计划）
                planDatabase.insert(values,"PlanTable3");
                //跳转
                Intent intent = new Intent(IsFinishedPlan.this, AllPlans.class);
                startActivity(intent);
            }
        });

    }
    //返回键监听，点击返回键程序退到后台
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ViewInit(){
        finished_plan_name = (TextView)findViewById(R.id.finished_plan_name);
        score = (TextView)findViewById(R.id.score);
        thinking = (EditText)findViewById(R.id.thinking);
        welcome = (TextView)findViewById(R.id.welcome);
        finished_plan_finish = (TextView)findViewById(R.id.finished_plan_finish);
    }
}

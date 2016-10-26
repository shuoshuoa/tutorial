package com.example.wangs.miniplan.CountPlan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wangs.miniplan.R;

/**
 * Created by wangshuo on 16/2/28.
 */
public class PlanedDetail extends Activity {
    private TextView plandetail_planedtime,plandetail_percentage,plandetail_date,plandetail_mood;
    private TextView plandetail_name,plandetail_back;
    private int planed_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planedtail_main);

        ViewInit();

        final Bundle bundle = this.getIntent().getExtras();
        planed_num = bundle.getInt("planed_num");
        String planed_name = bundle.getString("planed_name");
        int planed_time = bundle.getInt("planed_time");
        String planed_date = bundle.getString("planed_date");
        String planing_time = bundle.getString("planing_time");
        String planed_mood = bundle.getString("planed_mood");
        int planed_percentage = bundle.getInt("planed_percentage");

        plandetail_name.setText(planed_name);
        plandetail_planedtime.setText(planed_time+":00"+" min");
        plandetail_percentage.setText(planed_percentage+"%");
        plandetail_date.setText(planed_date+"   "+planing_time);
        plandetail_mood.setText(planed_mood);

        plandetail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanedDetail.this,CountActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("plan_num",planed_num);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private void ViewInit(){
        plandetail_back = (TextView)findViewById(R.id.plandetail_back);

        plandetail_name = (TextView)findViewById(R.id.plandetail_name);

        plandetail_planedtime = (TextView)findViewById(R.id.plandetail_planedtime);

        plandetail_percentage = (TextView)findViewById(R.id.plandetail_percentage);

        plandetail_date = (TextView)findViewById(R.id.plandetail_date);

        plandetail_mood = (TextView)findViewById(R.id.plandetail_mood);

    }
}

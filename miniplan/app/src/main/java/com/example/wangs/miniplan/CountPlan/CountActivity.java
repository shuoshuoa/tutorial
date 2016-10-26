package com.example.wangs.miniplan.CountPlan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wangs.miniplan.AllPlans.AllPlans;
import com.example.wangs.miniplan.Database.PlanDatabase;
import com.example.wangs.miniplan.Home.HomeActivity;
import com.example.wangs.miniplan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renjie on 2016/1/20.
 */
public class CountActivity extends Activity {

    private TextView Plans, Count, Home, tvRate;
    private ListView planedList;
    private MyAdapter myAdapter;
    private List<planeds> planedses;
    private int plan_num;
    private int planed_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_main);
        ViewInit();

        //实例化数据库帮助类
        final PlanDatabase helper = new PlanDatabase(this);
        //查询获得游标
        Cursor c = helper.query("PlanTable2");

        planedses = new ArrayList<planeds>();

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String planed_name = c.getString(1);
            String plan_time = c.getString(2);
            int planed_time = c.getInt(3);
            String planed_date = c.getString(4);
            String planing_time = c.getString(5);
            String planed_mood = c.getString(6);
            int planed_percentage = c.getInt(7);

            planed_num++;
            //   Toast.makeText(CountActivity.this,planed_name+planed_time+planed_percentage+planed_thought,Toast.LENGTH_SHORT).show();
            planedses.add(new planeds(id,planed_name,plan_time,planed_time,planed_date,planing_time,planed_mood,planed_percentage));
        }
        myAdapter = new MyAdapter(this, planedses);
        planedList.setAdapter(myAdapter);
        //在控件 tvRate上填充内容（完成计划的比例）
        setRate();

        planedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                planeds planed = planedses.get(position);

                String planed_name = planed.getPlanedName();
                int planed_time = planed.getPlanedTime();
                String planed_date = planed.getPlanedDate();
                String planing_time = planed.getPlaningTime();
                String planed_mood = planed.getPlanedMood();
                int planed_percentage = planed.getPlanedPercentage();

                //利用Bundle传值
                Bundle bundle = new Bundle();

                bundle.putInt("planed_num",plan_num);
                bundle.putString("planed_name",planed_name);
                bundle.putInt("planed_time",planed_time);
                bundle.putString("planed_date",planed_date);
                bundle.putString("planing_time",planing_time);
                bundle.putString("planed_mood",planed_mood);
                bundle.putInt("planed_percentage",planed_percentage);
                Intent intent = new Intent(CountActivity.this,PlanedDetail.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ChangeView();
    }

    private void setRate() {
        final Bundle bundle = this.getIntent().getExtras();
        plan_num = bundle.getInt("plan_num");
        tvRate.setText(planed_num+"/"+plan_num);
    }


    private void ViewInit() {
        Home = (TextView) findViewById(R.id.Home);
        Plans = (TextView) findViewById(R.id.Plans);
        Count = (TextView) findViewById(R.id.Count);
        tvRate = (TextView) findViewById(R.id.tvRate);
        planedList = (ListView) findViewById(R.id.planedList);
        Plans.setBackgroundColor(Color.WHITE);
        Count.setBackgroundColor(Color.rgb(65, 199, 220));
        Home.setBackgroundColor(Color.WHITE);
    }
    //切换界面
    private void ChangeView() {
        Plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountActivity.this, AllPlans.class);
                startActivity(intent);
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CountActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("plan_num",plan_num);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

class MyAdapter extends BaseAdapter {
    private Context context;
    private List<planeds> datas;
    viewHolder holder;

    public MyAdapter(Context context, List<planeds> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.planedlistview_item, null);
            holder = new viewHolder();
            holder.planedView = (ImageView)convertView.findViewById(R.id.planedView);
            holder.planedName = (TextView) convertView.findViewById(R.id.planedName);
            holder.planedTime = (TextView) convertView.findViewById(R.id.planedTime);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
            holder.star = (ImageView)convertView.findViewById(R.id.star);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        planeds planed = datas.get(position);
        /*holder.textview0.setText(w.getId());*/
        switch (planed.getPlanedName()){
            case "阅读":
                holder.planedView.setImageResource(R.drawable.ic_book_brown_300_36dp);
                break;
            case "午休":
                holder.planedView.setImageResource(R.drawable.ic_airline_seat_flat_red_900_36dp);
                break;
            case "健身":
                holder.planedView.setImageResource(R.drawable.ic_fitness_center_brown_300_36dp);
                break;
            case "编程":
                holder.planedView.setImageResource(R.drawable.ic_remove_from_queue_brown_300_36dp);
                break;
            case "学英语":
                holder.planedView.setImageResource(R.drawable.ic_text_format_brown_300_36dp);
                break;
            case "自习":
                holder.planedView.setImageResource (R.drawable.ic_school_red_900_36dp);
        }
        holder.planedName.setText(planed.getPlanedName());
        holder.planedTime.setText(planed.getPlanTime());
        if(planed.getPlanedPercentage()!=100){
            holder.progress.setProgress(planed.getPlanedPercentage());
            holder.star.setVisibility(View.GONE);
            holder.progress.setVisibility(View.VISIBLE);
        }else{
            holder.progress.setVisibility(View.GONE);
            holder.star.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class viewHolder {
        ImageView planedView;
        TextView planedName;
        TextView planedTime;
        ProgressBar progress;
        ImageView star;
    }
}
package com.example.wangs.miniplan.AllPlans;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wangs.miniplan.AddPlan.AddPlan;
import com.example.wangs.miniplan.AlarmClock.AlarmClockService;
import com.example.wangs.miniplan.CountPlan.CountActivity;
import com.example.wangs.miniplan.Database.PlanDatabase;
import com.example.wangs.miniplan.DoPlan.DoPlan;
import com.example.wangs.miniplan.Home.HomeActivity;
import com.example.wangs.miniplan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renjie on 2016/1/20.
 */
public class AllPlans extends Activity {

    private ListView listview;
    private List<plans> datas;
    private MyAdapter myAdapter;
    private ImageView addPlan;
    private TextView Plans,Count,Home;
    private int plan_num = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allplan_main);

        ViewInit();
        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllPlans.this, AddPlan.class);
                startActivity(intent);
            }
        });


        //实例化数据库帮助类
        final PlanDatabase helper=new PlanDatabase(this);
        //查询获得游标
        Cursor c=helper.query("PlanTable1");

        datas= new ArrayList<plans>();

        while(c.moveToNext()){
            int id = c.getInt(0);
            String planTitle = c.getString(1);
            String planTime = c.getString(2);
            String alarm = c.getString(3);
            String alarmTime = c.getString(4);
            String isDone = c.getString(5);
            plan_num++;
            datas.add(new plans(id,planTitle,planTime,alarmTime,alarm,isDone));
        }


        myAdapter = new MyAdapter(this,datas);
        listview.setAdapter(myAdapter);

        //提示对话框
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_delete_red_500_18dp);
        //为日记的ListView设置长按监听器  用于删除
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                // TODO Auto-generated method stub

                builder.setTitle("真的要删除记录吗？").
                        setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                plans note = datas.get(position);
                                //删除数据

                                helper.del(note.getId(),"PlanTable1");
                                datas.remove(position);
                                //重新查询
                                myAdapter.notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
                return false;
            }
        });
        //单击事件，执行计划
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                plans plan = datas.get(position);
                //获取数据库中对应计划的计划名和时长
                if(plan.getIsDone().equals(">") == true) {
                    int _id = plan.getId();
                    String planName = plan.getPlanTitle();
                    String planTime = plan.getPlanTime();
                    //利用Bundle存储要传值的数据
                    Bundle bundle = new Bundle();
                    bundle.putInt("_id", _id);
                    bundle.putString("planName", planName);
                    bundle.putString("planTime", planTime);
                    Intent intent = new Intent(AllPlans.this, DoPlan.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        helper.close();


        Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("plan_num",plan_num);
                Intent intent = new Intent(AllPlans.this, CountActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("plan_num",plan_num);
                Intent intent = new Intent(AllPlans.this, HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
    protected void onPause(){
        super.onPause();
        Intent intent = new Intent(this,AlarmClockService.class);
        startService(intent);
    }

    protected void onDestroy()
    {
        Intent intent = new Intent(this, AlarmClockService.class);
        stopService(intent);
        super.onDestroy();
    }




    //返回键监听，点击返回键程序退到后台
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ViewInit() {
        Home = (TextView) findViewById(R.id.Home);
        Count = (TextView) findViewById(R.id.Count);
        Plans = (TextView) findViewById(R.id.Plans);
        addPlan = (ImageView) findViewById(R.id.addPlan);
        listview = (ListView)findViewById(R.id.lvPlanList);

        Plans.setBackgroundColor(Color.rgb(65, 199, 220));
        Count.setBackgroundColor(Color.WHITE);
        Home.setBackgroundColor(Color.WHITE);
    }

}

class MyAdapter extends BaseAdapter {
    viewHolder holder;
    private Context context;
    private List<plans> datas;


    public MyAdapter(Context context, List<plans> datas) {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.planlistview_item, null);
            holder = new viewHolder();
            holder.planView = (ImageView)convertView.findViewById(R.id.planView);
            holder.tvPlanTitle = (TextView) convertView.findViewById(R.id.tvPlanTitle);
            holder.tvPlanTime = (TextView) convertView.findViewById(R.id.tvPlanTime);
            holder.tvAlarmTime = (TextView) convertView.findViewById(R.id.tvAlarmTime);
           // holder.tvAlarm = (TextView) convertView.findViewById(R.id.tvAlarm);
            holder.isDone = (ImageView) convertView.findViewById(R.id.isDone);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        plans note = datas.get(position);
        /*holder.textview0.setText(w.getId());*/

        switch (note.getPlanTitle()){
            case "阅读":
                holder.planView.setImageResource(R.drawable.ic_chrome_reader_mode_brown_300_36dp);
                break;
            case "午休":
                holder.planView.setImageResource(R.drawable.ic_airline_seat_flat_red_900_36dp);
                break;
            case "健身":
                holder.planView.setImageResource(R.drawable.ic_fitness_center_brown_300_36dp);
                break;
            case "编程":
                holder.planView.setImageResource(R.drawable.ic_remove_from_queue_brown_300_36dp);
                break;
            case "学英语":
                holder.planView.setImageResource(R.drawable.ic_text_format_brown_300_36dp);
                break;
            case "自习":
                holder.planView.setImageResource (R.drawable.ic_school_red_900_36dp);
                break;
            default:
                holder.planView.setImageResource(R.drawable.ic_bookmark_red_800_36dp);
                break;
        }
        holder.tvPlanTitle.setText(note.getPlanTitle());
        holder.tvPlanTime.setText(note.getPlanTime());
        holder.tvAlarmTime.setText(note.getAlarmTime());
       // holder.tvAlarm.setText(note.getAlarm());

        //判断是否为“√”，之后添加颜色
        if(note.getIsDone().equals("√")){
            holder.isDone.setImageResource(R.drawable.ic_done_teal_600_36dp);
        }else {
            holder.isDone.setImageResource(R.drawable.ic_keyboard_arrow_right_teal_600_36dp);
        }
        return convertView;
    }

    class viewHolder {
        ImageView planView;
        TextView tvPlanTitle;
        TextView tvPlanTime;
        TextView tvAlarmTime;
        ImageView isDone;

    }
}


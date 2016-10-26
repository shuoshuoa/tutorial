package com.example.wangs.miniplan.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangs.miniplan.AllPlans.AllPlans;
import com.example.wangs.miniplan.CountPlan.CountActivity;
import com.example.wangs.miniplan.Database.PlanDatabase;
import com.example.wangs.miniplan.PieChartAnalyze.PieCharActivity;
import com.example.wangs.miniplan.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by renjie on 2016/1/20.
 */
public class HomeActivity extends Activity {

    private TextView Plans,Count,Home,tvHoldDays;
    private ListView home_listview;
    private EditText etUserName,etSaying;

    private int plan_num;
//    private ArrayList<String> planNameList=new ArrayList<>(); //计划名
    private String[] planNameList = new String[10];
    private int planed_num = 0;   //计算数据库中有多少个计划
    private int[] days = new int[10];  //某计划执行了多少天
//    private ArrayList<String> datas = new ArrayList<>();  //某计划最近一次执行
    private String[] datas = new String[10];
    private List<homeCount> counts;

    //获取某个计划完成率分别在20-50,<50,50-80,80-100,100四个范围的次数
    private int[] rate_20 = new int[10];
    private int[] rate_20_50 = new int[10];
    private int[] rate_50_80 = new int[10];
    private int[] rate_80_100 = new int[10];
    private int[] rate_100 = new int[10];

    private List<String> datesOfDone = new ArrayList<String>();
    int daysOfDone = 1;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        final Bundle bundle = this.getIntent().getExtras();
        plan_num = bundle.getInt("plan_num");
        ViewInit();
        //从文件中得到用户名和励志铭
        getNameAndMood();

        for (int i=0; i<10; i++)
        {
            days[i] = 0;
            rate_20[i] = 0;
            rate_20_50[i] = 0;
            rate_50_80[i] = 0;
            rate_80_100[i] = 0;
            rate_100[i] = 0;
        }
        final PlanDatabase helper = new PlanDatabase(this);

        Cursor c = helper.query("PlanTable1");


        while (c.moveToNext())
        {
            int flag = 0;
            String home_plan = c.getString(1);

            for (int i=0; i<planed_num; i++)
            {
                if (home_plan.equals(planNameList[i]))
                {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                planNameList[planed_num] = home_plan;
                planed_num++;
            }
        }
        helper.close();


        counts = new ArrayList<homeCount>();
        final PlanDatabase planedHelper = new PlanDatabase(this);
        c = planedHelper.query("PlanTable3");
        while (c.moveToNext())
        {
            int index = c.getInt(0);
            index = index-1;
            datesOfDone.add(index,c.getString(4));

            String home_plan = c.getString(1);
            for (int i=0; i<planed_num; i++)
            {
                if (home_plan.equals(planNameList[i]))
                {
                    days[i]++;
                    String date = c.getString(4);
                    String data = "最近执行 "+date + "  已坚持"+days[i]+"次";
                    datas[i] = data;

                    //获取某个计划完成率分别在<50,50-80,80-100,100四个范围的次数
                    int percentage = c.getInt(7);
                    if (percentage<20){
                        rate_20[i]++;
                    }
                    if (percentage<50 && percentage>=20){
                        rate_20_50[i]++;
                    }
                    if (percentage>=50 &&percentage<80){
                        rate_50_80[i]++;
                    }
                    if (percentage>=80 && percentage<100){
                        rate_80_100[i]++;
                    }
                    if (percentage == 100){
                        rate_100[i]++;
                    }
                    break;
                }
            }
        }
        planedHelper.close();

        for (int j=0; j<planed_num; j++)
        {
            if (days[j]==0)
            {
                String data = "该计划尚未开始执行";
                datas[j] = data;
            }
            counts.add(new homeCount(planNameList[j],datas[j]));
        }
        myAdapter = new MyAdapter(this,counts);
        home_listview.setAdapter(myAdapter);

        //获取已经坚持天数
        for (int i = 0; i<datesOfDone.size(); i++)
        {
            for (int j = i+1; j<datesOfDone.size(); j++)
            {
                if (datesOfDone.get(j).equals(datesOfDone.get(i)))
                {
                    continue;
                }
                else
                {
                    daysOfDone++;
                    i=j;
                    break;
                }
            }
        }
        tvHoldDays.setText(daysOfDone+"");



        //点击事件，跳转到饼状图分析
        home_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                homeCount home_plan = counts.get(position);
                String plan_name = home_plan.getHomePlan();
                for (int i=0; i<planed_num; i++)
                {
                    if (planNameList[i].equals(plan_name))
                    {
                        if (days[i]==0){
                            break;
                        }else {
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("rate_20", rate_20[i]);
                            bundle1.putInt("rate_20_50", rate_20_50[i]);
                            bundle1.putInt("rate_50_80", rate_50_80[i]);
                            bundle1.putInt("rate_80_100", rate_80_100[i]);
                            bundle1.putInt("rate_100", rate_100[i]);
                            bundle1.putInt("days", days[i]);
                            bundle1.putString("plan_name", plan_name);

                            Intent intent = new Intent(HomeActivity.this, PieCharActivity.class);
                            intent.putExtras(bundle1);
                            startActivity(intent);
                        }
                    }
                }
            }
        });


        Plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllPlans.class);
                startActivity(intent);

            }
        });
        Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("plan_num",plan_num);
                Intent intent = new Intent(HomeActivity.this, CountActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);

            }
        });
    }
    protected void onPause(){
        super.onPause();
        String userName = etUserName.getText().toString();
        fileSave(userName,"username");
        String mood = etSaying.getText().toString();
        fileSave(mood,"usermood");
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
        Count = (TextView) findViewById(R.id.Count);
        Plans = (TextView) findViewById(R.id.Plans);
        Home = (TextView) findViewById(R.id.Home);
        tvHoldDays = (TextView) findViewById(R.id.tvHoldDays);
        home_listview = (ListView)findViewById(R.id.home_listview);
        etUserName = (EditText)findViewById(R.id.etUserName);
        etSaying = (EditText)findViewById(R.id.etSaying);


        Plans.setBackgroundColor(Color.WHITE);
        Count.setBackgroundColor(Color.WHITE);
        Home.setBackgroundColor(Color.rgb(65, 199, 220));
    }
    /*
    *如果本地文件存储了用户名和励志铭就调取出来
     */

    private void getNameAndMood() {
        String userName = fileOpen("username");
        String mood = fileOpen("usermood");
        if(!TextUtils.isEmpty(userName)){
            etUserName.setText(userName);
            etUserName.setSelection(userName.length());
        }
        if(!TextUtils.isEmpty(mood)){
            etSaying.setText(mood);
            etSaying.setSelection(mood.length());
        }
    }

    /*
    * 用于保存用户输入的用户名和励志铭
    * */
    public void fileSave(String inputText,String fileName){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput(fileName,Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
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

    /*
    *用于从文件fileName中提取用户最近一次输入的用户名和签名
    */
    public String fileOpen(String fileName) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput(fileName);
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
        return content.toString();
    }

}

class MyAdapter extends BaseAdapter {
    private Context context;
    private List<homeCount> datas;
    viewHolder holder;

    public MyAdapter(Context context, List<homeCount> datas) {
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

            convertView = LayoutInflater.from(context).inflate(R.layout.home_planed_item, null);
            holder = new viewHolder();
            holder.homePlan = (TextView) convertView.findViewById(R.id.home_planName);
            holder.homeData = (TextView) convertView.findViewById(R.id.home_data);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        homeCount planed = datas.get(position);

        holder.homePlan.setText(planed.getHomePlan());
        holder.homeData.setText(planed.getHomeData());
        return convertView;
    }

    class viewHolder {
        TextView homePlan;
        TextView homeData;
    }
}

package com.example.wangs.miniplan.DoPlan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangs.miniplan.R;

/**
 * Created by wangshuo on 16/2/20.
 */
public class Supervise extends Activity {

    private TextView supervise_back;
    private TextView supervise_finish;
    private EditText password,password1;
    private SharedPreferences mySharedPreferences;
    private int id;
    private String plan_name, plan_time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervise_main);

        final Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("_id");
        plan_name = bundle.getString("planName");
        plan_time = bundle.getString("planTime");


        ViewInit();

        supervise_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Supervise.this,DoPlan.class);
                startActivity(intent);
            }
        });

        supervise_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySharedPreferences = getSharedPreferences("passwordBook",MODE_PRIVATE);
                String str_password = password.getText().toString();
                String str_password1 = password1.getText().toString();
                if(str_password == null || str_password.equals("")==true || str_password.equals(" ")==true || str_password.equals(str_password1)==false) {
                    Toast.makeText(Supervise.this, "输入密码错误！", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("password", password.getText().toString().toString());
                    editor.commit();
                    Intent intent = new Intent(Supervise.this,DoPlan.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("_id",id);
                    bundle1.putString("planName",plan_name);
                    bundle1.putString("planTime",plan_time);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }

            }
        });


    }

    private void ViewInit() {
        supervise_back = (TextView)findViewById(R.id.supervise_back);
        supervise_finish = (TextView)findViewById(R.id.supervise_finish);
        password = (EditText)findViewById(R.id.password);
        password1 = (EditText)findViewById(R.id.password1);
    }
}

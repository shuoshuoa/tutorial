package com.example.wangs.miniplan.Guidance;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.wangs.miniplan.R;
import com.example.wangs.miniplan.UpdatePlan;

/**
 * ViewPager 引导
 *com.zhangyx.MyLauncherGuide.activity.viewPage.ViewPagerActivity
 * @author Admin-zhangyx
 *
 * create at 2015-1-21 下午4:24:29
 */
public class ViewPagerActivity extends FragmentActivity {
	private ViewPager mVPActivity;
	private Fragment1 mFragment1;
	private Fragment2 mFragment2;
	private Fragment3 mFragment3;
	private List<Fragment> mListFragment = new ArrayList<Fragment>();
	private PagerAdapter mPgAdapter;
	//存储app启动的次数
	private SharedPreferences Preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		//判断启动次数函数
		judgeStartCount();
		initView();

	}
	private void initView() {
		mVPActivity = (ViewPager) findViewById(R.id.guide_view_group);
		mVPActivity.setOffscreenPageLimit(10);
		mFragment1 = new Fragment1();
		mFragment2 = new Fragment2();
		mFragment3 = new Fragment3();
		mListFragment.add(mFragment1);
		mListFragment.add(mFragment2);
		mListFragment.add(mFragment3);
		mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				mListFragment);
		mVPActivity.setAdapter(mPgAdapter);
	}

	//判断启动次数
	private void judgeStartCount()
	{
		////读取SharedPreferences中需要的数据
		Preferences = getSharedPreferences("count", MODE_PRIVATE);
		int count = Preferences.getInt("count", 0);
		//判断程序与第几次运行，如果是第一次运行则跳转到引导页面
		if (count != 0) {
		//实现页面跳转
		Intent intent = new Intent(ViewPagerActivity.this, UpdatePlan.class);
		startActivity(intent);
	}
		SharedPreferences.Editor editor = Preferences.edit();
		//存入数据
		editor.putInt("count", ++count);
		//提交修改
		editor.commit();

	}
}

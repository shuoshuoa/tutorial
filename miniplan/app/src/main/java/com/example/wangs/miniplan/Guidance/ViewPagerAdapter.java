package com.example.wangs.miniplan.Guidance;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> fragmentList=new ArrayList<Fragment>();
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);		
	}
	public ViewPagerAdapter(FragmentManager fragmentManager,List<Fragment> arrayList) {
		super(fragmentManager);
		this.fragmentList=arrayList;
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}
	public void destroyItem(ViewGroup container, int position, Object object) {
		//super.destroyItem(container, position, object);        }

	}
}

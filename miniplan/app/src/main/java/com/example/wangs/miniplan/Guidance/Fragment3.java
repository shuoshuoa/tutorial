package com.example.wangs.miniplan.Guidance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wangs.miniplan.PieChartAnalyze.AnimationUtil;
import com.example.wangs.miniplan.R;
import com.example.wangs.miniplan.UpdatePlan;

public class Fragment3 extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_3, container, false);
		view.findViewById(R.id.tvInNew).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//实现页面跳转
				startActivity(new Intent(getActivity(),
						UpdatePlan.class));
				AnimationUtil.finishActivityAnimation(getActivity());
			}
		});
		return view;
	}

}

package com.example.wangs.miniplan.PieChartAnalyze;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wangs.miniplan.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by wangshuo on 16/4/3.
 */
public class PieCharActivity extends Activity{
    private PieChart mChart;
    private FrameLayout chartContainer;
    //we are going to display the pie chart for smartphone market shares
    private float[] yData = new float[5];
    private String[] xData ={ "完成率<20","完成率20-50","完成率50－80","完成率>80","圆满完成"};
    private int counts;
    private String plan_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_piechart);

        chartContainer = (FrameLayout)findViewById(R.id.chartContainer);

        //得到传进来的数据
        Bundle bundle = getIntent().getExtras();
        yData[0] = bundle.getInt("rate_20");
        yData[1] = bundle.getInt("rate_20_50");
        yData[2] = bundle.getInt("rate_50_80");
        yData[3] = bundle.getInt("rate_80_100");
        yData[4] = bundle.getInt("rate_100");
        counts = bundle.getInt("days");
        plan_name = bundle.getString("plan_name");

        mChart = new PieChart(this);
        //add pie chart to main layout
        //mainLayout.addView(mChart);
        chartContainer.addView(mChart);
        //mainLayout.setBackgroundColor(Color.LTGRAY);
        //  mainLayout.setBackgroundColor(Color.parseColor("#55656C"));
        //configure pie chart
        mChart.setUsePercentValues(true);
        mChart.setDescription(plan_name+"   共执行"+counts+"次");
        //enable hole and configure
        mChart.setDrawHoleEnabled(true);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(5);
        mChart.setTransparentCircleRadius(8);

        // enable rotation of the chart by touch
        mChart.setRotation(0);
        mChart.setRotationEnabled(true);

        //set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //display message when value selected
                if (e == null)
                    return;

                Toast.makeText(PieCharActivity.this, xData[e.getXIndex()]+" = "+e.getVal()+"%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        // add Data
        addData();

        //customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(2);
    }

    private void addData(){
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i=0; i< yData.length; i++) {

            yVals1.add(new Entry(yData[i]*100/counts, i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i=0; i< xData.length; i++) {
            xVals.add(xData[i]);
        }
        //create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "总览");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c: ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(6f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        //undo all higlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }
}

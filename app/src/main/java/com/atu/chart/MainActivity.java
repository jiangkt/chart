package com.atu.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.atu.chart.entity.BarChartEntity;
import com.atu.chartlibrary.entity.CustomChartEntity;
import com.atu.chartlibrary.view.BarChartView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BarChartView barChartView;
    private List<CustomChartEntity> barCharList;
    private List<CustomChartEntity> barCharList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barChartView = findViewById(R.id.barChartView);

        initBarChar();

        addSpline();
    }

    private String[] types = {
            "外向", "追求", "自主", "亲密", "随性", "融入",
            "内向", "知足", "通融", "独立", "计划", "独处"
    };
    private static final int BAR_COUNT = 6;

    private void initBarChar() {
        barCharList = new ArrayList<>();
        barCharList1 = new ArrayList<>();

        for (int i = 0; i < BAR_COUNT; i++) {
            String[] barCharNames = new String[2];
            barCharNames[0] = types[i];
            barCharNames[1] = types[i + BAR_COUNT];

            BarChartEntity barChar = new BarChartEntity();
            barChar.setBarTypeNames(barCharNames);
            barChar.setScore(Math.random() * 100);

            Log.d("MainActivity",barChar.toString());

            barCharList.add(barChar);
        }

        for (int i = 0; i < BAR_COUNT; i++) {

            BarChartEntity barChar = new BarChartEntity();
            barChar.setScore(Math.random() * 100);

            Log.d("MainActivity",barChar.toString());

            barCharList1.add(barChar);
        }

        barChartView.setBarCheckable(true);
        barChartView.initBar(barCharList);
    }

    private void addSpline(){

        if (barChartView.getSpLineCount() == 0){
            barChartView.addSpLineList(barCharList, getResources().getColor(R.color.colorPrimary), false);
            barChartView.addSpLineList(barCharList1, getResources().getColor(R.color.colorPrimaryDark), false);
        }
    }

}

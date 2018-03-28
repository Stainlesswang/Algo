package edu.tjut.algo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import edu.tjut.algo.R;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
//        Utils.init(this);
        LineChart lineChart= (LineChart) findViewById(R.id.chart);
        List<Entry> entries=new ArrayList<Entry>();
        entries.add(0,new Entry(3f,2));
        entries.add(1,new Entry(5.0f,3));
        entries.add(2,new Entry(8.0f,4));
        LineDataSet dataSet=new LineDataSet(entries,"labe1");
        LineData lineData=new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}

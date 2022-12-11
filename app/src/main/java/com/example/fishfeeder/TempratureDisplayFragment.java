package com.example.fishfeeder;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fishfeeder.bluetooth.BluetoothService;
import com.example.fishfeeder.bluetooth.GetMessage;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
/**
Displays tempreture chart and current tempreture
 */
public class TempratureDisplayFragment extends Fragment
{
    LineChart chart;
    BluetoothService bluetoothService;
    private Handler handler;
    LineDataSet lineDataSet;
    TextView currentTempDisplay;
    private long delayPeriod = 1000; // in ms

    Runnable tempUpdate = new Runnable() {
        @Override
        public void run() {
            try {
                bluetoothService.sendMessage(new GetMessage(new String[]{"temp","temps"},(data)->{
                    try {
                        ArrayList<Entry> entries = new ArrayList<>();
                        JSONArray arr = data.getJSONArray("temps");
                        for(int i=0;i<arr.length();i++)
                            entries.add(new Entry(i+1,(float)arr.getDouble(i)));
                        setData(entries);
                        String str = getActivity().getResources().getText(R.string.current_tempreture) + " " + new DecimalFormat("0.0").format(data.getDouble("temp")) + " °C";
                        currentTempDisplay.setText(str);
                    } catch (JSONException e) {
                        Log.e("xxx",e.toString());
                    }
                }));
            } finally {
              handler.postDelayed(tempUpdate,delayPeriod);
            }
        }
    };

    public TempratureDisplayFragment() {
        super(R.layout.temprature_display_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chart = view.findViewById(R.id.tempChart);
        initChart();
        handler = new Handler();
        bluetoothService = ((MainActivity)getActivity()).getBluetoohService();
        currentTempDisplay = getActivity().findViewById(R.id.currentTempDisplay);
        tempUpdate.run();
    }

    private void initChart()
    {

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setEnabled(true);
        x.setDrawGridLines(false);
        x.setDrawAxisLine(true);
        x.setAxisLineColor(Color.WHITE);
        x.setAxisLineWidth(2f);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(15f);
        x.setTextColor(Color.WHITE);
        chart.setExtraBottomOffset(4f);

        YAxis ly = chart.getAxisLeft();
        ly.setLabelCount(5, true);
        ly.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        ly.setAxisLineColor(Color.WHITE);
        ly.setDrawGridLines(false);
        ly.setTextSize(15f);
        ly.setTextColor(Color.WHITE);
        ly.setAxisLineWidth(2f);
        ly.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return (int)value + "°C";
            }
        });

        YAxis ry = chart.getAxisRight();
        ry.setDrawGridLines(false);
        ry.setEnabled(true);
        ry.setAxisLineWidth(2f);
        ry.setDrawAxisLine(true);
        ry.setDrawLabels(false);
        ry.setAxisLineColor(Color.WHITE);
        chart.getLegend().setEnabled(false);

        //todo chart.getAxisLeft().setAxisMinValue(YOUR_NUMBER); //resize to content
        chart.animateY(1000, Easing.EaseOutCubic);
        // don't forget to refresh the drawing
        chart.invalidate();
    }


    private void setData(ArrayList<Entry> values)
    {
        lineDataSet.setValues(values);
        LineData data = new LineData(lineDataSet);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
        chart.setData(data);
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        float maxY = 0;
        for(Entry e : values)
            maxY = Math.max(maxY,e.getY());
        chart.getAxisLeft().setAxisMaximum(maxY + 4);
        chart.invalidate();
    }

}

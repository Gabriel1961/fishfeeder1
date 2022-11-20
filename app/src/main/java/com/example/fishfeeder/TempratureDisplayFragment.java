package com.example.fishfeeder;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavType;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TempratureDisplayFragment extends Fragment
{
    LineChart chart;
    public TempratureDisplayFragment() {
        super(R.layout.temprature_display_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chart = view.findViewById(R.id.tempChart);
        initChart();
        setData(10,10);
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
        ly.setLabelCount(5, false);
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


    private void setData(int count, float range)
    {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * (range + 1)) + 20;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(3.0f);
            set1.setCircleRadius(6f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.white_transparent));
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }
}

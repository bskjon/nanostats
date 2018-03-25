package com.iktdev.nanostat.charts;
import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.*;
import com.iktdev.nanostat.R;
import com.iktdev.nanostat.classes.ChartData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Brage on 20.02.2018.
 */

public class ChartAxisValueFormatter implements IAxisValueFormatter {

    /*public ChartAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        this.mDate = new Date();
    }*/

    protected String[] months;
    private Map<Integer, DataValues> currentValues = new TreeMap<>();
    private BarLineChartBase<?> chart;
    public ChartAxisValueFormatter(BarLineChartBase<?> chart, Context context, Map<Long, ChartData> values)
    {
        this.chart = chart;
        months = new String[]{
                context.getString(R.string.month1SHORT),
                context.getString(R.string.month2SHORT),
                context.getString(R.string.month3SHORT),
                context.getString(R.string.month4SHORT),
                context.getString(R.string.month5SHORT),
                context.getString(R.string.month6SHORT),
                context.getString(R.string.month7SHORT),
                context.getString(R.string.month8SHORT),
                context.getString(R.string.month9SHORT),
                context.getString(R.string.month10SHORT),
                context.getString(R.string.month11SHORT),
                context.getString(R.string.month12SHORT)
        };

        Calendar cal = Calendar.getInstance();
        int i = 0;
        for (ChartData chartData : values.values())
        {
            cal.setTime(new Date(chartData.date*1000));
            DataValues dv = new DataValues(
                    cal.get(Calendar.HOUR),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.YEAR)
            );
            currentValues.put(i, dv);
            i+= 1;
        }


        /*for (int i = 0; i < values.size(); i++)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(values.get(i).date*1000));
            DataValues dv = new DataValues(
                    cal.get(Calendar.HOUR),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.YEAR)
            );
            currentValues.put(i, dv);
        }*/

    }


    @Override

    public String getFormattedValue(float value, AxisBase axis)
    {

        int key = (int)value;


        DataValues dv = currentValues.get(key);
        if (dv != null)
        {
            Log.e("YEAR - CHART", "dv is not null and value of year is: " + String.valueOf(dv.Year));

        }
        else
            return "";

        int year = currentValues.get(key).Year; // determineYear(days);
        int month = currentValues.get(key).Month; // determineMonth(days);

        String monthName = months[month % months.length];
        String yearName = String.valueOf(year);


        Log.e("VisibleRange", String.valueOf(chart.getVisibleXRange()));

        if (chart.getVisibleXRange() > 30 * 6)
        {

            return monthName + " " + yearName;
        }
        else if (chart.getVisibleXRange() > 30 * 2)
        {

            return currentValues.get(key).Day + "." + months[currentValues.get(key).Month];
        }
        else
        {
            return currentValues.get(key).Hour + ":" + currentValues.get(key).Minute;
        }

    }

    public class DataValues
    {
        public int Hour;
        public int Minute;
        public int Day;
        public int Month;
        public int Year;

        public DataValues(int Hour, int Minute, int Day, int Month, int Year)
        {
            this.Hour = Hour;
            this.Minute = Minute;
            this.Day = Day;
            this.Month = Month;
            this.Year = Year;
        }

    }



}

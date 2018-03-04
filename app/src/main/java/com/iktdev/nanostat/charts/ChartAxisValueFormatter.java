package com.iktdev.nanostat.charts;
import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.*;
import com.iktdev.nanostat.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Brage on 20.02.2018.
 */

public class ChartAxisValueFormatter implements IAxisValueFormatter {
    private long referenceTimestamp; // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;

    /*public ChartAxisValueFormatter(long referenceTimestamp) {
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        this.mDate = new Date();
    }*/

    protected String[] months;
    private Map<Integer, DataValues> currentValues = new HashMap<>();
    private BarLineChartBase<?> chart;
    public ChartAxisValueFormatter(BarLineChartBase<?> chart, Context context, Map<Integer, Long> values)
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
        for (int i = 0; i < values.size(); i++)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(values.get(i).longValue()*1000));
            DataValues dv = new DataValues(
                    cal.get(Calendar.HOUR),
                    cal.get(Calendar.MINUTE),
                    cal.get(Calendar.DAY_OF_WEEK),
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.YEAR)
            );
            currentValues.put(i, dv);
        }

    }



    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java
     */

    @Override

    public String getFormattedValue(float value, AxisBase axis) {
        /*long days = (long)value*1000;

        Date date = new Date(days);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);*/

        int key = (int)value;



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
            int dayOfMonth = currentValues.get(key).DayOfMonth;
            String appendix = "th";

            switch (dayOfMonth) {
                case 1:
                    appendix = "st";
                    break;
                case 2:
                    appendix = "nd";
                    break;
                case 3:
                    appendix = "rd";
                    break;
                case 21:
                    appendix = "st";
                    break;
                case 22:
                    appendix = "nd";
                    break;
                case 23:
                    appendix = "rd";
                    break;
                case 31:
                    appendix = "st";
                    break;
            }

            return dayOfMonth == 0 ? "" : dayOfMonth + appendix + " " + monthName;
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
        public int DayOfMonth;
        public int Month;
        public int Year;

        public DataValues(int Hour, int Minute, int Day, int DayOfMonth, int Month, int Year)
        {
            this.Hour = Hour;
            this.Minute = Minute;
            this.Day = Day;
            this.DayOfMonth = DayOfMonth;
            this.Month = Month;
            this.Year = Year;
        }

    }



}

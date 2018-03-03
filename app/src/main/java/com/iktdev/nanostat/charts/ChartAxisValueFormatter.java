package com.iktdev.nanostat.charts;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java
     */

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
        long convertedTimestamp = (long) value;

        // Retrieve original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        // Convert timestamp to hour:minute
        return (mDataFormat.format(convertedTimestamp));
    }

    private String getHour(long timestamp) {
        try {
            mDate.setTime(timestamp * 1000);
            return mDataFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

}

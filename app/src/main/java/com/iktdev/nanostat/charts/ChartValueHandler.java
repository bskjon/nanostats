package com.iktdev.nanostat.charts;

import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.iktdev.nanostat.classes.ChartData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brage on 23.02.2018.
 */

public class ChartValueHandler
{

    public ArrayList<ChartData> getPastDay(ArrayList<ChartData> items)
    {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        ArrayList<ChartData> data = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        date = cal.getTime();

        Log.d("CHART DEBUG:", "############################################################################################");

        for(ChartData item : items)
        {

            Date itemDate = new Date(item.date*1000L);
            if (itemDate.after(cal.getTime()))
            {
                Log.d("CHART DEBUG", df.format(itemDate) + ":: " +df.format(date) + " (PastDay)");
                data.add(item);
            }


        }


        return data;
    }






}

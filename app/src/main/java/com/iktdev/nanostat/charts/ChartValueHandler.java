package com.iktdev.nanostat.charts;

import com.github.mikephil.charting.charts.Chart;
import com.iktdev.nanostat.classes.ChartData;

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
        ArrayList<ChartData> data = new ArrayList<>();
        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        for(ChartData item : items)
        {
            Date itemDate = new Date(item.date*1000L);
            if (itemDate.after(cal.getTime()))
            {
                data.add(item);
            }


        }


        return data;
    }






}

package com.iktdev.nanostat.classes;

/**
 * Created by Brage on 23.02.2018.
 */

public class ChartData
{
    public long date;
    public int shares;
    public double hashrate;

    public ChartData(long date, int shares, double hashrate)
    {
        this.date = date*1000;
        this.shares = shares;
        this.hashrate = hashrate;
    }
}

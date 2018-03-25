package com.iktdev.nanostat.classes;

/**
 * Created by Brage on 08.03.2018.
 */

public class Prices
{
    public double USD;
    public double EUR;
    public double RUR;
    public double CNY;
    public double BTC;

    public Prices() {}
    public Prices(double USD, double EUR, double RUR, double CNY, double BTC)
    {
        this.USD = USD;
        this.EUR = EUR;
        this.RUR = RUR;
        this.CNY = CNY;
        this.BTC = BTC;
    }


}

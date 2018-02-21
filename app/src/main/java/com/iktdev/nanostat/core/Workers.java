package com.iktdev.nanostat.core;

import com.iktdev.nanostat.R;

import java.util.Date;

/**
 * Created by Brage on 20.02.2018.
 */

public class Workers
{
    public String id;
    public double hashrate;
    public Date lastshare;
    public double rating;
    public double h1;
    public double h3;
    public double h6;
    public double h12;
    public double h24;
    public String rateType;


    public Workers()
    {
    }

    public void setRateType()
    {
        switch (currentCryptoValues.WalletType)
        {
            case R.string.eth_address:
                rateType = "Mh/s";
                break;
            case R.string.etc_address:
                rateType = "Mh/s";
                break;
            case R.string.sia_address:
                rateType = "Mh/s";
                break;
            case R.string.xmr_address:
                rateType = "H/s";
                break;
            case R.string.pasc_address:
                rateType = "Mh/s";
                break;
            case R.string.etn_address:
                rateType = "H/s";
                break;
            case R.string.zec_address:
                rateType = "Sol/s";
                break;
        }
    }

    public Workers(String id, double hashrate, Date lastshare, double rating)
    {
        this.id = id;
        this.hashrate = hashrate;
        this.lastshare = lastshare;
        this.rating = rating;
    }


    public void sethashrate(double h1, double h3, double h6, double h12, double h24)
    {
        this.h1 = h1;
        this.h3 = h3;
        this.h6 = h6;
        this.h12 = h12;
        this.h24 = h24;
    }


}

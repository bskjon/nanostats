package com.iktdev.nanostat.classes;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brage on 22.02.2018.
 */

public class overview
{
    public int WalletId;
    public int WalletImageId;
    public String WalletShortText;
    public int WalletColorId;

    public String _prefix;
    public String _address;

    public double Balance;
    public double PayoutLimit;
    public ArrayList<ChartData> chartData;

    public overview() {}

    public overview(int WalletId, double Balance, double PayoutLimit)
    {
        this.WalletId = WalletId;
        this.Balance  = Balance;
        this.PayoutLimit = PayoutLimit;
    }

}

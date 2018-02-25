package com.iktdev.nanostat.core;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brage on 20.02.2018.
 */

public class currentCryptoValues
{
    public static int WalletType;

    private double PayoutLimit;
    private Date PayoutLimit_changed = null;

    public void setPayoutLimit(double Limit)
    {
        this.PayoutLimit = Limit;
        PayoutLimit_changed = Calendar.getInstance().getTime();
    }
    public double getPayoutLimit()
    {
        return this.PayoutLimit;
    }
    public Date getPayoutLimit_changed() { return this.PayoutLimit_changed; }



    private double Balance;
    private Date Balance_changed = null;

    public void setBalance(double balance)
    {
        this.Balance = balance;
        Balance_changed = Calendar.getInstance().getTime();
    }
    public double getBalance() { return this.Balance;}
    public Date getBalance_changed() { return this.Balance_changed; }

    public void reset()
    {
        Balance_changed = null;
        PayoutLimit_changed = null;
        Balance = 0.0d;
        PayoutLimit = 0.0d;
    }

}

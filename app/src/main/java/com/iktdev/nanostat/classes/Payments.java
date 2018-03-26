package com.iktdev.nanostat.classes;

/**
 * Created by Brage on 26.03.2018.
 */

public class Payments
{
    public int cryptoCurrency;
    public String CryptoShort;
    public long date;
    public String txHash;
    public double amount;
    public boolean confirmed;

    public Payments() {}
    public Payments(long date, String txHash, double amount, boolean confirmed)
    {
        this.date = date;
        this.txHash = txHash;
        this.amount = amount;
        this.confirmed = confirmed;
    }


}

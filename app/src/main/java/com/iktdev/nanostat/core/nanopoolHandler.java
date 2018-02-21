package com.iktdev.nanostat.core;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.iktdev.nanostat.adapters.workerAdpater;
import com.iktdev.nanostat.charts.ChartAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Brage on 18.02.2018.
 */

public class nanopoolHandler
{
    public static String Zec_main = "https://api.nanopool.org/v1/zec/";
    public static String Eth_main = "https://api.nanopool.org/v1/eth/";


    public String balance(String prefix, String Address)
    {
        return prefix + "balance/" + Address;
    }
    public String accountexist(String prefix, String Address) { return  prefix + "accountexist/" + Address; }
    public String general(String prefix, String Address) { return prefix + "user/" + Address; }
    public String hashratechart(String prefix, String Address) {return  prefix + "hashratechart/" + Address; }
    public String payoutlimit(String prefix, String Address) { return  prefix + "usersettings/" + Address; }

    public DecimalFormat decimalFormat = new DecimalFormat("#0.00000");
    public boolean getBalance(final Activity context, final TextView balanceTextView, String url, final currentCryptoValues cryptoValues)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowd = httpHandler.setUrl(url);
        if(urlAllowd)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run()
                {
                    String res = httpHandler.getApiResponse(httpHandler.getUrl());
                    String current_balance = getJSONField(res, "data");


                    final double value = Double.valueOf(current_balance);
                    final String thisBalance = decimalFormat.format(value);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            balanceTextView.setText(String.valueOf(thisBalance));
                            cryptoValues.setBalance(value);
                        }
                    });
                }
            });
            return true;
        }
        return false;
    }
    public boolean getGeneral(final Activity context, String url, final currentCryptoValues cryptoValues, final TextView balance, final TextView unconfirmed_balance, final TextView hashrate, final RecyclerView recyclerView)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);
        if (urlAllowed)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run()
                {
                    final String apiRes = httpHandler.getApiResponse(httpHandler.getUrl());
                    final generalInfo gi = new generalInfo();
                    gi.setDataFromJsonString(apiRes);

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cryptoValues.setBalance(gi._balance);
                            balance.setText(gi.balance);
                            unconfirmed_balance.setText("+ " + gi.unconfirmed_balance);
                            hashrate.setText(String.valueOf(gi.hashrate));

                            recyclerView.setHasFixedSize(true);
                            com.iktdev.nanostat.adapters.workerAdpater adapter = new workerAdpater(context, gi.workersList);
                            recyclerView.setAdapter(adapter);
                            //recyclerView

                        }
                    });
                }
            });
            return true;
        }
        return false;
    }
    public boolean getChartData(final Activity context, final String url, final LineChart chart)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);
        if (urlAllowed)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run()
                {
                    String apiRes = httpHandler.getApiResponse(httpHandler.getUrl());
                    try {
                        JSONObject main = new JSONObject(apiRes);
                        Boolean allowed = main.getBoolean("status");
                        if (allowed)
                        {
                            ArrayList<ChartData> chartData = new ArrayList<>();
                            JSONArray ja = main.getJSONArray("data");
                            for (int i = 0; i < ja.length(); i++)
                            {
                                JSONObject jo = ja.getJSONObject(i);
                                ChartData cd = new ChartData(
                                        //new Date(jo.getInt("date")),
                                        //BigDecimal.valueOf(jo.getDouble("date")).floatValue(),
                                        jo.getLong("date"),
                                        jo.getInt("shares"),
                                        jo.getDouble("hashrate")
                                );
                                chartData.add(cd);
                            }

                            if (chartData .size() > 0)
                            {
                                final long referenceTimeStamp = (chartData.get(0).date);
                                List<Entry> entries = new ArrayList<>();
                                for (ChartData data : chartData)
                                {
                                    //entries.add(new Entry((long)data.date, data.shares));
                                    entries.add(new Entry(chartData.indexOf(data), data.shares));
                                }
                                LineDataSet dataset = new LineDataSet(entries, "Shares");
                                dataset.setDrawValues(false);
                                dataset.setDrawCircleHole(false);
                                dataset.setDrawCircles(false);


                                dataset.setDrawFilled(true);
                                dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                                final LineData lineData = new LineData(dataset);

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        chart.setData(lineData);
                                        chart.getDescription().setEnabled(false);
                                        XAxis xaxis = chart.getXAxis();
                                        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                        chart.getAxisRight().setEnabled(false);
                                        chart.setDrawMarkers(false);
                                        chart.getAxisLeft().setDrawGridLines(false);
                                        chart.getXAxis().setDrawGridLines(false);
                                        chart.getLegend().setEnabled(false);
                                        chart.getData().setHighlightEnabled(false);
                                        /*ChartAxisValueFormatter cavf = new ChartAxisValueFormatter(referenceTimeStamp);
                                        XAxis xAxis = chart.getXAxis();
                                        xAxis.setValueFormatter(cavf);*/


                                        chart.invalidate();
                                    }
                                });


                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
            return true;
        }
        return false;
    }
    public boolean getPayoutLimit(final Activity context , final String url, final currentCryptoValues cryptoValues, final TextView payoutText)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);
        if (urlAllowed)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    String res = httpHandler.getApiResponse(httpHandler.getUrl());
                    try {
                        JSONObject main = new JSONObject(res);
                        JSONObject data = main.getJSONObject("data");
                        final double payoutLimit = data.getDouble("payout");
                        if (payoutLimit != 0.0)
                        {
                            cryptoValues.setPayoutLimit(payoutLimit);
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    payoutText.setText(String.valueOf(payoutLimit));
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        return false;
    }


    public String getJSONField(String jsonStrng, String targetKey)
    {
        try {
            JSONObject jo = new JSONObject(jsonStrng);
            String val = jo.getString(targetKey);
            if (val != null || val.length() != 0)
            {
                return val;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public class generalInfo
    {
        public boolean status;
        public double _balance;
        public String balance;
        public String unconfirmed_balance;
        public double hashrate;

        public double h1;
        public double h3;
        public double h6;
        public double h12;
        public double h24;

        public ArrayList<Workers> workersList;

        public generalInfo() {}
        public generalInfo(String balance, String unconfirmed_balance, double hashrate)
        {
            this.balance = balance;
            this.unconfirmed_balance = unconfirmed_balance;
            this.hashrate = hashrate;
        }
        public void sethashrate(double h1, double h3, double h6, double h12, double h24)
        {
            this.h1 = h1;
            this.h3 = h3;
            this.h6 = h6;
            this.h12 = h12;
            this.h24 = h24;
        }

        public void setDataFromJsonString(String jsonString)
        {
            try
            {
                JSONObject main = new JSONObject(jsonString);
                status = Boolean.parseBoolean(main.getString("status"));

                //JSONObject data = (main.getJSONArray("data").getJSONObject(0));
                JSONObject data = main.getJSONObject("data");
                this.balance = decimalFormat.format(Double.valueOf(data.getString("balance")));
                this._balance = data.getDouble("balance");
                this.unconfirmed_balance = data.getString("unconfirmed_balance");
                this.hashrate = data.getDouble("hashrate");


                JSONObject avgHashrate = data.getJSONObject("avgHashrate");
                this.h1 = avgHashrate.getDouble("h1");
                this.h3 = avgHashrate.getDouble("h3");
                this.h6 = avgHashrate.getDouble("h6");
                this.h12 = avgHashrate.getDouble("h12");
                this.h24 = avgHashrate.getDouble("h24");




                workersList = new ArrayList<>();
                JSONArray workers = data.getJSONArray("workers");
                for (int i = 0; i < workers.length(); i++)
                {
                    JSONObject wrok = workers.getJSONObject(i);
                    long unixTimeStamp = wrok.getLong("lastshare");


                    Workers w = new Workers(
                            wrok.getString("id"),
                            wrok.getDouble("hashrate"),
                            new Date((unixTimeStamp*1000L)),
                            wrok.getDouble("rating")
                    );
                    w.setRateType();

                    w.h1 = avgHashrate.getDouble("h1");
                    w.h3 = avgHashrate.getDouble("h3");
                    w.h6 = avgHashrate.getDouble("h6");
                    w.h12 = avgHashrate.getDouble("h12");
                    w.h24 = avgHashrate.getDouble("h24");
                    workersList.add(w);
                }






            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }






    }

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

}

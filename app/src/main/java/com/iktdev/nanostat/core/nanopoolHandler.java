package com.iktdev.nanostat.core;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.iktdev.nanostat.R;
import com.iktdev.nanostat.adapters.overviewAdapter;
import com.iktdev.nanostat.adapters.workerAdpater;
import com.iktdev.nanostat.charts.ChartAxisValueFormatter;
import com.iktdev.nanostat.charts.ChartValueHandler;
import com.iktdev.nanostat.classes.ChartData;
import com.iktdev.nanostat.classes.overview;
import com.iktdev.nanostat.classes.Prices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Brage on 18.02.2018.
 */

public class nanopoolHandler
{
    public static String Zec_main = "https://api.nanopool.org/v1/zec/";
    public static String Eth_main = "https://api.nanopool.org/v1/eth/";
    public static String Etc_main = "https://api.nanopool.org/v1/etc/";
    public static String Sia_main = "https://api.nanopool.org/v1/sia/";
    public static String Xmr_main = "https://api.nanopool.org/v1/xmr/";
    public static String Pasc_main = "https://api.nanopool.org/v1/pasc/";
    public static String Etn_main = "https://api.nanopool.org/v1/etn/";


    public String balance(String prefix, String Address)
    {
        return prefix + "balance/" + Address;
    }
    public String accountexist(String prefix, String Address) { return  prefix + "accountexist/" + Address; }
    public String general(String prefix, String Address) { return prefix + "user/" + Address; }
    public String hashratechart(String prefix, String Address) {return  prefix + "hashratechart/" + Address; }
    public String payoutlimit(String prefix, String Address) { return  prefix + "usersettings/" + Address; }
    public String prices(String prefix) { return prefix + "prices"; }

    public DecimalFormat decimalFormat = new DecimalFormat("#0.00000");

    public double getBalance(String url)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowd = httpHandler.setUrl(url);
        if(urlAllowd)
        {
            String current_balance = "0.0";
            String res = httpHandler.getApiResponse(httpHandler.getUrl());
            if (res != null && res.length() > 0)
            {
                current_balance = getJSONField(res, "data");
            }

            if (current_balance != null)
            {
                return Double.valueOf(current_balance);
            }
            else
                return 0.0;

            //return decimalFormat.format(value);
        }
        return -1;
    }
    public generalInfo getGeneral(String url)
    {
        HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);
        if (urlAllowed)
        {
            String apiRes = httpHandler.getApiResponse(httpHandler.getUrl());
            generalInfo gi = new generalInfo();
            gi.setDataFromJsonString(apiRes);
            return gi;
        }
        return null;
    }
    public ArrayList<ChartData> getChartData(final String url)
    {

        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);
        if (urlAllowed)
        {
            String apiRes = httpHandler.getApiResponse(httpHandler.getUrl());
            try
            {
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
                    Collections.reverse(chartData); //Nanopool echoes out in an order that conflicts with how the data is read...
                    return chartData;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }
    public double getPayoutLimit(String url)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);

        if (urlAllowed)
        {
            String res = httpHandler.getApiResponse(httpHandler.getUrl());
            try {
                JSONObject main = new JSONObject(res);
                JSONObject data = main.getJSONObject("data");
                final double payoutLimit = data.getDouble("payout");
                if (payoutLimit != 0.0)
                {
                    return payoutLimit;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    public Prices getPrices(String url)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowed = httpHandler.setUrl(url);
        if (urlAllowed)
        {
            String res = httpHandler.getApiResponse(httpHandler.getUrl());
            try
            {
                JSONObject data = new JSONObject(res).getJSONObject("data");
                Prices prices = new Prices(
                        data.getDouble("price_usd"),
                        data.getDouble("price_eur"),
                        data.getDouble("price_rur"),
                        data.getDouble("price_cny"),
                        data.getDouble("price_btc")
                );
                return prices;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    public void applyGeneralInfo(final Activity context, final String url, final currentCryptoValues cryptoValues, final TextView balance, final TextView unconfirmed_balance, final TextView hashrate, final RecyclerView recyclerView)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final generalInfo gi = getGeneral(url);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (gi != null)
                        {
                            cryptoValues.setBalance(gi._balance);
                            balance.setText(gi.balance);
                            unconfirmed_balance.setText("+ " + gi.unconfirmed_balance);
                            hashrate.setText(String.valueOf(gi.hashrate));
                            recyclerView.setHasFixedSize(true);
                            ArrayList<Workers> WorkerList = gi.workersList;
                            if (WorkerList != null && WorkerList.size() > 0)
                            {
                                workerAdpater adapter = new workerAdpater(context, gi.workersList);
                                recyclerView.setAdapter(adapter);
                            }
                            else
                            {
                                Toast.makeText(context, "Failed to retrieve chart data..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {

                            balance.setText("Unexpected error, value -2");
                            unconfirmed_balance.setText("Unexpected error, value -2");
                            hashrate.setText("Unexpected error, value -2");
                        }
                        //recyclerView

                    }
                });
            }
        });
    }

    public void applyChartData(final Activity context, final String url, final LineChart chart, final int ColorId)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ChartData> chartData = getChartData(url);
                final Map<Long, ChartData> values = new TreeMap<>();
                for (ChartData cd : chartData)
                {
                    values.put(cd.date, cd);
                }

                final List<Entry> entries = new ArrayList<>();

                if (values != null && values.size() > 0)
                {
                    int i = 0;
                    for (ChartData data : values.values())
                    {
                        //entries.add(new Entry((long)data.date, data.shares));
                        //entries.add(new Entry(chartData.indexOf(data), data.shares));
                        entries.add(new Entry(i, data.shares));
                        //values.put(chartData.indexOf(data), data.date);
                        i+=1;
                    }


                    LineDataSet dataset = new LineDataSet(entries, "Shares");
                    dataset.setDrawValues(false);
                    dataset.setDrawCircleHole(false);
                    dataset.setDrawCircles(false);



                    dataset.setDrawFilled(true);
                    //dataset.setFillColor(R.color.testColor);
                    dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    helper h = new helper();
                    dataset.setColor(h.getNonThemedColor(context, ColorId));
                    dataset.setFillColor(h.getNonThemedColor(context, ColorId));

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
                            chart.setScaleYEnabled(false);


                                        ChartAxisValueFormatter cavf = new ChartAxisValueFormatter(chart, context, values);
                                        XAxis xAxis = chart.getXAxis();
                                        xAxis.setGranularity(1f);
                                        xAxis.setValueFormatter(cavf);


                            chart.invalidate();
                        }
                    });
                }



            }
        });

    }

    public void applyPayoutLimit(final Activity context , final String url, final currentCryptoValues cryptoValues, final TextView payoutText)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final double response = getPayoutLimit(url);
                cryptoValues.setPayoutLimit(response);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if (response != -1)
                        {
                            payoutText.setText(String.valueOf(response));
                            cryptoValues.setPayoutLimit(response);
                        }
                        else
                            payoutText.setText("Unexpected error, value -1");


                    }
                });
            }
        });
    }

    public void applyBalance(final Activity context, final TextView balanceTextView, final String url, final currentCryptoValues cryptoValues)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {
                final double response = getBalance(url);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (response != -1)
                        {
                            balanceTextView.setText(decimalFormat.format(response));
                            cryptoValues.setBalance(response);
                        }
                        else
                            balanceTextView.setText("Unexpected error, value -1");

                        
                    }
                });

            }
        });
    }


    public overviewAdapter _overviewAdapter;
    public void applyOverview(final Activity context, final ArrayList<overview> ovs, final RecyclerView rv, final SwipeRefreshLayout srl)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _overviewAdapter = (overviewAdapter)rv.getAdapter();
                        if (_overviewAdapter != null)
                        {
                            rv.setAdapter(new overviewAdapter(context, ovs));
                            _overviewAdapter = (overviewAdapter)rv.getAdapter();
                        }


                        for (int i = 0; i < ovs.size(); i++)
                        {
                            final overview nov = ovs.get(i);
                            final int finalId = i;
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    final int finalI = finalId;
                                    final double _balance = getBalance(balance(ovs.get(finalI)._prefix, ovs.get(finalI)._address));
                                    final double _payoutLimit = getPayoutLimit(payoutlimit(ovs.get(finalI)._prefix, ovs.get(finalI)._address));
                                    final ArrayList<ChartData> chartData = getChartData(hashratechart(ovs.get(finalI)._prefix, ovs.get(finalI)._address));

                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            nov.Balance = _balance;
                                            nov.PayoutLimit = _payoutLimit;
                                            if (chartData != null && chartData.size() > 0)
                                            {
                                                ChartValueHandler cvh = new ChartValueHandler();
                                                ArrayList<ChartData> pastDayChart = cvh.getPastDay(chartData);
                                                nov.chartData = pastDayChart;
                                            }
                                            _overviewAdapter.updateItem(nov, finalI);
                                        }
                                    });
                                }
                            });

                        }

                        if (srl != null){
                            srl.setRefreshing(false);

                        }

                    }
                });


                /*context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overviewAdapter adapter = (overviewAdapter) rv.getAdapter();
                        if (adapter != null)
                        {
                            adapter.replaceItems(ov);
                        }
                        else
                        {
                            adapter = new overviewAdapter(context, ov);
                            rv.setAdapter(adapter);
                        }

                    }
                });*/

            }
        });
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

}

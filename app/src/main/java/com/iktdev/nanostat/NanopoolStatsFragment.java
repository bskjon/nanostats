package com.iktdev.nanostat;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.iktdev.nanostat.CustomViews.CircleProgressBar;
import com.iktdev.nanostat.core.HttpHandler;
import com.iktdev.nanostat.core.currentCryptoValues;
import com.iktdev.nanostat.core.nanopoolHandler;

import org.w3c.dom.Text;

import java.util.Set;

public class NanopoolStatsFragment extends Fragment {

    private int crypto  = -1;
    private String wallet = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        wallet = getArguments().getString("address");
        crypto = getArguments().getInt("id");

        return inflater.inflate(R.layout.fragment_nanopool_stats, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        //getView().setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView)getView().findViewById(R.id.fragment_stats_workerView)).setLayoutManager(linearLayoutManager);

        fetchData();

    }

    private HttpHandler httpHandler = null;
    private nanopoolHandler apiHandler = null;
    public currentCryptoValues ccv;
    public void fetchData()
    {
        if (apiHandler == null)
            apiHandler = new nanopoolHandler();

        ccv = new currentCryptoValues();
        currentCryptoValues.WalletType = crypto;
        switch (crypto)
        {
            case R.string.eth_address:
                SetDefaults(R.drawable.ic_etherum, "ETH", "Mh/s");
                CallApis(nanopoolHandler.Eth_main);
                setPayoutProgress();
                break;

            case R.string.etc_address:
                SetDefaults(R.drawable.ic_etherum_classic, "ETC", "Mh/s");
                CallApis(nanopoolHandler.Etc_main);
                setPayoutProgress();
                break;

            case R.string.sia_address:
                SetDefaults(R.drawable.ic_siacoin, "SIA", "Mh/s");
                CallApis(nanopoolHandler.Sia_main);
                setPayoutProgress();
                break;

            case R.string.zec_address:
                SetDefaults(R.drawable.ic_zcash, "ZEC", "Sol/s");
                CallApis(nanopoolHandler.Zec_main);
                setPayoutProgress();
                break;

            case R.string.xmr_address:
                SetDefaults(R.drawable.ic_monero, "XMR", "H/s");
                CallApis(nanopoolHandler.Xmr_main);
                setPayoutProgress();
                break;

            case R.string.pasc_address:
                SetDefaults(R.drawable.ic_pascal, "PASC", "Mh/s");
                CallApis(nanopoolHandler.Pasc_main);
                setPayoutProgress();
                break;

            case R.string.etn_address:
                SetDefaults(R.drawable.ic_electroneum,"ETN", "H/s");
                CallApis(nanopoolHandler.Etn_main);
                setPayoutProgress();
                break;

            default:
                //Alert
                ShowDialog("No Account found", "Could not retrieve or find any account stored in the app").show();
                break;
        }


        //Boolean setUrl = httpHandler.setUrl(nanopoolHandler.Zec.Zec_main, apiHandler.Zec. )
    }

    private void CallApis(String main)
    {
        apiHandler.getGeneral(getActivity(),
                apiHandler.general(main, wallet),
                ccv,
                (TextView)getView().findViewById(R.id.fragment_stats_balance),
                (TextView)getView().findViewById(R.id.fragment_stats_unconfirmed_balance),
                (TextView)getView().findViewById(R.id.fragment_stats_hashrate),
                (RecyclerView)getView().findViewById(R.id.fragment_stats_workerView)
        );
        apiHandler.getChartData(getActivity(),
                apiHandler.hashratechart(main, wallet),
                (LineChart)getView().findViewById(R.id.fragment_stats_chart)
        );
        apiHandler.getPayoutLimit(getActivity(),
                apiHandler.payoutlimit(main, wallet),
                ccv,
                (TextView)getView().findViewById(R.id.fragment_stats_payoutLimit)
        );
    }






    private void SetDefaults(int Resid, String currcencyShort, String hashrateFormat)
    {
        ((ImageView)getView().findViewById(R.id.crypto_ic)).setImageResource(Resid);
        ((TextView)getView().findViewById(R.id.fragment_stats_cryptoName)).setText(currcencyShort);
        ((TextView)getView().findViewById(R.id.fragment_stats_hashrate_format)).setText(hashrateFormat);
        ((TextView)getView().findViewById(R.id.fragment_stats_cryptoNameLimit)).setText(currcencyShort);
    }



    public void setPayoutProgress()
    {
        final TextView payoutProgressText = (TextView)getView().findViewById(R.id.fragment_stats_progressPercent);
        final CircleProgressBar payoutProgress = getView().findViewById(R.id.fragment_stats_payoutProgressBar);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while (ccv.getBalance_changed() == null && ccv.getPayoutLimit_changed() == null)
                {
                    Log.e("WAITING", "Waiting for Balance and Payoutlimit change!");
                    SystemClock.sleep(500);
                }

                final double progress = ((ccv.getBalance() / ccv.getPayoutLimit())*100);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Double.isInfinite(progress))
                        {
                            payoutProgress.setProgressWithAnimation((float)progress);
                            payoutProgressText.setText(String.valueOf(Math.round(progress))+ "%" );
                        }

                    }
                });


            }
        });
    }




    public String data;
    public String handleResponse(String url)
    {
        boolean urlAllows = httpHandler.setUrl(url);
        if (urlAllows)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    data = httpHandler.getApiResponse(httpHandler.getUrl());
                }
            });
            return data;
        }
        return "";
    }



    private String recieveData()
    {
        return  "";
    }

    public AlertDialog ShowDialog(String title, String message)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(title);
        b.setMessage(message);
        b.setNeutralButton("dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return b.create();
    }

}

package com.iktdev.nanostat;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iktdev.nanostat.core.HttpHandler;
import com.iktdev.nanostat.core.nanopoolHandler;

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

        fetchData();
    }

    private HttpHandler httpHandler = null;
    private nanopoolHandler apiHandler = null;
    public void fetchData()
    {
        if (apiHandler == null)
            apiHandler = new nanopoolHandler();

        switch (crypto)
        {
            case R.string.eth_address:

                break;
            case R.string.zec_address:
                SetDefaults(R.drawable.ic_zcash);

                //getBalance((TextView)getView().findViewById(R.id.fragment_stats_balance), apiHandler.balance(nanopoolHandler.Zec_main, wallet));
                /*apiHandler.setBalance((TextView)getView().findViewById(R.id.fragment_stats_cryptotype),
                        apiHandler.getJSONField(handleResponse(apiHandler.getBalance(nanopoolHandler.Zec_main, wallet)), "data")
                        );*/
                apiHandler.getBalance(getActivity(),
                        (TextView)getView().findViewById(R.id.fragment_stats_balance),
                        apiHandler.balance(nanopoolHandler.Zec_main, wallet)
                );
                break;
            default:
                //Alert
                ShowDialog("No Account found", "Could not retrieve or find any account stored in the app").show();
                break;
        }


        //Boolean setUrl = httpHandler.setUrl(nanopoolHandler.Zec.Zec_main, apiHandler.Zec. )
    }

    private void SetDefaults(int Resid)
    {
        ((ImageView)getView().findViewById(R.id.crypto_ic)).setImageResource(Resid);
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

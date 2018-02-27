package com.iktdev.nanostat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.iktdev.nanostat.adapters.donateAdapter;
import com.iktdev.nanostat.classes.account;
import com.iktdev.nanostat.core.SharedPreferencesHandler;

import java.util.ArrayList;

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        RecyclerView rv = (RecyclerView)findViewById(R.id.activity_donate_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DonateActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        initAdapter(rv);

        ((ImageButton)findViewById(R.id.activity_donate_paypal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/iktdev"));
                startActivity(browserIntent);
            }
        });

    }

    public void initAdapter(RecyclerView rv)
    {
        ArrayList<account> items = new ArrayList<>();
        int[] donateAddresses = {
                R.string.donate_crypto_BTC,
                R.string.donate_crypto_BCH,
                R.string.donate_crypto_ETH,
                R.string.donate_crypto_LTC,
                R.string.donate_crypto_ZEC,
                R.string.donate_crypto_XMR,
                R.string.donate_crypto_ETC
        };

        for (int i = 0; i < donateAddresses.length; i++)
        {
            account acc = getAccountData(donateAddresses[i]);
            if (acc != null)
            {
                items.add(acc);
            }
        }

        rv.setHasFixedSize(false);
        donateAdapter adapter = new donateAdapter(this, items);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(adapter);





    }

    public account getAccountData(int id)
    {
        account ac = new account();
        String address = this.getString(id);
        if (address != null && address.length() > 0)
        {
            ac.WalletRid = id;
            switch (id)
            {
                case R.string.donate_crypto_ZEC:
                    ac.ReadableWalletType = "ZCash";
                    ac.WalletImageId = R.drawable.ic_zcash;
                    ac.WalletColorId = R.color.ZECcolor;
                    ac.Address = address;
                    break;

                case R.string.donate_crypto_ETH:
                    ac.ReadableWalletType = "Etherum";
                    ac.WalletImageId = R.drawable.ic_etherum;
                    ac.WalletColorId = R.color.ETH_background;
                    ac.Address = address;
                    break;

                case R.string.donate_crypto_ETC:
                    ac.ReadableWalletType = "Etherum Classic";
                    ac.WalletImageId = R.drawable.ic_etherum_classic;
                    ac.WalletColorId = R.color.ETC_background;
                    ac.Address = address;
                    break;

                case R.string.donate_crypto_XMR:
                    ac.ReadableWalletType = "Monero";
                    ac.WalletImageId = R.drawable.ic_monero;
                    ac.WalletColorId = R.color.XMR_background;
                    ac.Address = address;
                    break;

                case R.string.donate_crypto_BCH:
                    ac.ReadableWalletType = "Bitcoin cash";
                    ac.WalletImageId = R.drawable.ic_bitcoin_cash;
                    ac.WalletColorId = R.color.BCH_background;
                    ac.Address = address;
                    break;

                case R.string.donate_crypto_BTC:
                    ac.ReadableWalletType = "Bitcoin";
                    ac.WalletImageId = R.drawable.ic_bitcoin;
                    ac.WalletColorId = R.color.BTC_background;
                    ac.Address = address;
                    break;

                case R.string.donate_crypto_LTC:
                    ac.ReadableWalletType = "Litecoin";
                    ac.WalletImageId = R.drawable.ic_litecoin;
                    ac.WalletColorId = R.color.LTC_background;
                    ac.Address = address;
                    break;



                default:
                    return null;
            }
            return ac;
        }
        return null;
    }

}

package com.iktdev.nanostat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.iktdev.nanostat.adapters.accountAdapter;
import com.iktdev.nanostat.classes.account;
import com.iktdev.nanostat.core.HttpHandler;
import com.iktdev.nanostat.core.SharedPreferencesHandler;
import com.iktdev.nanostat.core.nanopoolHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private boolean Address_Passed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent it = getIntent();
        Bundle b = it.getExtras();
        if (b != null)
        {
            int address = b.getInt("address");
            if (address != 0)
            {
                Address_Passed = true;
                showInputDialog(address, null);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView)findViewById(R.id.activity_account_recyclerView)).setLayoutManager(linearLayoutManager);


        LoadAccounts();
    }

    public void LoadAccounts()
    {
        ArrayList<account> items = new ArrayList<>();
        int[] wallets = {
                R.string.zec_address,
                R.string.eth_address,
                R.string.etc_address,
                R.string.sia_address,
                R.string.xmr_address,
                R.string.pasc_address,
                R.string.etn_address
        };

        SharedPreferencesHandler sph = new SharedPreferencesHandler();
        for (int i = 0; i < wallets.length; i++)
        {
            account acc = getAccountData(wallets[i], sph);
            if (acc != null)
            {
                items.add(acc);
            }
        }

        RecyclerView rv = findViewById(R.id.activity_account_recyclerView);
        rv.setHasFixedSize(false);
        accountAdapter adapter = new accountAdapter(this, items);
        rv.setAdapter(adapter);




    }

    public account getAccountData(int id, SharedPreferencesHandler sph)
    {
        account ac = new account();
        String address = sph.getString(this, id);
        if (address != null && address.length() > 0)
        {
            ac.WalletRid = id;
            switch (id)
            {
                case R.string.zec_address:
                    ac.ReadableWalletType = "ZCash";
                    ac.WalletImageId = R.drawable.ic_zcash;
                    ac.Address = address;
                    break;

                case R.string.eth_address:
                    ac.ReadableWalletType = "Etherum";
                    ac.WalletImageId = R.drawable.ic_etherum;
                    ac.Address = address;
                    break;

                case R.string.etc_address:
                    ac.ReadableWalletType = "Etherum Classic";
                    ac.WalletImageId = R.drawable.ic_etherum_classic;
                    ac.Address = address;
                    break;

                case R.string.sia_address:
                    ac.ReadableWalletType = "SiaCoin";
                    ac.WalletImageId = R.drawable.ic_siacoin;
                    ac.Address = address;
                    break;

                case R.string.xmr_address:
                    ac.ReadableWalletType = "Monero";
                    ac.WalletImageId = R.drawable.ic_monero;
                    ac.Address = address;
                    break;

                case R.string.pasc_address:
                    ac.ReadableWalletType = "Pascal";
                    ac.WalletImageId = R.drawable.ic_pascal;
                    ac.Address = address;
                    break;

                case R.string.etn_address:
                    ac.ReadableWalletType = "Electroneum";
                    ac.WalletImageId = R.drawable.ic_electroneum;
                    ac.Address = address;
                    break;

                default:
                    return null;
            }
            return ac;
        }
        return null;
    }







    private String prefix = "";
    private boolean Accountcheck_status = false;
    public void showInputDialog(final int addressType, String failedInput)
    {

        switch (addressType)
        {
            case R.string.eth_address:
                prefix = nanopoolHandler.Eth_main;
                break;
            case R.string.etc_address:
                prefix = nanopoolHandler.Etc_main;
                break;
            case R.string.sia_address:
                prefix = nanopoolHandler.Sia_main;
                break;
            case R.string.xmr_address:
                prefix = nanopoolHandler.Xmr_main;
                break;
            case R.string.pasc_address:
                prefix = nanopoolHandler.Pasc_main;
                break;
            case R.string.etn_address:
                prefix = nanopoolHandler.Etn_main;
                break;
            case R.string.zec_address:
                prefix = nanopoolHandler.Zec_main;
                break;
        }
        //LayoutInflater li = LayoutInflater.from(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText input = new EditText(this);
        input.setLayoutParams(params);

        if (failedInput != null)
        {
            input.setText(failedInput);
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Insert your wallet address");
        //dialog.setMessage();
        dialog.setView(input);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i)
            {
                final String textIn = input.getText().toString();

                final ProgressDialog pd = new ProgressDialog(AccountActivity.this);
                pd.setTitle("Checking account");
                pd.setMessage("Checking if your account exists in Nanopool");
                pd.show();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        HttpHandler httpHandler = new HttpHandler();
                        final nanopoolHandler nanoH = new nanopoolHandler();
                        boolean isAllowed = httpHandler.setUrl(nanoH.accountexist(prefix, textIn));
                        if (isAllowed)
                        {
                            final String apiResponse = httpHandler.getApiResponse(httpHandler.getUrl());
                            Accountcheck_status = Boolean.parseBoolean(nanoH.getJSONField(apiResponse, "status"));
                            AccountActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (Accountcheck_status == true)
                                    {
                                        String data = nanoH.getJSONField(apiResponse, "data");
                                        pd.dismiss();
                                        Toast.makeText(AccountActivity.this, "Response from nanopool " + data, Toast.LENGTH_LONG).show();
                                        SharedPreferencesHandler handler = new SharedPreferencesHandler();
                                        handler.setString(AccountActivity.this, addressType, textIn);
                                        Toast.makeText(AccountActivity.this, handler.getString(AccountActivity.this, addressType), Toast.LENGTH_LONG).show();
                                        dialogInterface.dismiss();
                                        if (Address_Passed)
                                            AccountActivity.super.onBackPressed();
                                        else
                                        {
                                            AccountActivity.this.updateAccounts(addressType, textIn, false);
                                        }
                                    }
                                    else
                                    {
                                        pd.dismiss();
                                        String data = nanoH.getJSONField(apiResponse, "error");
                                        Toast.makeText(AccountActivity.this, "Response from nanopool " + data, Toast.LENGTH_LONG).show();
                                        dialogInterface.dismiss();
                                        showInputDialog(addressType, textIn);
                                    }
                                }
                            });
                        }
                        //if not
                        pd.dismiss();
                    }
                });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (Address_Passed)
                    AccountActivity.super.onBackPressed();
            }
        });

        if (Address_Passed)
            dialog.setCancelable(false);

        dialog.show();

    }

    public void updateAccounts(int addressType, String address, boolean delete)
    {
        RecyclerView rv = (RecyclerView)findViewById(R.id.activity_account_recyclerView);
        accountAdapter adapter = (accountAdapter) rv.getAdapter();
        if (adapter == null)
        {
            LoadAccounts();
            return;
        }
        ArrayList<account> items = adapter.getItems();
        int pos = itemExists(items, addressType);
        if (pos == -1)
        {
            SharedPreferencesHandler sph = new SharedPreferencesHandler();
            account ac = getAccountData(addressType, sph);
            if (ac != null)
            {
                adapter.addItem(ac);
            }
            //adapter.addItem();
        }
        else
        {
            if (delete == false)
            {
                adapter.updateItem(pos, address);
            }
            else
            {
                adapter.removeItem(pos);
            }
        }

    }
    public int itemExists(ArrayList<account> items, int key)
    {
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).WalletRid == key)
                return i;
        }
        return -1;
    }


}

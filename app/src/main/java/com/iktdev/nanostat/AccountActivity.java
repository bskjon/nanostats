package com.iktdev.nanostat;

import android.Manifest;
import android.animation.Animator;
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
import android.support.v4.content.ContextCompat;
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

    public static final int REQUEST_CODE = 10;
    public static final int CAMERA_PERMISSION_REQUEST = 30;


    private boolean Address_Passed = false;

    FloatingActionButton fab, fab1, fab2, fab3, fab4, fab5, fab6, fab7;
    LinearLayout fabLayout1, fabLayout2, fabLayout3, fabLayout4, fabLayout5, fabLayout6, fabLayout7;
    View fabBGLayout;
    boolean isFabOpen = false;


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

        fabLayout1= (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2= (LinearLayout) findViewById(R.id.fabLayout2);
        fabLayout3= (LinearLayout) findViewById(R.id.fabLayout3);
        fabLayout4 = (LinearLayout) findViewById(R.id.fabLayout4);
        fabLayout5 = (LinearLayout) findViewById(R.id.fabLayout5);
        fabLayout6 = (LinearLayout) findViewById(R.id.fabLayout6);
        fabLayout7 = (LinearLayout) findViewById(R.id.fabLayout7);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2= (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab5 = (FloatingActionButton) findViewById(R.id.fab5);
        fab6 = (FloatingActionButton) findViewById(R.id.fab6);
        fab7 = (FloatingActionButton) findViewById(R.id.fab7);
        fabBGLayout=findViewById(R.id.fabBGLayout);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView)findViewById(R.id.activity_account_recyclerView)).setLayoutManager(linearLayoutManager);

        setFabListners();
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
    private int CurrentWalletId = -1;
    public void showInputDialog(final int addressType, String failedInput)
    {

        String WalletTypeText = "";
        switch (addressType)
        {
            case R.string.eth_address:
                prefix = nanopoolHandler.Eth_main;
                WalletTypeText = "ETH";
                break;
            case R.string.etc_address:
                prefix = nanopoolHandler.Etc_main;
                WalletTypeText = "ETC";
                break;
            case R.string.sia_address:
                prefix = nanopoolHandler.Sia_main;
                WalletTypeText = "SIA";
                break;
            case R.string.xmr_address:
                prefix = nanopoolHandler.Xmr_main;
                WalletTypeText = "XMR";
                break;
            case R.string.pasc_address:
                prefix = nanopoolHandler.Pasc_main;
                WalletTypeText = "PASC";
                break;
            case R.string.etn_address:
                prefix = nanopoolHandler.Etn_main;
                WalletTypeText = "ETN";
                break;
            case R.string.zec_address:
                prefix = nanopoolHandler.Zec_main;
                WalletTypeText = "ZEC";
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
        dialog.setTitle(R.string.AccountActivity_AddWalletTitle);
        dialog.setMessage(getString(R.string.AccountActivity_AddWalletMessage_P1) + " " + WalletTypeText + " " + getString(R.string.AccountActivity_AddWalletMessage_P2));
        dialog.setView(input);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i)
            {
                final String textIn = input.getText().toString();

                final ProgressDialog pd = new ProgressDialog(AccountActivity.this);
                pd.setTitle(R.string.AccountActivity_CheckingAccount_ProgressDialogTitle);
                pd.setMessage(AccountActivity.this.getString(R.string.AccountActivity_CheckingAccount_ProgressDialogMessage));
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
                CurrentWalletId = -1;
            }
        });

        dialog.setNeutralButton("Scan code", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                CurrentWalletId = addressType;
                if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }
                else
                {
                    Intent intent = new Intent(AccountActivity.this, ScanActivity.class);
                    intent.putExtra("WalletId", addressType);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CurrentWalletId = -1;
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

    private void setFabListners()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabOpen)
                {
                    showFABMenu();
                }
                else
                    closeFABMenu();
            }
        });
        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.eth_address, null);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.etc_address, null);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.zec_address, null);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.sia_address, null);
            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.xmr_address, null);
            }
        });
        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.pasc_address, null);
            }
        });
        fab7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(R.string.etn_address, null);
            }
        });



    }

    private void showFABMenu(){

        isFabOpen = true;

        RecyclerView rv = (RecyclerView)findViewById(R.id.activity_account_recyclerView);
        accountAdapter adapter = (accountAdapter) rv.getAdapter();
        if (adapter == null)
        {
            LoadAccounts();
            return;
        }
        ArrayList<LinearLayout> visibleFabs = new ArrayList<>();
        ArrayList<account> visibelAccounts = adapter.getItems();

        if (itemExists(visibelAccounts, R.string.eth_address) == -1)
            visibleFabs.add(fabLayout1);
        if (itemExists(visibelAccounts, R.string.etc_address) == -1)
            visibleFabs.add(fabLayout2);
        if (itemExists(visibelAccounts, R.string.zec_address) == -1)
            visibleFabs.add(fabLayout3);
        if (itemExists(visibelAccounts, R.string.sia_address) == -1)
            visibleFabs.add(fabLayout4);
        if (itemExists(visibelAccounts, R.string.xmr_address) == -1)
            visibleFabs.add(fabLayout5);
        if (itemExists(visibelAccounts, R.string.pasc_address) == -1)
            visibleFabs.add(fabLayout6);
        if (itemExists(visibelAccounts, R.string.etn_address) == -1)
            visibleFabs.add(fabLayout7);

        int[] dimenArray = {
                R.dimen.standard_55,
                R.dimen.standard_100,
                R.dimen.standard_145,
                R.dimen.standard_195,
                R.dimen.standard_245,
                R.dimen.standard_295,
                R.dimen.standard_345
        };


        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(225);
        for(int i = 0; i < visibleFabs.size(); i++)
        {
            LinearLayout ll = visibleFabs.get(i);
            ll.setVisibility(View.VISIBLE);
            ll.animate().translationY(-getResources().getDimension(dimenArray[i]));
        }
    }

    private void closeFABMenu(){

        isFabOpen = false;

        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-225);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0);

        fabLayout4.animate().translationY(0);
        fabLayout5.animate().translationY(0);
        fabLayout6.animate().translationY(0);
        fabLayout7.animate().translationY(0).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFabOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                    fabLayout4.setVisibility(View.GONE);
                    fabLayout5.setVisibility(View.GONE);
                    fabLayout6.setVisibility(View.GONE);
                    fabLayout7.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }


            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                Barcode barcode = data.getParcelableExtra("barcode");
                int WalletType = data.getIntExtra("WalletId", -1);
                if (WalletType != -1)
                    showInputDialog(WalletType, barcode.displayValue);

                Toast.makeText(AccountActivity.this, "Data read: " + barcode.displayValue, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(AccountActivity.this, ScanActivity.class);
            intent.putExtra("WalletId", CurrentWalletId);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }








    @Override
    public void onBackPressed() {

        if(isFabOpen){
            closeFABMenu();
        }else{

            super.onBackPressed();

        }

    }
}

package com.iktdev.nanostat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.iktdev.nanostat.core.SharedPreferencesHandler;

import java.io.File;
import java.io.FileNotFoundException;

public class AccountActivity extends AppCompatActivity {

    private boolean Address_Passed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = getIntent();
        Bundle b = it.getExtras();
        if (b != null)
        {
            int address = b.getInt("address");
            if (address != 0)
            {
                Address_Passed = true;
                showInputDialog(address);
            }
        }


        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    private void showInputDialog(final int addressType)
    {
        //LayoutInflater li = LayoutInflater.from(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText input = new EditText(this);
        input.setLayoutParams(params);


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Insert your wallet address");
        //dialog.setMessage();
        dialog.setView(input);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String textIn = input.getText().toString();
                SharedPreferencesHandler handler = new SharedPreferencesHandler();
                handler.setString(AccountActivity.this, addressType, textIn);
                Toast.makeText(AccountActivity.this, handler.getString(AccountActivity.this, addressType), Toast.LENGTH_LONG).show();

                if (Address_Passed)
                    AccountActivity.super.onBackPressed();
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



}

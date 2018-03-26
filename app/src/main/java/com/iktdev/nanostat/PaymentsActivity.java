package com.iktdev.nanostat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.iktdev.nanostat.core.nanopoolHandler;

public class PaymentsActivity extends AppCompatActivity {

    private String Address = null;
    private String Prefix = null;
    private String CryptoShort = null;
    private int Crypto = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Intent intent = getIntent();
        Address = intent.getExtras().getString("address");
        Crypto = intent.getExtras().getInt("cryptoId");
        Prefix = intent.getExtras().getString("prefix");
        CryptoShort = intent.getExtras().getString("cryptoShort");

        if (Address == null && Prefix == null && CryptoShort == null && Crypto == -1)
            return;

        nanopoolHandler nh = new nanopoolHandler();
        nh.applyPayments(this, nh.payments(Prefix, Address), (ListView) findViewById(R.id.paymentsListView), CryptoShort);

    }
}

package com.iktdev.nanostat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iktdev.nanostat.core.nanopoolHandler;

public class PaymentsActivity extends AppCompatActivity {

    private String Address = "";
    private int Crypto = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Intent intent = getIntent();
        Address = intent.getExtras().getString("address");
        Crypto = intent.getExtras().getInt("cryptoId");

        nanopoolHandler nh = new nanopoolHandler();
        nh.applyPayments(this, );

    }
}

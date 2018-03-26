package com.iktdev.nanostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.iktdev.nanostat.R;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Intent intent = getIntent();
        Address = intent.getExtras().getString("address");
        Crypto = intent.getExtras().getInt("cryptoId");
        Prefix = intent.getExtras().getString("prefix");
        CryptoShort = intent.getExtras().getString("cryptoShort");

        if (Address == null && Prefix == null && CryptoShort == null && Crypto == -1)
            return;

        nanopoolHandler nh = new nanopoolHandler();
        nh.applyPayments(this, nh.payments(Prefix, Address), (ListView) findViewById(R.id.paymentsListView), CryptoShort);

        /* rv = (RecyclerView)getView().findViewById(R.id.fragment_overview_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);

        rv.setHasFixedSize(false);*/

    }
}

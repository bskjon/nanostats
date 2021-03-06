package com.iktdev.nanostat;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iktdev.nanostat.adapters.accountAdapter;
import com.iktdev.nanostat.adapters.overviewAdapter;
import com.iktdev.nanostat.classes.overview;
import com.iktdev.nanostat.core.SharedPreferencesHandler;
import com.iktdev.nanostat.core.nanopoolHandler;

import java.util.ArrayList;

public class OverviewFragment extends Fragment {

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    public SwipeRefreshLayout srl;
    RecyclerView rv;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rv = (RecyclerView)getView().findViewById(R.id.fragment_overview_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);

        rv.setHasFixedSize(false);

        srl = (SwipeRefreshLayout)getView().findViewById(R.id.fragment_overview_refreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv.setAdapter(null);
                setValue(true);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        rv = (RecyclerView)getView().findViewById(R.id.fragment_overview_recyclerview);
        overviewAdapter adapter = (overviewAdapter) rv.getAdapter();
        if (adapter == null)
        {
            setValue(false);
        }
        else
        {
            rv.setAdapter(null);
            setValue(false);
        }




    }

    public void setValue(boolean IsPulledToRefresh)
    {
        ArrayList<overview> items = new ArrayList<>();
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
            overview ovit = getAccountData(wallets[i], sph);
            if (ovit != null)
            {
                items.add(ovit);
            }
        }

        if (items.size() > 0)
        {
            getView().findViewById(R.id.fragment_overview_noAccountsAdded).setVisibility(View.GONE);
        }

        overviewAdapter adapter = new overviewAdapter(getActivity(), items);
        rv.setAdapter(adapter);
        nanopoolHandler api = new nanopoolHandler();
        api.applyOverview(getActivity(), items, rv, srl);


        /*overviewAdapter adapter = new overviewAdapter(getActivity(), items);
        ((RecyclerView)getView().findViewById(R.id.fragment_overview_recyclerview)).setAdapter(adapter);*/
    }

    public overview getAccountData(int id, SharedPreferencesHandler sph)
    {
        overview ov = new overview();
        String address = sph.getString(getActivity(), id);
        if (address != null && address.length() > 0)
        {
            ov.WalletId = id;
            ov._address = address;
            switch (id)
            {
                case R.string.zec_address:
                    ov._prefix = nanopoolHandler.Zec_main;
                    ov.WalletImageId = R.drawable.ic_zcash;
                    ov.WalletShortText = "ZEC";
                    ov.WalletColorId = R.color.ZECcolor;
                    break;

                case R.string.eth_address:
                    ov._prefix = nanopoolHandler.Eth_main;
                    ov.WalletImageId = R.drawable.ic_etherum;
                    ov.WalletShortText = "ETH";
                    ov.WalletColorId = R.color.ETHcolor;
                    break;

                case R.string.etc_address:
                    ov._prefix = nanopoolHandler.Etc_main;
                    ov.WalletImageId = R.drawable.ic_etherum_classic;
                    ov.WalletShortText = "ETC";
                    ov.WalletColorId = R.color.ETCcolor;
                    break;

                case R.string.sia_address:
                    ov._prefix = nanopoolHandler.Sia_main;
                    ov.WalletImageId = R.drawable.ic_siacoin;
                    ov.WalletShortText = "SIA";
                    ov.WalletColorId = R.color.SIAcolor;
                    break;

                case R.string.xmr_address:
                    ov._prefix = nanopoolHandler.Xmr_main;
                    ov.WalletImageId = R.drawable.ic_monero;
                    ov.WalletShortText = "XMR";
                    ov.WalletColorId = R.color.XMRcolor;
                    break;

                case R.string.pasc_address:
                    ov._prefix = nanopoolHandler.Pasc_main;
                    ov.WalletImageId = R.drawable.ic_pascal;
                    ov.WalletShortText = "PASC";
                    ov.WalletColorId = R.color.PASCcolor;
                    break;

                case R.string.etn_address:
                    ov._prefix = nanopoolHandler.Etn_main;
                    ov.WalletImageId = R.drawable.ic_electroneum;
                    ov.WalletShortText = "ETN";
                    ov.WalletColorId = R.color.ETNcolor;
                    break;

                default:
                    return null;
            }
            return ov;
        }
        return null;
    }



}

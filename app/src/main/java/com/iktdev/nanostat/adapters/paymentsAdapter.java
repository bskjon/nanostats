package com.iktdev.nanostat.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iktdev.nanostat.R;
import com.iktdev.nanostat.classes.Payments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Brage on 26.03.2018.
 */

public class paymentsAdapter extends BaseAdapter
{
    Activity context;
    ArrayList<Payments> items;

    public paymentsAdapter(Activity context, ArrayList<Payments> items)
    {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view== null)
            view = LayoutInflater.from(context).inflate(R.layout.adapter_payments, viewGroup, false);

        Payments p = items.get(i);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(p.date*1000));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        ((TextView)view.findViewById(R.id.adapter_paymentsDate)).setText(sdf.format(calendar));
        ((TextView)view.findViewById(R.id.adapter_paymentsBalance)).setText(String.valueOf(p.amount));

        return view;
    }
}

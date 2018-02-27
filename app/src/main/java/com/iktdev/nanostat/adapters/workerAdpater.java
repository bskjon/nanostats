package com.iktdev.nanostat.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iktdev.nanostat.R;
import com.iktdev.nanostat.core.Workers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Brage on 20.02.2018.
 */

public class workerAdpater extends RecyclerView.Adapter<workerAdpater.ViewHolder>
{
    Activity context;
    ArrayList<Workers> items;

    public workerAdpater(Activity context, ArrayList<Workers> items)
    {
        this.context = context;
        this.items = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView id;
        TextView lastshare;
        TextView rating;
        TextView rate;
        TextView rateType;

        ViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.adapter_workers_cardView);
            id = (TextView)itemView.findViewById(R.id.adapter_workers_id);
            rating = (TextView)itemView.findViewById(R.id.adapter_workers_rating);
            lastshare = (TextView)itemView.findViewById(R.id.adapter_workers_lastSeen);
            rate = (TextView)itemView.findViewById(R.id.adapter_workers_rate);
            rateType = (TextView)itemView.findViewById(R.id.adapter_workers_rateType);

        }

    }

    @Override
    public workerAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_workers, parent, false);
        return new workerAdpater.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(workerAdpater.ViewHolder holder, int position)
    {
        Workers w = items.get(position);
        holder.id.setText(w.id);
        holder.rateType.setText(w.rateType);
        holder.rate.setText(String.valueOf(w.hashrate));
        holder.rating.setText(String.valueOf(Math.round(w.rating)));

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        holder.lastshare.setText(df.format(w.lastshare));

    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return -1;
    }
}

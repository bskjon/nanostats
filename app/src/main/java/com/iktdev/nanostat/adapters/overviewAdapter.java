package com.iktdev.nanostat.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.iktdev.nanostat.CustomViews.CircleProgressBar;
import com.iktdev.nanostat.R;
import com.iktdev.nanostat.classes.overview;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Brage on 22.02.2018.
 */

public class overviewAdapter extends RecyclerView.Adapter<overviewAdapter.ViewHolder>
{
    Activity context;
    ArrayList<overview> items;

    public overviewAdapter(Activity context, ArrayList<overview> items)
    {
        this.context = context;
        this.items = items;
    }

    @Override
    public overviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_overview, parent, false);
        return new overviewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        overview ov = items.get(position);
        holder.walletIcon.setImageResource(ov.WalletImageId);
        holder.walletName.setText(ov.WalletShortText);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        LineChart lc;
        ImageView walletIcon;
        TextView walletName;
        CircleProgressBar cpb;
        TextView balance;
        TextView payoutLimit;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.adapter_overview_card);
            walletIcon = (ImageView)itemView.findViewById(R.id.adapter_overview_Icon);
            cpb = (CircleProgressBar)itemView.findViewById(R.id.adapter_overview_Progress);
            walletName = (TextView)itemView.findViewById(R.id.adapter_overview_WalletType);
            lc = (LineChart)itemView.findViewById(R.id.adapter_overview_chart);


        }
    }
}

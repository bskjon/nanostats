package com.iktdev.nanostat.adapters;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.iktdev.nanostat.CustomViews.CircleProgressBar;
import com.iktdev.nanostat.MainActivity;
import com.iktdev.nanostat.R;
import com.iktdev.nanostat.classes.ChartData;
import com.iktdev.nanostat.classes.overview;
import com.iktdev.nanostat.core.helper;
import com.iktdev.nanostat.core.nanopoolHandler;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<overview> getItems()
    {
        return this.items;
    }

    public boolean replaceItems(ArrayList<overview> items)
    {
        this.items = items;
        notifyDataSetChanged();
        return true;
    }

    public void addItem(overview item)
    {
        this.items.add(item);
        notifyDataSetChanged();
    }


    public boolean updateItem(overview item, int position)
    {
        overview ov = this.items.get(position);
        ov = item;
        notifyDataSetChanged();
        this.notifyItemChanged(position);
        return true;

    }

    @Override
    public overviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_overview, parent, false);
        return new overviewAdapter.ViewHolder(v);
    }

    public DecimalFormat decimalFormat = new DecimalFormat("#0.00000");

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        helper _helper = new helper();
        overview ov = items.get(position);
        final int WalletId = ov.WalletId;
        holder.walletIcon.setImageResource(ov.WalletImageId);
        holder.balance_valuta.setText(ov.WalletShortText);
        holder.balance.setText(decimalFormat.format(ov.Balance));

        if (ov.Balance > 0.0 && ov.PayoutLimit > 0.0)
        {
            holder.cpb.setColor(_helper.getNonThemedColor(context, ov.WalletColorId));
            double progress = ((ov.Balance / ov.PayoutLimit)*100);
            holder.cpb.setProgressWithAnimation((float)progress);
        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (context instanceof MainActivity)
                {
                    int entry = -1;
                    switch (WalletId)
                    {
                        case R.string.zec_address:
                            entry = 4;
                            break;

                        case R.string.eth_address:
                            entry = 1;
                            break;

                        case R.string.etc_address:
                            entry = 2;
                            break;

                        case R.string.sia_address:
                            entry = 3;
                            break;

                        case R.string.xmr_address:
                            entry = 5;
                            break;

                        case R.string.pasc_address:
                            entry = 6;
                            break;

                        case R.string.etn_address:
                            entry = 7;
                            break;
                    }

                    NavigationView nv = ((MainActivity)context).findViewById(R.id.nav_view);
                    nv.getMenu().getItem(entry).setChecked(true);
                    ((MainActivity)context).onNavigationItemSelected(nv.getMenu().getItem(entry));

                }
                else
                {
                    Toast.makeText(context, "uhmm this was not suppose to happen..", Toast.LENGTH_SHORT).show();
                }



            }
        });







        List<Entry> entryList = new ArrayList<>();
        if (ov.chartData != null && ov.chartData .size() > 0)
        {
            for (ChartData data : ov.chartData)
            {
                //entries.add(new Entry((long)data.date, data.shares));
                entryList.add(new Entry(ov.chartData.indexOf(data), data.shares));
            }
            holder.lc.setTouchEnabled(false);
            holder.lc.setData(getLineData( new LineDataSet(entryList, "Shares"), _helper, ov));
            holder.lc.getDescription().setEnabled(false);
            holder.lc.getXAxis().setEnabled(false);
            holder.lc.getAxisRight().setEnabled(false);
            holder.lc.getAxisLeft().setEnabled(false);
            holder.lc.setDrawMarkers(false);
            holder.lc.getAxisLeft().setDrawGridLines(false);
            holder.lc.getXAxis().setDrawGridLines(false);
            holder.lc.getData().setHighlightEnabled(false);
            holder.lc.getLegend().setEnabled(false);
            holder.lc.invalidate();
            holder.lc.setViewPortOffsets(0,0,0,0);
            holder.lc.post(new Runnable() {
                @Override
                public void run() {
                    holder.lc.invalidate();
                }
            });

        }
    }

    private LineData getLineData(LineDataSet dataset, helper _helper, overview ov)
    {
        dataset.setDrawValues(false);
        dataset.setDrawCircleHole(false);
        dataset.setDrawCircles(false);
        dataset.setDrawFilled(true);
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setFillColor(_helper.getNonThemedColor(context, ov.WalletColorId));
        dataset.setColor(_helper.getNonThemedColor(context, ov.WalletColorId));

        return new LineData(dataset);
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
        CircleProgressBar cpb;
        TextView balance;
        TextView balance_valuta;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.adapter_overview_card);
            walletIcon = (ImageView)itemView.findViewById(R.id.adapter_overview_Icon);
            cpb = (CircleProgressBar)itemView.findViewById(R.id.adapter_overview_Progress);
            lc = (LineChart)itemView.findViewById(R.id.adapter_overview_chart);
            balance = (TextView)itemView.findViewById(R.id.adapter_overview_balance);
            balance_valuta = (TextView)itemView.findViewById(R.id.adapter_overview_balanceValuta);


        }
    }
}

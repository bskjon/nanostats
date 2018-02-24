package com.iktdev.nanostat.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iktdev.nanostat.DonateActivity;
import com.iktdev.nanostat.R;
import com.iktdev.nanostat.classes.account;
import com.iktdev.nanostat.core.helper;

import java.util.ArrayList;

/**
 * Created by Brage on 24.02.2018.
 */

public class donateAdapter extends RecyclerView.Adapter<donateAdapter.ViewHolder>
{
    Activity context;
    ArrayList<account> items;

    public donateAdapter(Activity context, ArrayList<account> items){
        this.context = context;
        this.items = items;
    }

    public ArrayList<account> getItems()
    {
        return this.items;
    }

    public void removeItem(int position)
    {
        this.items.remove(position);
        notifyDataSetChanged();
    }
    public void addItem(account item)
    {
        this.items.add(item);
        notifyDataSetChanged();
    }
    public void updateItem(int position, String nAddress)
    {
        this.items.get(position).Address = nAddress;
        notifyDataSetChanged();
    }



    @Override
    public donateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_donate, parent, false);
        return new donateAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        account ac = items.get(position);
        holder.address.setText(ac.Address);
        holder.cryptoIcon.setImageResource(ac.WalletImageId);
        holder.cryptoName.setText(ac.ReadableWalletType);
        helper h = new helper();
        ((GradientDrawable)holder.rl.getBackground()).setColor(h.getNonThemedColor(context, ac.WalletColorId));

        final int WalletrID = ac.WalletRid;
        final String WalletType = ac.ReadableWalletType;
        final String WalletAddr = ac.Address;
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PopupMenu pop = new PopupMenu(context, holder.menuButton);
                pop.inflate(R.menu.menu_donate);
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.action_copy:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(WalletType, WalletAddr);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context, "Address copied to your clipboard", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return false;
                    }
                });
                pop.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        RelativeLayout rl;
        ImageView cryptoIcon;
        TextView cryptoName;
        TextView address;
        ImageButton menuButton;

        public ViewHolder(View itemView)
        {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.adapter_workers_cardView);
            cryptoIcon = (ImageView)itemView.findViewById(R.id.adapter_accounts_walletIcon);
            cryptoName = (TextView)itemView.findViewById(R.id.adapter_accounts_walletType);
            address = (TextView)itemView.findViewById(R.id.adapter_accounts_walletAddress);
            menuButton = (ImageButton)itemView.findViewById(R.id.adapter_accounts_menuButton);
            rl = (RelativeLayout)itemView.findViewById(R.id.adapter_donateIcon_background);

        }
    }
}

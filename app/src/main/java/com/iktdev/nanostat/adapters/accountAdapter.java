package com.iktdev.nanostat.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.iktdev.nanostat.AccountActivity;
import com.iktdev.nanostat.R;
import com.iktdev.nanostat.classes.account;
import com.iktdev.nanostat.core.SharedPreferencesHandler;

import java.util.ArrayList;

/**
 * Created by Brage on 21.02.2018.
 */

public class accountAdapter extends RecyclerView.Adapter<accountAdapter.ViewHolder>
{
    Activity context;
    ArrayList<account> items;

    public accountAdapter(Activity context, ArrayList<account> items){
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
    public accountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_accounts, parent, false);
        return new accountAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        account ac = items.get(position);
        holder.address.setText(ac.Address);
        holder.cryptoIcon.setImageResource(ac.WalletImageId);
        holder.cryptoName.setText(ac.ReadableWalletType);
        final int WalletrID = ac.WalletRid;
        final String WalletType = ac.ReadableWalletType;
        final String WalletAddr = ac.Address;
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PopupMenu pop = new PopupMenu(context, holder.menuButton);
                pop.inflate(R.menu.activity_account_wallet);
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.action_edit:
                                if (context instanceof AccountActivity)
                                {
                                    ((AccountActivity) context).showInputDialog(WalletrID, WalletAddr);
                                }
                                break;

                            case R.id.action_copy:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText(WalletType, WalletAddr);
                                clipboard.setPrimaryClip(clip);
                                break;

                            case R.id.action_delete:
                                SharedPreferencesHandler sph = new SharedPreferencesHandler();
                                sph.deleteString(context, WalletrID);
                                if (context instanceof AccountActivity)
                                {
                                    ((AccountActivity) context).updateAccounts(WalletrID, WalletAddr, true);
                                }
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

        }
    }
}

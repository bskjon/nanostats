package com.iktdev.nanostat;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.iktdev.nanostat.core.SharedPreferencesHandler;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        CrashManager.register(this, getMetaString("net.hockeyapp.android.appIdentifier"), new CrashManagerHandler());
        setContentView(R.layout.activity_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try
        {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = getString(R.string.VersionNumber_PreText) + " " + packageInfo.versionName;


            TextView versionText = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewAppVersion);
            if (versionText != null)
                versionText.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if (savedInstanceState == null)
        {
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment visibleFragment = null;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferencesHandler sph = new SharedPreferencesHandler();

        if (id == R.id.nav_overview)
        {
            setVisibleFragment((Fragment) new OverviewFragment());
        }
        else if (id == R.id.nav_eth)
        {
            String address = sph.getString(this, R.string.eth_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.eth_address, address));
            else
                addAccountDialog(R.string.eth_address);
        }
        else if (id == R.id.nav_etc)
        {
            String address = sph.getString(this, R.string.etc_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.etc_address, address));
            else
                addAccountDialog(R.string.etc_address);
        }
        else if (id == R.id.nav_sia)
        {
            String address = sph.getString(this, R.string.sia_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.sia_address, address));
            else
                addAccountDialog(R.string.sia_address);
        }
        else if (id == R.id.nav_zec)
        {
            String address = sph.getString(this, R.string.zec_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.zec_address, address));
            else
                addAccountDialog(R.string.zec_address);
        }
        else if (id == R.id.nav_xmr)
        {
            String address = sph.getString(this, R.string.xmr_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.xmr_address, address));
            else
                addAccountDialog(R.string.xmr_address);
        }
        else if (id == R.id.nav_pasc)
        {
            String address = sph.getString(this, R.string.pasc_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.pasc_address, address));
            else
                addAccountDialog(R.string.pasc_address);
        }
        else if (id == R.id.nav_etn)
        {
            String address = sph.getString(this, R.string.etn_address);
            if (address != null)
                setVisibleFragment((Fragment) createFragment(R.string.etn_address, address));
            else
                addAccountDialog(R.string.etn_address);
        }
        else if (id == R.id.nav_myAccounts)
        {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_donate)
        {
            Intent intent = new Intent(MainActivity.this, DonateActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private NanopoolStatsFragment createFragment(int id, String address)
    {
        Bundle b = new Bundle();
        b.putInt("id", id);
        b.putString("address", address);
        NanopoolStatsFragment fragment = new NanopoolStatsFragment();
        fragment.setArguments(b);
        return fragment;
    }


    public void addAccountDialog(final int type)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("No account found!");
        b.setMessage("Would you like to add an account now?");
        b.setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                intent.putExtra("address", type);
                startActivity(intent);
            }
        });
        b.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.setCancelable(false);
        AlertDialog alertDialog = b.create();
        alertDialog.show();
    }


    public void setVisibleFragment(Fragment f)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (visibleFragment == null)
        {
            ft.add(R.id.fragmentContainer, f);
            ft.commit();
        }
        else
        {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.replace(R.id.fragmentContainer, f);
            ft.commit();
        }
        //getFragmentManager().popBackStack();
        visibleFragment = f;
    }


    public String getMetaString(String key)
    {
        try {
            ApplicationInfo appinfo = this.getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            if (appinfo != null)
            {
                return appinfo.metaData.get(key).toString();
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class CrashManagerHandler extends CrashManagerListener
    {
        @Override
        public boolean shouldAutoUploadCrashes()
        {
            return true;
        }
    }
}

package com.iktdev.nanostat.core;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Brage on 18.02.2018.
 */

public class nanopoolHandler
{
    public static String Zec_main = "https://api.nanopool.org/v1/zec/";

    public String balance(String prefix, String Address)
    {
        return prefix + "balance/" + Address;
    }

    public boolean getBalance(final Activity context, final TextView balanceTextView, String url)
    {
        final HttpHandler httpHandler = new HttpHandler();
        boolean urlAllowd = httpHandler.setUrl(url);
        if(urlAllowd)
        {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run()
                {
                    String res = httpHandler.getApiResponse(httpHandler.getUrl());
                    final String current_balance = getJSONField(res, "data");

                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            balanceTextView.setText(current_balance);
                        }
                    });
                }
            });
            return true;
        }
        return false;
    }



    public String getJSONField(String jsonStrng, String targetKey)
    {
        try {
            JSONObject jo = new JSONObject(jsonStrng);
            String val = jo.getString(targetKey);
            if (val != null || val.length() != 0)
            {
                return val;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void setStyle(TextView cryptotype, String newText, int color)
    {
        GradientDrawable recolor = (GradientDrawable)cryptotype.getBackground().getCurrent();
        recolor.setStroke(8, color);
        cryptotype.invalidate();
        cryptotype.setText(newText);
        cryptotype.setTextColor(color);
    }
}

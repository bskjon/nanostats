package com.iktdev.nanostat.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Brage on 19.02.2018.
 */

public class SharedPreferencesHandler
{

    public String getString(Activity context, int id)
    {
        String key = context.getResources().getResourceEntryName(id);
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String res = sharedPreferences.getString(key, null);
        return res;
    }

    public void setString(Activity context, int id, String value)
    {
        String key = context.getResources().getResourceEntryName(id);
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


}

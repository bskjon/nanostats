package com.iktdev.nanostat.core;

import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;

import com.iktdev.nanostat.R;

/**
 * Created by Brage on 21.02.2018.
 */

public class helper
{
    public int getNonThemedColor(Activity context, int ResId)
    {
        int c = ResourcesCompat.getColor(context.getResources(), ResId, null);
        return c;
    }


}

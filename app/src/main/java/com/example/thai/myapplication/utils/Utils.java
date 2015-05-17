package com.example.thai.myapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.thai.myapplication.R;

/**
 * Created by thaidh on 5/17/15.
 */
public class Utils {

    public static Drawable getRandomDrawable(Context context, int random) {

        switch (random % 4) {
            case 1:
                return context.getResources().getDrawable(R.drawable.demo1);
            case 2:
                return context.getResources().getDrawable(R.drawable.demo2);

            case 3:
                return context.getResources().getDrawable(R.drawable.demo3);

            default:
                return context.getResources().getDrawable(R.drawable.demo4);
        }
    }
}

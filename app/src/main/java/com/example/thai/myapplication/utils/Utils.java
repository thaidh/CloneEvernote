package com.example.thai.myapplication.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

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

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }



    //            etMessage = (EditText) findViewById(R.id.etMessage);
//            etMessage.setBackgroundColor(Color.TRANSPARENT);
//            etMessage.setTextColor(Color.GRAY);
//            etMessage.requestFocus();
//            etMessage.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    Log.i("AAAAA","Before text change:" + s + " " + start + " " + count);
//                    int selectPos = etMessage.getSelectionStart();
//
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editTable) {
//                    Matcher matcher = imageSpanFinder.matcher(editTable);
//
//                    int i = 0;
//                    ImageSpan[] arrayOfImageSpan = editTable.getSpans(0, editTable.length(), ImageSpan.class);
//                    int j = arrayOfImageSpan.length;
//                    while (true) {
//                        if (i >= j) {
//                            int posToFind = 0;
//                            while (matcher.find(posToFind)) {
//                                int start = matcher.start();
//                                int end = matcher.end();
//                                int value = Integer.parseInt(matcher.group(1));
//
//                                posToFind = end < (editTable.length()-1) ? end : (editTable.length()-1);
//
//                                editTable.setSpan(createImageSpan(value), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }
//
//                            break;
//                        }
//                        editTable.removeSpan(arrayOfImageSpan[i]);
//                        i++;
//                    }
//
//
//                }
//            });
    //todo may be used
}
